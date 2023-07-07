package com.suite.suite_user_service.member.dto;

import com.suite.suite_user_service.member.handler.StatusCode;
import lombok.Data;

@Data
public class Message {
    private int statusCode;
    private String message;
    private Object data;

    public Message(StatusCode statusCode, Object data) {
        this.statusCode = statusCode.getCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }
}
