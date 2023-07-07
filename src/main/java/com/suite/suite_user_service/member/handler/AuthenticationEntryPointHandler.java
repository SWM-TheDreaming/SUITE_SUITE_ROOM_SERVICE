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
        StatusCode statusCode;

        if(exception == null) {
            statusCode = StatusCode.UNAUTHORIZEDException;
            setResponse(response, statusCode);
            return;
        }

        if(exception.equals("NullPointerException")) {
            statusCode = StatusCode.UNAUTHORIZEDException;
            setResponse(response, statusCode);
            return;
        }


        if(exception.equals("PasswordNotFoundException")) {
            statusCode = StatusCode.PasswordNotFoundException;
            setResponse(response, statusCode);
            return;
        }

        if(exception.equals("ForbiddenException")) {
            statusCode = StatusCode.ForbiddenException;
            setResponse(response, statusCode);
            return;
        }

        //토큰이 만료된 경우
        if(exception.equals("ExpiredJwtException")) {
            statusCode = StatusCode.ExpiredJwtException;
            setResponse(response, statusCode);
            return;
        }

        //아이디 비밀번호가 다를 경우
        if(exception.equals("UsernameOrPasswordNotFoundException")) {
            statusCode = StatusCode.UsernameOrPasswordNotFoundException;
            setResponse(response, statusCode);
            return;
        }

    }

    private void setResponse(HttpServletResponse response, StatusCode statusCode) throws IOException {
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        json.put("code", statusCode.getCode());
        json.put("message", statusCode.getMessage());
        response.getWriter().print(json);

    }
}
