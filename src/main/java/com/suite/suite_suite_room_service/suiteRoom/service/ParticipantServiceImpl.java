package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.ResPaymentParticipantDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService{
    private final SuiteRoomRepository suiteRoomRepository;
    private final ParticipantRepository participantRepository;

    @Override
    @Transactional
    public void addParticipant(Long suiteRoomId, AuthorizerDto authorizerDto) {
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND));

        participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, authorizerDto.getMemberId()).ifPresent(
                member -> { throw new CustomException(StatusCode.ALREADY_EXISTS_PARTICIPANT); });

        Participant participant = Participant.builder()
                                        .authorizerDto(authorizerDto)
                                        .status(SuiteStatus.PLAIN)
                                        .isHost(false).build();
        suiteRoom.addParticipant(participant);
        participantRepository.save(participant);
    }

    @Override
    @Transactional
    public void removeParticipant(Long suiteRoomId, AuthorizerDto authorizerDto) {
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, authorizerDto.getMemberId()).orElseThrow(
                () -> new CustomException(StatusCode.FAILED_REQUEST));

        if(participant.getStatus().equals(SuiteStatus.READY))
            System.out.println("kafka");
        if(participant.getIsHost())
            throw new CustomException(StatusCode.CAN_NOT_CALCEL_SUITEROOM);
        participantRepository.deleteBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, authorizerDto.getMemberId());
    }

    @Override
    @Transactional
    public void updatePaymentParticipant(Long suiteRoomId, Long memberId) {
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, memberId)
                .orElseThrow(() -> { throw new CustomException(StatusCode.NOT_FOUND); });
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId)
                .orElseThrow(() -> { throw new CustomException(StatusCode.NOT_FOUND); });
        if (participant.getIsHost())
            suiteRoom.openSuiteRoom();

        participant.updateStatus(SuiteStatus.READY);

        System.out.println("결제서비스 kafka 메시지 큐에 READY 성공 메시지를 넣습니다.");

    }

    @Override
    public List<ResPaymentParticipantDto> listUpPaymentParticipants(Long suiteRoomId) {
        List<ResPaymentParticipantDto> resPaymentParticipantDtos = participantRepository.findAllBySuiteRoom_SuiteRoomIdAndStatus(suiteRoomId, SuiteStatus.READY)
                .stream().map(
                        participant -> participant.toResPaymentParticipantDto()
                ).collect(Collectors.toList());

        return resPaymentParticipantDtos;
    }

    @Override
    public List<ResPaymentParticipantDto> listUpNotYetPaymentParticipants(Long suiteRoomId) {
        List<ResPaymentParticipantDto> resPaymentParticipantDtos = participantRepository.findAllBySuiteRoom_SuiteRoomIdAndStatus(suiteRoomId, SuiteStatus.PLAIN)
                .stream().map(
                        participant -> participant.toResPaymentParticipantDto()
                ).collect(Collectors.toList());

        return resPaymentParticipantDtos;
    }

    @Override
    @Transactional
    public List<ResPaymentParticipantDto> updateParticipantsStatusReadyToStart(Long suiteRoomId) {
        List<ResPaymentParticipantDto> resPaymentParticipantDtos = participantRepository.findAllBySuiteRoom_SuiteRoomIdAndStatus(suiteRoomId, SuiteStatus.READY)
                .stream()
                .map(
                        participant -> {
                            if(participant.getStatus() == SuiteStatus.PLAIN) throw new CustomException(StatusCode.PLAIN_USER_EXIST);

                            participant.updateStatus(SuiteStatus.START);
                            return participant.toResPaymentParticipantDto();
                        }
                ).collect(Collectors.toList());
        System.out.println("kafka 프로듀싱 to 블록체인 서비스");
        return resPaymentParticipantDtos;
    }
}
