package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class ResSuiteRoomDto {
    private Long suiteRoomId;
    private String title;
    private String content;
    private StudyCategory subject;
    private Timestamp recruitmentDeadline;
    private Timestamp studyDeadline;
    private Integer recruitmentLimit;
    private Integer depositAmount;
    private Integer minAttendanceRate;
    private Integer minMissionCompleteRate;
    private Boolean isPublic;
    private Integer password;
    private Boolean isOpen;
    private String channelLink;
    private StudyType studyMethod;
    private String contractAddress;
    private Long participantCount;
    private boolean isParticipant;
    private boolean isHost;
    private Long markCount;
    private Timestamp createdAt;
    private boolean isMark;
    @Builder
    public ResSuiteRoomDto(Long suiteRoomId, String title, String content, StudyCategory subject, Timestamp recruitmentDeadline, Timestamp studyDeadline, Integer recruitmentLimit, Integer depositAmount, Integer minAttendanceRate, Integer minMissionCompleteRate, Boolean isPublic, Boolean isOpen , String channelLink, StudyType studyMethod, String contractAddress, Long participantCount, boolean isParticipant, boolean isHost, Long markCount, Timestamp createdAt, boolean isMark) {
        this.suiteRoomId = suiteRoomId;
        this.title = title;
        this.content = content;
        this.subject = subject;
        this.recruitmentDeadline = recruitmentDeadline;
        this.studyDeadline = studyDeadline;
        this.recruitmentLimit = recruitmentLimit;
        this.depositAmount = depositAmount;
        this.minAttendanceRate = minAttendanceRate;
        this.minMissionCompleteRate = minMissionCompleteRate;
        this.isPublic = isPublic;
        this.isOpen = isOpen;
        this.channelLink = channelLink;
        this.studyMethod = studyMethod;
        this.contractAddress = contractAddress;
        this.participantCount = participantCount;
        this.isParticipant = isParticipant;
        this.isHost = isHost;
        this.markCount = markCount;
        this.isMark = isMark;
        this.createdAt = createdAt;
    }
}