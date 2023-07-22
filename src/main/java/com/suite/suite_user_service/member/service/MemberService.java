package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.AuthorizerDto;
import com.suite.suite_user_service.member.dto.Message;
import com.suite.suite_user_service.member.dto.ReqSignUpMemberDto;

public interface MemberService {

    Message saveMemberInfo(ReqSignUpMemberDto reqSignUpMemberDto);
    Message getMemberInfo(AuthorizerDto authorizerDto);
}
