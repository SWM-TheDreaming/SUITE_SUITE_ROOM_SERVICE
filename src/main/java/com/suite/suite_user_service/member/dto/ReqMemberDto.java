package com.suite.suite_user_service.member.dto;

import com.suite.suite_user_service.member.entity.Member;
import com.suite.suite_user_service.member.entity.MemberInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter
@Getter
@NoArgsConstructor
public class ReqMemberDto {
    private String email;
    private String password;
    private String role;
    private String name;
    private String nickName;
    private String phone;
    private String securityNum;
    private String preferStudy;
    private String location;
    private String studyMethod;

    @Builder
    public ReqMemberDto(String email, String password, String role, String name, String nickName, String phone, String securityNum, String preferStudy, String location, String studyMethod) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.nickName = nickName;
        this.phone = phone;
        this.securityNum = securityNum;
        this.preferStudy = preferStudy;
        this.location = location;
        this.studyMethod = studyMethod;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
    public Member toMemberEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .role(role).build();
    }

    public MemberInfo toMemberInfoEntity() {
        return MemberInfo.builder()
                .name(name)
                .nickname(nickName)
                .phone(phone)
                .securityNum(securityNum)
                .preferStudy(preferStudy)
                .location(location)
                .studyMethod(studyMethod).build();
    }

}
