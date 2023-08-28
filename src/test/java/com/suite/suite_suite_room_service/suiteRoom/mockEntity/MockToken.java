package com.suite.suite_suite_room_service.suiteRoom.mockEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String key;
}
