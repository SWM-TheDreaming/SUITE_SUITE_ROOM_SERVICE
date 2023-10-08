package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ResBeforeStudyDashboard {
    private Long suiteRoomId;
    private Boolean isStart;
    private Timestamp studyStartDate;
    private Timestamp studyDeadline;
    private String title;
    private List<ParticipantDto> participantDtoList;

    @Builder
    public ResBeforeStudyDashboard(Long suiteRoomId, Boolean isStart, Timestamp studyStartDate, Timestamp studyDeadline, String title, List<ParticipantDto> participantDtoList) {
        this.suiteRoomId = suiteRoomId;
        this.isStart = isStart;
        this.studyStartDate = studyStartDate;
        this.studyDeadline = studyDeadline;
        this.title = title;
        this.participantDtoList = participantDtoList;
    }
}
