package com.suite.suite_suite_room_service.suiteRoom.mockEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MockCheckInDto {
    private Long suiteRoomId;
    private Long memberId;

    public MockCheckInDto(Long suiteRoomId, Long memberId) {
        this.suiteRoomId = suiteRoomId;
        this.memberId = memberId;
    }
}
