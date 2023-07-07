package com.suite.suite_user_service.member.service;

import com.suite.suite_user_service.member.dto.Message;
import com.suite.suite_user_service.member.dto.ReqMemberDto;
import com.suite.suite_user_service.member.dto.ResMemberDto;

public interface MemberService {

    Message saveMemberInfo(ReqMemberDto reqMemberDto);
}
