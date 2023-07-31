package com.suite.suite_suite_room_service.suiteRoom.dto;

import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class ResSuiteRoomDto {
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
    private String channelLink;
    private StudyType studyMethod;
    private String contractAddress;
    private Long participantCount;
    private boolean isHost;
    @Builder
    public ResSuiteRoomDto(String title, String content, StudyCategory subject, Timestamp recruitmentDeadline, Timestamp studyDeadline, Integer recruitmentLimit, Integer depositAmount, Integer minAttendanceRate, Integer minMissionCompleteRate, Boolean isPublic, String channelLink, StudyType studyMethod, String contractAddress, Long participantCount, boolean isHost) {
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
        this.channelLink = channelLink;
        this.studyMethod = studyMethod;
        this.contractAddress = contractAddress;
        this.participantCount = participantCount;
        this.isHost = isHost;
    }
}
