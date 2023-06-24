package com.suite.suite_user_service.member.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UsernameOrPasswordNotFoundException (400, "아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    PasswordNotFoundException (400, "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    ForbiddenException(403, "해당 요청에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZEDException (400, "로그인 후 이용가능합니다.", HttpStatus.UNAUTHORIZED),
    ExpiredJwtException(400, "기존 토큰이 만료되었습니다. 해당 토큰을 가지고 /token/refresh 링크로 이동 후 토큰을 재발급 받으세요.", HttpStatus.UNAUTHORIZED),
    ReLogin(400, "모든 토큰이 만료되었습니다. 다시 로그인해주세요.", HttpStatus.UNAUTHORIZED),
    FAILEDSIGNUP(400, "회원가입에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATEEMAIL(400, "중복된 이메일입니다.", HttpStatus.BAD_REQUEST),
    REFISTEREDEMAIL(400, "등록된 회원입니다.", HttpStatus.BAD_REQUEST),
    NOTFOUND(404, "일치하는 정보가 없습니다.", HttpStatus.NOT_FOUND),
    ;
    @Getter
    private int code;
    @Getter
    private String message;
    @Getter
    private HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String toString() {
        return "{" +
                "\"code\" : " + "\""+code+"\"" +
                "\"status\" : " + "\""+status+"\"" +
                "\"message\" : " + "\""+message+"\"" +
                "}";
    }
}
