package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ResSuiteRoomListDto {
    private Long suiteRoomId;
    private String title;
    private StudyCategory subject;
    private Timestamp recruitmentDeadline;
    private LocalDateTime createdDate;
    private Integer recruitmentLimit;
    private Integer depositAmount;
    private Boolean isPublic;
    private Boolean isOpen;
    private Long participantCount;
    private String hostNickName;
    private boolean isHost;
    private Long markCount;

    @Builder
    public ResSuiteRoomListDto(Long suiteRoomId, String title, StudyCategory subject, Timestamp recruitmentDeadline, LocalDateTime createdDate, Integer recruitmentLimit, Integer depositAmount, Boolean isPublic, Boolean isOpen, Long participantCount, String hostNickName, boolean isHost, Long markCount) {
        this.suiteRoomId = suiteRoomId;
        this.title = title;
        this.subject = subject;
        this.recruitmentDeadline = recruitmentDeadline;
        this.createdDate = createdDate;
        this.recruitmentLimit = recruitmentLimit;
        this.depositAmount = depositAmount;
        this.isPublic = isPublic;
        this.isOpen = isOpen;
        this.participantCount = participantCount;
        this.hostNickName = hostNickName;
        this.isHost = isHost;
        this.markCount = markCount;
    }
}
