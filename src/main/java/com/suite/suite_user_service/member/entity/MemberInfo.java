package com.suite.suite_user_service.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member_info")
public class MemberInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_info_id")
    private Long memberInfoId;

    private String name;

    private String nickname;

    private String phone;

    @Column(name = "security_num")
    private String securityNum;

    @Column(name = "prefer_study")
    private String preferStudy;

    @Column(name = "location")
    private String location;

    @Column(name = "study_method")
    private String studyMethod;

    //프로필 이미지
    //private ProfileImage profileImage;


}
