package com.suite.suite_suite_room_service.suiteRoom.mockEntity;

import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MockParticipant {
    public static Participant getMockParticipant(boolean ishost, AuthorizerDto authorizerDto) {
        return Participant.builder()
                .authorizerDto(authorizerDto)
                .status(SuiteStatus.PLAIN)
                .isHost(ishost).build();
    }

    public static AuthorizerDto getMockAuthorizer(String memberId) {
        return AuthorizerDto.builder()
                .memberId(Long.parseLong(memberId))
                .accountStatus("ACTIVIATE")
                .name("김대현")
                .nickName("Darren")
                .email("zxz4641@gmail.com")
                .role("ROLE_USER").build();
    }

}
