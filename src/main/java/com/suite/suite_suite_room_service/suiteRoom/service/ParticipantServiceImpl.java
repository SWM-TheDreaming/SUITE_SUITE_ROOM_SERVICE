package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.ResPaymentParticipantDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.kafka.producer.SuiteRoomProducer;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService{
    private final SuiteRoomRepository suiteRoomRepository;
    private final ParticipantRepository participantRepository;
    private final SuiteRoomProducer suiteRoomProducer;
    private final AnpService anpService;

    @Override
    @Transactional
    public void addParticipant(Long suiteRoomId, AuthorizerDto authorizerDto) {
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND));

        Participant hostInfo = participantRepository.findBySuiteRoom_SuiteRoomIdAndIsHost(suiteRoomId, true).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND));

        participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, authorizerDto.getMemberId()).ifPresent(
                member -> { throw new CustomException(StatusCode.ALREADY_EXISTS_PARTICIPANT); });

        if(anpService.getPoint(authorizerDto.getMemberId()) < suiteRoom.getDepositAmount())
            throw new CustomException(StatusCode.FAILED_PAY);

        suiteRoomProducer.sendPaymentMessage(suiteRoom, hostInfo.getMemberId(), authorizerDto, false, true);
    }

    @Override
    @Transactional
    public void removeParticipant(Long suiteRoomId, AuthorizerDto authorizerDto) {
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoomId, authorizerDto.getMemberId(), false).orElseThrow(
                () -> new CustomException(StatusCode.FAILED_REQUEST));

        Participant hostInfo = participantRepository.findBySuiteRoom_SuiteRoomIdAndIsHost(suiteRoomId, true).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND));

        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND));

        if(participant.getStatus().equals(SuiteStatus.READY)) {
            suiteRoomProducer.sendPaymentMessage(suiteRoom, hostInfo.getMemberId(), authorizerDto, false, false);
        }

        if(participant.getIsHost())
            throw new CustomException(StatusCode.CAN_NOT_CALCEL_SUITEROOM);
        participantRepository.deleteBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, authorizerDto.getMemberId());
    }

    @Override
    @Transactional
    public void updatePaymentParticipant(Long suiteRoomId, AuthorizerDto authorizerDto) {
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, authorizerDto.getMemberId())
                .orElseThrow(() -> { throw new CustomException(StatusCode.NOT_FOUND); });

        Participant hostInfo = participantRepository.findBySuiteRoom_SuiteRoomIdAndIsHost(suiteRoomId, true).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND));

        if(participant.getStatus().equals(SuiteStatus.READY))
            throw new CustomException(StatusCode.ALREADY_EXISTS_PARTICIPANT);

        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId)
                .orElseThrow(() -> { throw new CustomException(StatusCode.NOT_FOUND); });

        if(anpService.getPoint(authorizerDto.getMemberId()) < suiteRoom.getDepositAmount())
            throw new CustomException(StatusCode.FAILED_PAY);

        if (participant.getIsHost())
            suiteRoom.openSuiteRoom();

        participant.updateStatus(SuiteStatus.READY);

        suiteRoomProducer.sendPaymentMessage(suiteRoom, hostInfo.getMemberId(), authorizerDto, true, true);
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

        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND));

        List<ResPaymentParticipantDto> resPaymentParticipantDtos = participantRepository.findAllBySuiteRoom_SuiteRoomIdAndStatus(suiteRoomId, SuiteStatus.PLAIN)
                .stream().map(
                        participant -> participant.toResPaymentParticipantDto()
                ).collect(Collectors.toList());

        return resPaymentParticipantDtos;
    }

    @Override
    @Transactional
    public void updateParticipantsStatusReadyToStart(Long suiteRoomId, Long memberId) {
        participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoomId, memberId, true).orElseThrow(() -> new CustomException(StatusCode.FORBIDDEN));

        participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoomId, memberId, true).orElseThrow(() -> new CustomException(StatusCode.FORBIDDEN));
        List<Participant> participants = participantRepository.findAllBySuiteRoom_SuiteRoomIdAndStatus(suiteRoomId, SuiteStatus.READY);

        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomIdAndIsStart(suiteRoomId, false)
                .orElseThrow(() ->  new CustomException(StatusCode.ALEADY_START_OR_NOT_FOUND));

        suiteRoom.startSuiteRoom();
        List<ResPaymentParticipantDto> resPaymentParticipantDtos = participants.stream().map(
                p -> {
                    p.updateStatus(SuiteStatus.START);
                    return p.toResPaymentParticipantDto();
                }).collect(Collectors.toList());

        suiteRoomProducer.suiteRoomContractCreationProducer(suiteRoomId, participants, suiteRoom);
        suiteRoomProducer.suiteRoomStartProducer(suiteRoom, resPaymentParticipantDtos);
    }

}
