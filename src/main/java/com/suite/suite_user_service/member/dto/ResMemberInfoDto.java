package com.suite.suite_user_service.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResMemberInfoDto {
    private Long memberId;
    private String email;
    private String name;
    private String nickName;
    private String phone;
    private String securityNum;
    private String preferStudy;
    private String location;
    private String studyMethod;

    @Builder
    public ResMemberInfoDto(Long memberId, String email, String name, String nickName, String phone, String securityNum, String preferStudy, String location, String studyMethod) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.phone = phone;
        this.securityNum = securityNum;
        this.preferStudy = preferStudy;
        this.location = location;
        this.studyMethod = studyMethod;
    }
}
