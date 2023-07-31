package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.*;
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

import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuiteRoomServiceImpl implements SuiteRoomService{
    private final SuiteRoomRepository suiteRoomRepository;
    private final ParticipantRepository participantRepository;


    @Override
    public List<ResSuiteRoomDto> getAllSuiteRooms() {
        List<SuiteRoom> suiteRooms = suiteRoomRepository.findAll();
        return suiteRooms.stream().map(suiteRoom -> suiteRoom.toResSuiteRoomDto()).collect(Collectors.toList());

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
    public Message createSuiteRoom(ReqSuiteRoomDto reqSuiteRoomDto, AuthorizerDto authorizerDto) {
        suiteRoomRepository.findByTitle(reqSuiteRoomDto.getTitle()).ifPresent(
                (suiteRoom) ->  new CustomException(StatusCode.ALREADY_EXISTS)
        );
        SuiteRoom suiteRoom = reqSuiteRoomDto.toSuiteRoomEntity();
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
    @Transactional
    public Message updateSuiteRoom(ReqUpdateSuiteRoomDto reqUpdateSuiteRoomDto, AuthorizerDto authorizerDto) {
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(reqUpdateSuiteRoomDto.getSuiteRoomId())
                .orElseThrow( () -> new CustomException(StatusCode.NOT_FOUND));

        participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(reqUpdateSuiteRoomDto.getSuiteRoomId(), authorizerDto.getMemberId(), true)
                .orElseThrow( () -> new CustomException(StatusCode.FORBIDDEN));

        suiteRoom.updateSuiteRoom(reqUpdateSuiteRoomDto);
        return new Message(StatusCode.OK);
    }

    @Override
    public Optional<?> commitPaymentStatus() {
        return Optional.empty();
    }
}
