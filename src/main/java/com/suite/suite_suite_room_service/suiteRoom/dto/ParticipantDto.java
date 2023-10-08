package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParticipantDto {
    private Long memberId;
    private String name;
    private String nickName;
    private Boolean isHost;
    private Double attendanceRate;
    private Double missionRate;


    @Builder
    public ParticipantDto(Long memberId, String name, String nickName, Boolean isHost, Double attendanceRate, Double missionRate) {
        this.memberId = memberId;
        this.name = name;
        this.nickName = nickName;
        this.isHost = isHost;
        this.attendanceRate = attendanceRate;
        this.missionRate = missionRate;
    }
}
