package com.suite.suite_suite_room_service.suiteRoom.repository;

import com.suite.suite_suite_room_service.suiteRoom.dto.StudyCategory;
import com.suite.suite_suite_room_service.suiteRoom.dto.StudyType;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class ParticipantRepositoryTest {

    @Autowired private ParticipantRepository participantRepository;
    @Autowired private SuiteRoomRepository suiteRoomRepository;
    @Test
    @DisplayName("스위트룸 참가")
    public void ParticipateSuiteRoom() {
        //given
        SuiteRoom suiteRoom = getMockSuiteRoom(true);
        suiteRoomRepository.save(suiteRoom);

        Participant participant = getMockParticipant(true, getMockAuthorizer());
        //when
        Participant result = participantRepository.save(participant);
        //then
        assertThat(result.getMemberId()).isEqualTo(participant.getMemberId());
        assertThat(result.getSuiteRoom()).isEqualTo(participant.getSuiteRoom());
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

    private SuiteRoom getMockSuiteRoom(boolean secret) {
        return SuiteRoom.builder()
                .title("Test Title")
                .content("Test Content")
                .subject(StudyCategory.TOEIC)
                .recruitmentDeadline(getTimeStamp("2023-08-23 12:57:23"))
                .studyDeadline(getTimeStamp("2023-10-23 12:57:23"))
                .depositAmount(20000)
                .minAttendanceRate(80)
                .minMissionCompleteRate(80)
                .isPublic(secret)
                .password(secret ? null : 3249)
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