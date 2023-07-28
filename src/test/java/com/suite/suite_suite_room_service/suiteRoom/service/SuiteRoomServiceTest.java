package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.StudyCategory;
import com.suite.suite_suite_room_service.suiteRoom.dto.StudyType;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SuiteRoomServiceTest {

    @Autowired private SuiteRoomRepository suiteRoomRepository;
    @Autowired private ParticipantRepository participantRepository;

    @Test
    @DisplayName("스위트룸 생성")
    public void createSuiteRoom() {
        //given
        SuiteRoom suiteRoom = getMockSuiteRoom(true).toSuiteRoomEntity();
        Participant participant = getMockParticipant(true, getMockAuthorizer());
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
        SuiteRoom suiteRoom = getMockSuiteRoom(false).toSuiteRoomEntity();
        Participant participant = getMockParticipant(true, getMockAuthorizer());
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


    private AuthorizerDto getMockAuthorizer() {
        return AuthorizerDto.builder()
                .memberId(Long.parseLong("1"))
                .accountStatus("ACTIVIATE")
                .name("김대현")
                .nickName("Darren")
                .email("zxz4641@gmail.com")
                .role("ROLE_USER").build();
    }

    private Participant getMockParticipant(boolean ishost, AuthorizerDto authorizerDto) {
        return Participant.builder()
                .authorizerDto(authorizerDto)
                .status(SuiteStatus.PLAIN)
                .isHost(ishost).build();
    }

    private ReqSuiteRoomDto getMockSuiteRoom(boolean isPublic) {
        return ReqSuiteRoomDto.builder()
                .title("Test Title")
                .content("Test Content")
                .subject(StudyCategory.TOEIC)
                .recruitmentDeadline(getTimeStamp("2023-08-23 12:57:23"))
                .studyDeadline(getTimeStamp("2023-10-23 12:57:23"))
                .depositAmount(20000)
                .minAttendanceRate(80)
                .minMissionCompleteRate(80)
                .isPublic(isPublic)
                .password(isPublic ? null : 3249)
                .channelLink("https://open.kakao.com/o/gshpRksf")
                .studyMethod(StudyType.ONLINE).build();
    }

    private Timestamp getTimeStamp(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2023-08-23 12:57:23", formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));
        return Timestamp.from(zonedDateTime.toInstant());
    }

}