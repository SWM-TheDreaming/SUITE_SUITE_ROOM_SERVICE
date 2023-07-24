package com.suite.suite_suite_room_service.suiteRoom.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    StatusCode authCode;
}