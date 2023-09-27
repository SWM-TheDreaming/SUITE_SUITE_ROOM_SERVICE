package com.suite.suite_suite_room_service.suiteRoom.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suite.suite_suite_room_service.suiteRoom.dto.*;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;

import com.suite.suite_suite_room_service.suiteRoom.kafka.producer.SuiteRoomProducer;
import com.suite.suite_suite_room_service.suiteRoom.repository.MarkRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuiteRoomServiceImpl implements SuiteRoomService{

    private final SuiteRoomRepository suiteRoomRepository;
    private final ParticipantRepository participantRepository;
    private final MarkRepository markRepository;
    private final SuiteRoomProducer suiteRoomProducer;

    @Override
    public List<ResSuiteRoomListDto> getSuiteRooms(AuthorizerDto authorizerDto, ReqListUpSuiteRoomDto reqListUpSuiteRoomDto, Pageable pageable) {

        /*
        * TO DO 성능 nGrinder로 성능 비교해보기!
        Page<SuiteRoom> suiteRooms = subjects.size() != 0 ?
                suiteRoomRepository.findByIsOpenAndSubjectInOrderByCreatedDateDesc(true, subjects, pageable)
                : suiteRoomRepository.findByIsOpenOrderByCreatedDateDesc(true, pageable);
        */
        List<SuiteRoom> suiteRooms = suiteRoomRepository.findOpenSuiteWithSearch(true, reqListUpSuiteRoomDto.getSubjects(), reqListUpSuiteRoomDto.getKeyword(), pageable);

        return suiteRooms.stream().map(
                suiteRoom -> suiteRoom.toResSuiteRoomListDto(
                        participantRepository.countBySuiteRoom_SuiteRoomId(suiteRoom.getSuiteRoomId()),
                        participantRepository.existsBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoom.getSuiteRoomId(), authorizerDto.getMemberId(), true),
                        participantRepository.findBySuiteRoom_SuiteRoomIdAndIsHost(suiteRoom.getSuiteRoomId(), true).get(),
                        markRepository.countBySuiteRoomId(suiteRoom.getSuiteRoomId())
                )
        ).collect(Collectors.toList());
    }

    @Override
    public ResSuiteRoomDto getSuiteRoom(Long suiteRoomId, AuthorizerDto authorizerDto) {

        SuiteRoom suiteRoom = suiteRoomRepository.findById(suiteRoomId).orElseThrow(
                () -> new CustomException(StatusCode.SUITE_ROOM_NOT_FOUND));

        return suiteRoom.toResSuiteRoomDto(
                participantRepository.countBySuiteRoom_SuiteRoomId(suiteRoom.getSuiteRoomId()),
                participantRepository.existsBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoom.getSuiteRoomId(), authorizerDto.getMemberId(), true),
                markRepository.countBySuiteRoomId(suiteRoomId)
        );

    }

    @Override
    public void validatePassword(Long suiteRoomId, int password) {
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND));
        if(suiteRoom.getPassword() != password) throw new CustomException(StatusCode.PASSWORD_NOT_FOUND);
    }

    @Override
    public List<ResConditionSuiteRoomDto> getProgressSuiteRoomList(Long memberId) {
        List<Participant> participantList = participantRepository.findByMemberIdAndStatusNot(memberId, SuiteStatus.END);

        return participantList.stream().map(
                parti -> parti.toResCompletionSuiteRoomDto(parti, parti.getStatus(),
                        participantRepository.countBySuiteRoom_SuiteRoomId(parti.getSuiteRoom().getSuiteRoomId()),
                        participantRepository.findBySuiteRoom_SuiteRoomIdAndIsHost(parti.getSuiteRoom().getSuiteRoomId(), true).get())
        ).collect(Collectors.toList());
    }

    @Override
    public List<ResConditionSuiteRoomDto> getCompletionSuiteRoomList(Long memberId) {
        List<Participant> participantList = participantRepository.findByMemberIdAndStatus(memberId, SuiteStatus.END);

        return participantList.stream().map(
                parti -> parti.toResCompletionSuiteRoomDto(parti, parti.getStatus(),
                        participantRepository.countBySuiteRoom_SuiteRoomId(parti.getSuiteRoom().getSuiteRoomId()),
                        participantRepository.findBySuiteRoom_SuiteRoomIdAndIsHost(parti.getSuiteRoom().getSuiteRoomId(), true).get())
        ).collect(Collectors.toList());
    }

    @Override
    public void validateTitle(String title) {
        suiteRoomRepository.findByTitle(title).ifPresent(
                suiteRoom ->  { throw new CustomException(StatusCode.ALREADY_EXISTS_SUITEROOM); });
    }


    @Override
    @Transactional
    public ResSuiteRoomCreationDto createSuiteRoom(ReqSuiteRoomCreationDto reqSuiteRoomCreationDto, AuthorizerDto authorizerDto) {
        if(!reqSuiteRoomCreationDto.getIsPublic() && reqSuiteRoomCreationDto.getPassword() == null) {
            throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        }
        suiteRoomRepository.findByTitle(reqSuiteRoomCreationDto.getTitle()).ifPresent(
                suiteRoom ->  { throw new CustomException(StatusCode.ALREADY_EXISTS_SUITEROOM); });
        SuiteRoom suiteRoom = reqSuiteRoomCreationDto.toSuiteRoomEntity();

        Participant participant = Participant.builder()
                .authorizerDto(authorizerDto)
                .status(SuiteStatus.PLAIN)
                .isHost(true).build();
        suiteRoom.addParticipant(participant);
        suiteRoomRepository.save(suiteRoom);
        participantRepository.save(participant);
        return ResSuiteRoomCreationDto.builder().suiteRoomId(suiteRoom.getSuiteRoomId()).build();
    }

    @Override
    @Transactional
    public void deleteSuiteRoom(Long suiteRoomId, AuthorizerDto authorizerDto) {
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoomId, authorizerDto.getMemberId(), true).orElseThrow(
                () -> new CustomException(StatusCode.FORBIDDEN));

        if(participant.getStatus().equals(SuiteStatus.START)) throw new CustomException(StatusCode.NOT_DELETE_SUITE_ROOM);

        List<ResPaymentParticipantDto> participants = participantRepository.findBySuiteRoom_SuiteRoomId(suiteRoomId).stream().map(
                p -> {
                    return p.toResPaymentParticipantDto();
                }).collect(Collectors.toList());
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId).orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));


        suiteRoomProducer.suiteRoomTerminateProducer(suiteRoomId, suiteRoom.getTitle(), suiteRoom.getDepositAmount(), participants);
        //suiteRoomRepository.deleteBySuiteRoomId(suiteRoomId);
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

}
