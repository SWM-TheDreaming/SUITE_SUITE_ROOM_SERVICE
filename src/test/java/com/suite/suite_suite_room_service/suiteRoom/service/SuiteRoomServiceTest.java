package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SuiteRoomServiceTest {

    @InjectMocks private SuiteRoomServiceImpl suiteRoomServiceImpl;
    @Mock private SuiteRoomRepository suiteRoomRepository;

    @Test
    @DisplayName("스위트룸 생성")
    public void createSuiteRoom() {
        //given
        SuiteRoom suiteRoom = getMockSuiteRoom();

        //when
        SuiteRoom result = suiteRoomRepository.save(suiteRoom);

        //then
        assertThat(result.getTitle()).isEqualTo("Test Title");
    }

    public SuiteRoom getMockSuiteRoom() {
        return SuiteRoom.builder()
                .title("Test Title")
                .content("Test Content")
                .subject("")
                .recruitmentDeadline(getTimeStamp("2023-08-23 12:57:23"))
                .studyDeadline(getTimeStamp("2023-10-23 12:57:23"))
                .depositAmount(20000)
                .minAttendanceRate(80)
                .minMissionCompleteRate(80)
                .isPublic(true)
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