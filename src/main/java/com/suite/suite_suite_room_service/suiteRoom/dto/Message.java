package com.suite.suite_suite_room_service.suiteRoom.dto;

import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Message<T> {
    public static final String DEFAULT_RESPONSE = "Request processed successfully";
    private int statusCode;
    private String message;
    private T data;

    public Message(StatusCode statusCode) {
        this.statusCode = statusCode.getStatusCode();
        this.message = statusCode.getMessage();
        this.data = (T) DEFAULT_RESPONSE;
    }
    public Message(StatusCode statusCode, T data) {
        this.statusCode = statusCode.getStatusCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }


    public MessageAppender messageAppenderCaller( String key, Object value, StatusCode statusCode, T data) {

        MessageAppender messageAppender = new MessageAppender(key, value, statusCode, data);
        return messageAppender;
    }

    @NoArgsConstructor
    @Getter
    public static class MessageAppender<T> extends Message{
        private Map responseData = new HashMap();

        public MessageAppender(String key, Object value , StatusCode statusCode, T data) {
            super(statusCode, data);
            this.responseData.put(key,value);
        }

    }



}
