package com.suite.suite_suite_room_service.suiteRoom.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorizerDto {

    private Long memberId;
    private String email;
    private String name;
    private String nickName;
    private String accountStatus;
    private String role;


    @Builder
    public AuthorizerDto(Long memberId, String email, String name, String nickName, String accountStatus, String role) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.accountStatus = accountStatus;
        this.role = role;
    }

    @Getter
    @AllArgsConstructor
    public enum ClaimName {
        ID("ID"),
        NAME("NAME"),
        NICKNAME("NICKNAME"),
        ACCOUNTSTATUS("ACCOUNTSTATUS"),
        ROLE("ROLE");
        private final String value;
    }

    public String toJSONString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
