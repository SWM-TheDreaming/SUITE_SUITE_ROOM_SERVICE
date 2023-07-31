package com.suite.suite_suite_room_service.suiteRoom.mockEntity;

import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;

public class MockAuthorizer {
    public static AuthorizerDto getMockAuthorizer(String name) {
        return AuthorizerDto.builder()
                .memberId(Long.parseLong("1"))
                .accountStatus("ACTIVIATE")
                .name(name)
                .nickName("Darren")
                .email("zxz4641@gmail.com")
                .role("ROLE_USER").build();
    }
}
