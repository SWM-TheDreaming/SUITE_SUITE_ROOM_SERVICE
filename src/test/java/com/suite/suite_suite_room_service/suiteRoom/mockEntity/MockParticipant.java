package com.suite.suite_suite_room_service.suiteRoom.mockEntity;

import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;



public class MockParticipant {
    public static Participant getMockParticipant(boolean ishost, AuthorizerDto authorizerDto) {
        return Participant.builder()
                .authorizerDto(authorizerDto)
                .status(SuiteStatus.READY)
                .isHost(ishost).build();
    }


}
