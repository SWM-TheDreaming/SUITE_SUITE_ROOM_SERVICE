package com.suite.suite_suite_room_service.suiteRoom.service;


import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockParticipant;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import org.junit.jupiter.api.Assertions;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class SuiteRoomServiceTest {

    @Autowired private SuiteRoomRepository suiteRoomRepository;
    @Autowired private ParticipantRepository participantRepository;

    @Test
    @DisplayName("스위트룸 생성")
    public void createSuiteRoom() {
        //given
        SuiteRoom suiteRoom = MockSuiteRoom.getMockSuiteRoom("test",true).toSuiteRoomEntity();
        Participant participant = MockParticipant.getMockParticipant(true, MockParticipant.getMockAuthorizer());
        //when
        suiteRoom.addParticipant(participant);
        SuiteRoom result_suiteRoom = suiteRoomRepository.save(suiteRoom);
        Participant result_participant = participantRepository.save(participant);
        //then
        Assertions.assertAll(
                () -> assertThat(result_suiteRoom.getTitle()).isEqualTo(suiteRoom.getTitle()),
                () -> assertThat(result_suiteRoom).isEqualTo(result_participant.getSuiteRoom())
                );
    }

    @Test
    @DisplayName("스위트룸 비공개생성")
    public void createSecretSuiteRoom() {
        //given
        SuiteRoom suiteRoom = MockSuiteRoom.getMockSuiteRoom("test",false).toSuiteRoomEntity();
        Participant participant = MockParticipant.getMockParticipant(true, MockParticipant.getMockAuthorizer());
        //when
        suiteRoom.addParticipant(participant);
        SuiteRoom result_suiteRoom = suiteRoomRepository.save(suiteRoom);
        Participant result_participant = participantRepository.save(participant);

        //then
        Assertions.assertAll(
                () -> assertThat(result_suiteRoom.getIsPublic()).isEqualTo(suiteRoom.getIsPublic()),
                () -> assertThat(result_suiteRoom.getPassword()).isEqualTo(suiteRoom.getPassword()),
                () -> assertThat(result_suiteRoom).isEqualTo(result_participant.getSuiteRoom())
        );

    }

    @Test
    @DisplayName("스위트룸 수정")
    public void updateSuiteRoom() {
        //given
        ReqSuiteRoomDto suiteRoomDto = MockSuiteRoom.getMockSuiteRoom("test", true);
        SuiteRoom suiteRoom = suiteRoomDto.toSuiteRoomEntity();
        Participant participant = MockParticipant.getMockParticipant(true, MockParticipant.getMockAuthorizer());
        suiteRoom.addParticipant(participant);
        suiteRoomRepository.save(suiteRoom);
        //when
        Optional<Participant> member = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoom.getSuiteRoomId(), participant.getMemberId(), true);
        if(member.isEmpty())
            assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.FORBIDDEN); });

        ReqSuiteRoomDto updatedSuiteRoomDto = ReqSuiteRoomDto.builder()
                .content("updated content")
                .channelLink("http://www.naver.com").build();

        suiteRoom.updateSuiteRoom(updatedSuiteRoomDto);
        suiteRoomRepository.save(suiteRoom);
        //then
        Assertions.assertAll(
                () -> assertThat(suiteRoom.getContent()).isEqualTo(updatedSuiteRoomDto.getContent()),
                () -> assertThat(suiteRoom.getChannelLink()).isEqualTo(updatedSuiteRoomDto.getChannelLink())
        );
    }

    @Test
    @DisplayName("스위트룸 그룹 목록 확인")
    void getAllSuiteRooms() {
        //given
        SuiteRoom suiteRoom1 = MockSuiteRoom.getMockSuiteRoom("test",true).toSuiteRoomEntity();
        SuiteRoom suiteRoom2 = MockSuiteRoom.getMockSuiteRoom("test2",true).toSuiteRoomEntity();

        List<SuiteRoom> expectedList = new ArrayList<>();
        expectedList.add(suiteRoom1);
        expectedList.add(suiteRoom2);

        suiteRoomRepository.save(suiteRoom1);
        suiteRoomRepository.save(suiteRoom2);

        //when
        List<SuiteRoom> suiteRooms = suiteRoomRepository.findAll();
        suiteRooms.stream().map(suiteRoom -> suiteRoom.toResSuiteRoomDto()).collect(Collectors.toList());

        //then
        Assertions.assertAll(
                ()-> assertThat(suiteRooms.toArray().length).isEqualTo(2)
        );

    }

}