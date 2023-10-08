package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParticipantDto {
    private Long memberId;
    private String name;
    private String nickname;
    private Boolean isHost;

    @Builder
    public ParticipantDto(Long memberId, String name, String nickname, Boolean isHost) {
        this.memberId = memberId;
        this.name = name;
        this.nickname = nickname;
        this.isHost = isHost;
    }
}
