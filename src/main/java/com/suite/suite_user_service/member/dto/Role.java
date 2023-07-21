package com.suite.suite_user_service.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");
    private final String value;

    @JsonCreator
    public static Role from(String s) {
        try {
            return Role.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        }
    }
}