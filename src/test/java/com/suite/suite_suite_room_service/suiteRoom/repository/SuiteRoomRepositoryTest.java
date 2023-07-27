package com.suite.suite_suite_room_service.suiteRoom.repository;

import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class SuiteRoomRepositoryTest {
    @Autowired
    private SuiteRoomRepository suiteRoomRepository;
    
    @Test
    @DisplayName("스위트룸 생성")
    void saveSuiteRoom() {
        //given
        SuiteRoom suiteRoom = getMockSuiteRoom("test");

        //when
        suiteRoomRepository.save(suiteRoom);
        //then
        assertEquals(suiteRoom.getTitle(), suiteRoom.getTitle());
    }

    @Test
    @DisplayName("스위트룸 그룹 목록 확인")
    void getAllSuiteRooms() {
        //given
        SuiteRoom suiteRoom = getMockSuiteRoom("test1");
        SuiteRoom suiteRoom2 = getMockSuiteRoom("test2");
        SuiteRoom suiteRoom3 = getMockSuiteRoom("test3");
        //when
        suiteRoomRepository.save(suiteRoom);
        suiteRoomRepository.save(suiteRoom2);
        suiteRoomRepository.save(suiteRoom3);

        List<SuiteRoom> result = suiteRoomRepository.findAll();
        //then
        assertEquals(result.toArray().length, 3);
    }

    SuiteRoom getMockSuiteRoom(String title) {
        return SuiteRoom.builder()
                .title(title)
                .content("Test Content")
                .subject("")
                .recruitmentDeadline(getTimeStamp("2023-08-23 12:57:23"))
                .studyDeadline(getTimeStamp("2023-10-23 12:57:23"))
                .depositAmount(20000)
                .minAttendanceRate(80)
                .minMissionCompleteRate(80)
                .isPublic(true)
                .channelLink("https://open.kakao.com/o/gshpRksf")
                .studyMethod("ONLINE")
                .studyLocation("SEOUL").build();
    }

    private Timestamp getTimeStamp(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2023-08-23 12:57:23", formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));
        return Timestamp.from(zonedDateTime.toInstant());
    }

}