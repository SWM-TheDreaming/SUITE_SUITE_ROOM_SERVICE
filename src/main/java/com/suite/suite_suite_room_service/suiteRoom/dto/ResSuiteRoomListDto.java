package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class ResSuiteRoomListDto {
    private String title;
    private StudyCategory subject;
    private Timestamp recruitmentDeadline;
    private Timestamp studyDeadline;
    private Integer recruitmentLimit;
    private Integer depositAmount;
    private Boolean isPublic;
    private Boolean isOpen;
    private Long participantCount;
    private boolean isHost;
    private Long markCount;

    @Builder
    public ResSuiteRoomListDto(String title, StudyCategory subject, Timestamp recruitmentDeadline, Timestamp studyDeadline, Integer recruitmentLimit, Integer depositAmount, Boolean isPublic, Boolean isOpen, Long participantCount, boolean isHost, Long markCount) {
        this.title = title;
        this.subject = subject;
        this.recruitmentDeadline = recruitmentDeadline;
        this.studyDeadline = studyDeadline;
        this.recruitmentLimit = recruitmentLimit;
        this.depositAmount = depositAmount;
        this.isPublic = isPublic;
        this.isOpen = isOpen;
        this.participantCount = participantCount;
        this.isHost = isHost;
        this.markCount = markCount;
    }
}
