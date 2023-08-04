package com.suite.suite_suite_room_service.suiteRoom.mockEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MockKafkaCardDto {
    private Long suiteRoomId;
    private Long participantId;

    @Builder
    public MockKafkaCardDto(Long suiteRoomId, Long participantId) {
        this.suiteRoomId = suiteRoomId;
        this.participantId = participantId;
    }
}
