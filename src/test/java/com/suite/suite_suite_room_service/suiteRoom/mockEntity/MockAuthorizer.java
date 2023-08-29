package com.suite.suite_suite_room_service.suiteRoom.mockEntity;

import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;

public class MockAuthorizer {
    public static AuthorizerDto YH() {
        return AuthorizerDto.builder()
                .memberId(1L)
                .accountStatus("ACTIVIATE")
                .name("반영환")
                .nickName("hawny")
                .email("lopahn@gmail.com")
                .role("ROLE_USER").build();
    }

    public static AuthorizerDto DH() {
        return AuthorizerDto.builder()
                .memberId(2L)
                .accountStatus("ACTIVIATE")
                .name("김대현")
                .nickName("darren")
                .email("zxz4641@gmail.com")
                .role("ROLE_USER").build();
    }

    public static AuthorizerDto JH() {
        return AuthorizerDto.builder()
                .memberId(3L)
                .accountStatus("ACTIVIATE")
                .name("유정협")
                .nickName("MIMO")
                .email("one348@naver.com")
                .role("ROLE_USER").build();
    }

}
