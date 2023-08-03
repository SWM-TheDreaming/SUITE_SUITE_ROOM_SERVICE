package com.suite.suite_suite_room_service.suiteRoom.service;

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
                () -> new CustomException(StatusCode.FAILED_REQUEST)
        );

        if(participant.getStatus().equals(SuiteStatus.READY))
            System.out.println("kafka");

        participantRepository.deleteBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, authorizerDto.getMemberId());
    }

}
