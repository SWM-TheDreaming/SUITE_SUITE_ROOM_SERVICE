package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class ResConditionSuiteRoomDto {
    private Long suiteRoomId;
    private String title;
    private StudyCategory subject;
    private SuiteStatus suiteStatus;
    private Timestamp studyStartDate;
    private Timestamp studyDeadline;
    private Integer depositAmount;
    private Long participantCount;
    private String hostNickName;

    @Builder
    public ResConditionSuiteRoomDto(Long suiteRoomId, String title, StudyCategory subject, SuiteStatus suiteStatus, Timestamp studyStartDate, Timestamp studyDeadline, Integer depositAmount, Long participantCount, String hostNickName) {
        this.suiteRoomId = suiteRoomId;
        this.title = title;
        this.subject = subject;
        this.suiteStatus = suiteStatus;
        this.studyStartDate = studyStartDate;
        this.studyDeadline = studyDeadline;
        this.depositAmount = depositAmount;
        this.participantCount = participantCount;
        this.hostNickName = hostNickName;
    }
}
