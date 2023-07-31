package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqUpdateSuiteRoomDto {
    private Long suiteRoomId;
    private String content;
    private String channelLink;


    @Builder
    public ReqUpdateSuiteRoomDto(Long suiteRoomId, String content, String channelLink) {
        this.suiteRoomId = suiteRoomId;
        this.content = content;
        this.channelLink = channelLink;
    }
}
