package com.suite.suite_suite_room_service.suiteRoom.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class SuiteRoomDto {
    private String title;
    private String content;
    private String subject;
    private Timestamp recruitmentDeadline;
    private Timestamp studyDeadline;
    private Integer recruitmentLimit;
    private Integer depositAmount;
    private Integer minAttendanceRate;
    private Integer minMissionCompleteRate;
    private Boolean isPublic;
    private Integer password;
    private String channelLink;
    private String studyMethod;
    private String studyLocation;
    private String contractAddress;

    @Builder
    public SuiteRoomDto(String title, String content, String subject, Timestamp recruitmentDeadline, Timestamp studyDeadline, Integer recruitmentLimit, Integer depositAmount, Integer minAttendanceRate, Integer minMissionCompleteRate, Boolean isPublic, Integer password, String channelLink, String studyMethod, String studyLocation, String contractAddress) {
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
        this.password = password;
        this.channelLink = channelLink;
        this.studyMethod = studyMethod;
        this.studyLocation = studyLocation;
        this.contractAddress = contractAddress;
    }
}
