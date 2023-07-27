package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import org.assertj.core.api.Assertions;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SuiteRoomServiceTest {

    @InjectMocks private SuiteRoomServiceImpl suiteRoomServiceImpl;
    @Mock private SuiteRoomRepository suiteRoomRepository;

    @Test
    @DisplayName("스위트룸 생성")
    void createSuiteRoom() {
        //given
        SuiteRoom suiteRoom = getMockSuiteRoom("test1");

        //when
        when(suiteRoomRepository.save(suiteRoom)).thenReturn(suiteRoom);

        //then
        SuiteRoom result = suiteRoomServiceImpl.createSuiteRoom(suiteRoom);
        verify(suiteRoomRepository).save(suiteRoom);

        assertThat(result.getTitle()).isEqualTo("Test Title");
    }

    @Test
    @DisplayName("스위트룸 그룹 목록 확인")
    void listUpAllSuiteRooms() {
        //given
        SuiteRoom suiteRoom1 = getMockSuiteRoom("test1");
        SuiteRoom suiteRoom2 = getMockSuiteRoom("test2");

        List<SuiteRoom> expectedList = new ArrayList<>();
        expectedList.add(suiteRoom1);
        expectedList.add(suiteRoom2);

        suiteRoomRepository.save(suiteRoom1);
        suiteRoomRepository.save(suiteRoom2);

        //when
        when(suiteRoomRepository.findAll()).thenReturn(expectedList);

        //then
        List<SuiteRoomDto> result = suiteRoomServiceImpl.getAllSuiteRooms();
        assertThat(result.toArray().length).isEqualTo(2);
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