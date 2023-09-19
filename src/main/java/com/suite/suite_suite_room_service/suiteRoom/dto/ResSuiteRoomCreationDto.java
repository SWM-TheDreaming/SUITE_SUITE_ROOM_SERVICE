package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResSuiteRoomCreationDto {
    private long suiteRoomId;

    @Builder
    public ResSuiteRoomCreationDto(long suiteRoomId) {
        this.suiteRoomId = suiteRoomId;
    }
}
