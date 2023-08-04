package com.suite.suite_suite_room_service.suiteRoom.mockEntity;

import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;

public class MockAuthorizer {
    public static AuthorizerDto getMockAuthorizer(String name, Long memberId) {
        return AuthorizerDto.builder()
                .memberId(memberId)
                .accountStatus("ACTIVIATE")
                .name(name)
                .nickName("Darren")
                .email("zxz4641@gmail.com")
                .role("ROLE_USER").build();
    }
}
