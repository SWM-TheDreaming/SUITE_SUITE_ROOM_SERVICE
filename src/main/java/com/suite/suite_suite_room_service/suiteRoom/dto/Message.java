package com.suite.suite_suite_room_service.suiteRoom.dto;

import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Message {
    public static final String DEFAULT_RESPONSE = "Request processed successfully";
    private int statusCode;
    private String message;
    private Object data;

    public Message(StatusCode statusCode, Object data) {
        this.statusCode = statusCode.getCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }

    public Message(StatusCode statusCode) {
        this.statusCode = statusCode.getCode();
        this.message = statusCode.getMessage();
        this.data = DEFAULT_RESPONSE;
    }
}
