package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class ResSuiteRoomInfoDto {
    private Long suiteRoomId;
    private Boolean isStart;
    private Timestamp studyStartDate;
    private Timestamp studyDeadline;
    private String title;

    @Builder
    public ResSuiteRoomInfoDto(Long suiteRoomId, Boolean isStart, Timestamp studyStartDate, Timestamp studyDeadline, String title) {
        this.suiteRoomId = suiteRoomId;
        this.isStart = isStart;
        this.studyStartDate = studyStartDate;
        this.studyDeadline = studyDeadline;
        this.title = title;
    }
}
