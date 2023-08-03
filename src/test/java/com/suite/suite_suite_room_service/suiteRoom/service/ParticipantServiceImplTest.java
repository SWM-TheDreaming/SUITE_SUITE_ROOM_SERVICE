package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockAuthorizer;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockParticipant;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class ParticipantServiceImplTest {

    @Autowired private ParticipantRepository participantRepository;
    @Autowired private SuiteRoomRepository suiteRoomRepository;

    private final SuiteRoom suiteRoom = MockSuiteRoom.getMockSuiteRoom("test", true).toSuiteRoomEntity();
    private final Participant participantHost = MockParticipant.getMockParticipant(true, MockParticipant.getMockAuthorizer());

    @BeforeEach
    public void setUp() {
        suiteRoom.addParticipant(participantHost);
        suiteRoomRepository.save(suiteRoom);
        participantRepository.save(participantHost);
    }

    @Test
    @DisplayName("스위트룸 참가하기")
    public void addParticipant() {
        //given
        Long targetSuiteRoomId = 1L;
        AuthorizerDto authorizerDto = MockAuthorizer.getMockAuthorizer("kim1");
        //when
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(targetSuiteRoomId).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.NOT_FOUND); } )
        );
        participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(targetSuiteRoomId, authorizerDto.getMemberId()).ifPresent(
                participant -> { assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.ALREADY_EXISTS_PARTICIPANT); } );}
        );
        Participant newParticipant = MockParticipant.getMockParticipant(false, authorizerDto);
        suiteRoom.addParticipant(newParticipant);
        participantRepository.save(newParticipant);
        List<Participant> result = participantRepository.findBySuiteRoom_SuiteRoomId(targetSuiteRoomId);
        //then
        Assertions.assertAll(
                () -> assertThat(result.size()).isEqualTo(2)
        );
    }


    @Test
    @DisplayName("스위트룸 참가 취소")
    @Transactional
    public void cancelParticipant() {
        //given
        Long targetSuiteRoomId = 1L;
        AuthorizerDto authorizerDto = MockAuthorizer.getMockAuthorizer("kim1");
        //when
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(targetSuiteRoomId, authorizerDto.getMemberId()).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.FAILED_REQUEST); })
         );

        if(participant.getStatus().equals(SuiteStatus.READY))
            System.out.println("kafka");
        participantRepository.deleteBySuiteRoom_SuiteRoomIdAndMemberId(targetSuiteRoomId, authorizerDto.getMemberId());
        List<Participant> result = participantRepository.findBySuiteRoom_SuiteRoomId(targetSuiteRoomId);
        //then
        Assertions.assertAll(
                () -> assertThat(result.size()).isEqualTo(1)
        );
    }


}