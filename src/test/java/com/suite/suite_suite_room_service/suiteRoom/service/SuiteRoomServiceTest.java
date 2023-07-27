package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SuiteRoomServiceTest {

    @InjectMocks
    private SuiteRoomServiceImpl suiteRoomService;

    @Mock
    private SuiteRoomRepository suiteRoomRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("공개방 생성 테스트")
    @Test
    public void testCreateSuiteRoomWithoutPassword() {


        SuiteRoom suiteRoom = SuiteRoom.builder()
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
                .studyMethod("ONLINE")
                .studyLocation("SEOUL").build();

        //suiteRoomRepository.save() 호출 시 SuiteRoom 객체를 반환 하도록 설정
        when(suiteRoomRepository.save(suiteRoom)).thenReturn(suiteRoom);

        SuiteRoom createdSuiteRoom = suiteRoomService.createSuiteRoom(suiteRoom);

        verify(suiteRoomRepository).save(suiteRoom);
        System.out.println(createdSuiteRoom.getSuiteRoomId());
        //assertEquals(createdSuiteRoom.getSuiteRoomId(), 1);
        assertEquals(createdSuiteRoom.getTitle(), suiteRoom.getTitle());
    }

    private Timestamp getTimeStamp(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2023-08-23 12:57:23", formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));
        return Timestamp.from(zonedDateTime.toInstant());
    }

}