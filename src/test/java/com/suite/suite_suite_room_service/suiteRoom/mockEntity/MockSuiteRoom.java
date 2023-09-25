package com.suite.suite_suite_room_service.suiteRoom.mockEntity;

import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomCreationDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.StudyCategory;
import com.suite.suite_suite_room_service.suiteRoom.dto.StudyType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MockSuiteRoom {
    public static ReqSuiteRoomCreationDto getMockSuiteRoom(String title, boolean isPublic, boolean isOpen) {
        return ReqSuiteRoomCreationDto.builder()
                .title(title)
                .content("Test Content")
                .subject(StudyCategory.TOEIC)
                .recruitmentDeadline(getTimeStamp("2023-08-23 12:57:23"))
                .studyDeadline(getTimeStamp("2023-10-23 12:57:23"))
                .depositAmount(20000)
                .minAttendanceRate(80)
                .minMissionCompleteRate(80)
                .isPublic(isPublic)
                .password(isPublic ? null : 3249)
                .isOpen(isOpen)
                .channelLink("https://open.kakao.com/o/gshpRksf")
                .studyMethod(StudyType.ONLINE).build();
    }


    private static Timestamp getTimeStamp(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2023-08-23 12:57:23", formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));
        return Timestamp.from(zonedDateTime.toInstant());
    }
}
