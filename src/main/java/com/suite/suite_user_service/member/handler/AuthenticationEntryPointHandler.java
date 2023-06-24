package com.suite.suite_user_service.member.handler;

import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequestMapping
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        ErrorCode errorCode;

        if(exception == null) {
            errorCode = ErrorCode.UNAUTHORIZEDException;
            setResponse(response, errorCode);
            return;
        }

        if(exception.equals("NullPointerException")) {
            errorCode = ErrorCode.UNAUTHORIZEDException;
            setResponse(response, errorCode);
            return;
        }


        if(exception.equals("PasswordNotFoundException")) {
            errorCode = ErrorCode.PasswordNotFoundException;
            setResponse(response, errorCode);
            return;
        }

        if(exception.equals("ForbiddenException")) {
            errorCode = ErrorCode.ForbiddenException;
            setResponse(response, errorCode);
            return;
        }

        //토큰이 만료된 경우
        if(exception.equals("ExpiredJwtException")) {
            errorCode = ErrorCode.ExpiredJwtException;
            setResponse(response, errorCode);
            return;
        }

        //아이디 비밀번호가 다를 경우
        if(exception.equals("UsernameOrPasswordNotFoundException")) {
            errorCode = ErrorCode.UsernameOrPasswordNotFoundException;
            setResponse(response, errorCode);
            return;
        }

    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        json.put("code", errorCode.getCode());
        json.put("message", errorCode.getMessage());
        response.getWriter().print(json);

    }
}
