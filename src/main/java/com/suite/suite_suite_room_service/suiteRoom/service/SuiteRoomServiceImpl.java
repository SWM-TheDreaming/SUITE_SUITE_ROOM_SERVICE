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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuiteRoomServiceImpl implements SuiteRoomService{
    private final SuiteRoomRepository suiteRoomRepository;
    private final ParticipantRepository participantRepository;


    @Override
    public List<ResSuiteRoomDto> getAllSuiteRooms(AuthorizerDto authorizerDto) {
        List<SuiteRoom> suiteRooms = suiteRoomRepository.findAll();

        return suiteRooms.stream().map(
                suiteRoom -> suiteRoom.toResSuiteRoomDto(
                        participantRepository.countBySuiteRoom_SuiteRoomId(suiteRoom.getSuiteRoomId()),
                        participantRepository.existsBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoom.getSuiteRoomId(), authorizerDto.getMemberId(), true)
                )
        ).collect(Collectors.toList());


    }

    @Override
    public ResSuiteRoomDto getSuiteRoom(Long suiteRoomId, AuthorizerDto authorizerDto) {
        Optional<SuiteRoom> findSuiteRoomBySuiteRoomIdResult = suiteRoomRepository.findById(suiteRoomId);
        findSuiteRoomBySuiteRoomIdResult.orElseThrow(
                () -> {throw new CustomException(StatusCode.SUITE_ROOM_NOT_FOUND);}
        );
        ResSuiteRoomDto resSuiteRoomDto = findSuiteRoomBySuiteRoomIdResult.get().toResSuiteRoomDto(
                participantRepository.countBySuiteRoom_SuiteRoomId(findSuiteRoomBySuiteRoomIdResult.get().getSuiteRoomId()),
                participantRepository.existsBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(findSuiteRoomBySuiteRoomIdResult.get().getSuiteRoomId(), authorizerDto.getMemberId(), true)
        );

        return resSuiteRoomDto;
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
    public void createSuiteRoom(ReqSuiteRoomDto reqSuiteRoomDto, AuthorizerDto authorizerDto) {
        suiteRoomRepository.findByTitle(reqSuiteRoomDto.getTitle()).ifPresent(
                suiteRoom ->  { throw new CustomException(StatusCode.ALREADY_EXISTS_SUITEROOM); }
        );
        SuiteRoom suiteRoom = reqSuiteRoomDto.toSuiteRoomEntity();
        Participant participant = Participant.builder()
                                        .authorizerDto(authorizerDto)
                                        .status(SuiteStatus.PLAIN)
                                        .isHost(true).build();
        suiteRoom.addParticipant(participant);

        suiteRoomRepository.save(suiteRoom);
        participantRepository.save(participant);
    }



    @Override
    @Transactional
    public void deleteSuiteRoom(Long suiteRoomId, AuthorizerDto authorizerDto) {
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoomId, authorizerDto.getMemberId(), true).orElseThrow(
                () -> new CustomException(StatusCode.FORBIDDEN)
        );
        if(participant.getStatus().equals(SuiteStatus.START)) throw new CustomException(StatusCode.NOT_DELETE_SUITE_ROOM);

        suiteRoomRepository.deleteBySuiteRoomId(suiteRoomId);
    }

    @Override
    @Transactional
    public void updateSuiteRoom(ReqUpdateSuiteRoomDto reqUpdateSuiteRoomDto, AuthorizerDto authorizerDto) {
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(reqUpdateSuiteRoomDto.getSuiteRoomId())
                .orElseThrow( () -> new CustomException(StatusCode.NOT_FOUND));

        participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(reqUpdateSuiteRoomDto.getSuiteRoomId(), authorizerDto.getMemberId(), true)
                .orElseThrow( () -> new CustomException(StatusCode.FORBIDDEN));

        suiteRoom.updateSuiteRoom(reqUpdateSuiteRoomDto);
    }

    @Override
    public void checkInParticipant(Long suiteRoomId, Long memberId) {
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, memberId)
                .orElseThrow(() -> { throw new CustomException(StatusCode.NOT_FOUND); });
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId)
                .orElseThrow(() -> { throw new CustomException(StatusCode.NOT_FOUND); });

        if (!suiteRoom.getIsOpen()) {
            if(participant.getIsHost()) {
                suiteRoom.openSuiteRoom();
                participant.updateStatus(SuiteStatus.READY);
            } else {
                throw new CustomException(StatusCode.IS_NOT_OPEN);
            }
        } else {
            participant.updateStatus(SuiteStatus.READY);
        }

        System.out.println("결제서비스 kafka 메시지 큐에 READY 성공 메시지를 넣습니다.");

    }

}
