package com.suite.suite_suite_room_service.suiteRoom.dto;

import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Message<T> {
    public static final String DEFAULT_RESPONSE = "Request processed successfully";
    private int statusCode;
    private String message;
    private T data;

    public Message(StatusCode statusCode, T data) {
        this.statusCode = statusCode.getStatusCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }

    public Message(StatusCode statusCode) {
        this.statusCode = statusCode.getStatusCode();
        this.message = statusCode.getMessage();
        this.data = (T) DEFAULT_RESPONSE;
    }

    public T getData() {
        return this.data;
    }

}
