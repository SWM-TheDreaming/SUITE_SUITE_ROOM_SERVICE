package com.suite.suite_suite_room_service.suiteRoom.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");
    private final String value;

    @JsonCreator
    public static String from(String s) {
        try {
            return Role.valueOf(s.toUpperCase()).getValue();
        } catch (IllegalArgumentException e) {
            throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        }
    }
}