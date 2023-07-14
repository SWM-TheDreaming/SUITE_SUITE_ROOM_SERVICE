package com.suite.suite_user_service.member.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
@Getter
public class EmailDto {
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String code;

    public void setCode(String code) {
        this.code = code;
    }
}
