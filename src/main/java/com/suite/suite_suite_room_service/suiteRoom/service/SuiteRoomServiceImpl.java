package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuiteRoomServiceImpl implements SuiteRoomService{
    private final SuiteRoomRepository suiteRoomRepository;
    private final ParticipantRepository participantRepository;

    @Override
    public Optional<List<SuiteRoom>> getAllSuiteRooms() {
        return Optional.empty();
    }

    @Override
    public Optional<SuiteRoom> getSuiteRoom() {
        return Optional.empty();
    }

    @Override
    public Optional<List<SuiteRoom>> getAllProgressRooms() {
        return Optional.empty();
    }

    @Override
    public Optional<List<SuiteRoom>> getAllCompletionRooms() {
        return Optional.empty();
    }

    @Override
    public Message createSuiteRoom(ReqSuiteRoom reqSuiteRoom, AuthorizerDto authorizerDto) {
        SuiteRoom suiteRoom = reqSuiteRoom.toSuiteRoomEntity();
        Participant participant = Participant.builder()
                                        .authorizerDto(authorizerDto)
                                        .status(SuiteStatus.PLAIN)
                                        .isHost(true).build();
        suiteRoom.addParticipant(participant);

        suiteRoomRepository.save(suiteRoom);
        participantRepository.save(participant);

        return new Message(StatusCode.OK);
    }

    @Override
    public Optional<SuiteRoom> joinRoom() {
        return Optional.empty();
    }

    @Override
    public Optional<SuiteRoom> deleteRoom() {
        return Optional.empty();
    }

    @Override
    public Optional<SuiteRoom> renewalRoom() {
        return Optional.empty();
    }

    @Override
    public Optional<?> commitPaymentStatus() {
        return Optional.empty();
    }
}
