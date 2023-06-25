package com.suite.suite_user_service.member.dto;

import com.suite.suite_user_service.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MemberDto {
    private String email;
    private String password;
    private String role;

    @Builder
    public MemberDto(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .role(role).build();
    }

}
