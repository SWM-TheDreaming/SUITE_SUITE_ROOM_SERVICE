package com.suite.suite_suite_room_service.suiteRoom.dto;


import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Getter
@NoArgsConstructor
public class ReqSuiteRoomDto {
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

    @Builder
    public ReqSuiteRoomDto(String title, String content, StudyCategory subject, Timestamp recruitmentDeadline, Timestamp studyDeadline, Integer recruitmentLimit, Integer depositAmount, Integer minAttendanceRate, Integer minMissionCompleteRate, Boolean isPublic, Integer password, String channelLink, StudyType studyMethod, String contractAddress) {
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
        this.contractAddress = contractAddress;
    }

    public SuiteRoom toSuiteRoomEntity() {
        return SuiteRoom.builder()
                .title(title)
                .content(content)
                .subject(subject)
                .recruitmentDeadline(recruitmentDeadline)
                .studyDeadline(studyDeadline)
                .recruitmentLimit(recruitmentLimit)
                .depositAmount(depositAmount)
                .minAttendanceRate(minAttendanceRate)
                .minMissionCompleteRate(minMissionCompleteRate)
                .isPublic(isPublic)
                .password(password)
                .channelLink(channelLink)
                .studyMethod(studyMethod)
                .contractAddress(contractAddress).build();
    }
}
