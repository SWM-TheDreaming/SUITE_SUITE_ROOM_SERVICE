package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckInInfoDto {
    private Long suiteRoomId;
    private Long memberId;

    public CheckInInfoDto(Long suiteRoomId, Long memberId) {
        this.suiteRoomId = suiteRoomId;
        this.memberId = memberId;
    }


}
