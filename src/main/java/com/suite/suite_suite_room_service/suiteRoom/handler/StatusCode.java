package com.suite.suite_suite_room_service.suiteRoom.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum StatusCode {
    OK(200, "OK", HttpStatus.OK),

    DISABLED_ACCOUNT(403, "삭제된 계정입니다.", HttpStatus.FORBIDDEN),
    DORMANT_ACCOUNT(423, "이 계정은 휴먼 계정입니다.", HttpStatus.LOCKED),
    ALREADY_EXISTS_SUITEROOM(400, "이미 존재하는 스위트룸입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXISTS_PARTICIPANT(400, "이미 참여중 입니다.", HttpStatus.BAD_REQUEST),
    CAN_NOT_CALCEL_SUITEROOM(400, "스위트룸 참가 취소를 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_DELETE_SUITE_ROOM(400, "시작된 스터디는 삭제가 불가능합니다.", HttpStatus.BAD_REQUEST),
    USERNAME_OR_PASSWORD_NOT_FOUND (400, "아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    SUITE_ROOM_NOT_FOUND (400, "존재하지 않는 스위트룸 아이디입니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_FOUND (400, "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN(403, "해당 요청에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    PLAIN_USER_EXIST(405, "포인트 결제중인 유저가 있습니다. 잠시 후 다시 시도하세요.", HttpStatus.METHOD_NOT_ALLOWED),
    IS_NOT_OPEN(400, "방장이 체크인을 완료하지 않은 스위트룸입니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED (400, "로그인 후 이용가능합니다.", HttpStatus.UNAUTHORIZED),
    FAILED_PAY(400, "포인트가 부족합니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_JWT(400, "기존 토큰이 만료되었습니다. 해당 토큰을 가지고 /token/refresh 링크로 이동 후 토큰을 재발급 받으세요.", HttpStatus.UNAUTHORIZED),
    RE_LOGIN(400, "모든 토큰이 만료되었습니다. 다시 로그인해주세요.", HttpStatus.UNAUTHORIZED),
    FAILED_SIGNUP(400, "회원가입에 실패하였습니다.", HttpStatus.BAD_REQUEST),

    REGISTERED_EMAIL(400, "등록된 회원입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "일치하는 정보가 없습니다.", HttpStatus.NOT_FOUND),
    FAILED_REQUEST(400, "요청에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    INVALID_DATA_FORMAT(400, "형식이 맞지 않습니다.", HttpStatus.BAD_REQUEST),
    ;
    @Getter
    private int statusCode;
    @Getter
    private String message;
    @Getter
    private HttpStatus status;

    StatusCode(int statusCode, String message, HttpStatus status) {
        this.statusCode = statusCode;
        this.message = message;
        this.status = status;
    }

    public String toString() {
        return "{" +
                "\"code\" : " + "\""+ statusCode +"\"" +
                "\"status\" : " + "\""+status+"\"" +
                "\"message\" : " + "\""+message+"\"" +
                "}";
    }
}
