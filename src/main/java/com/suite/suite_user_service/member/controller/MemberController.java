package com.suite.suite_user_service.member.controller;

import com.suite.suite_user_service.member.dto.Message;
import com.suite.suite_user_service.member.dto.ReqMemberDto;
import com.suite.suite_user_service.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<Message> signupSuite(@RequestBody ReqMemberDto reqMemberDto) {
        reqMemberDto.encodePassword(passwordEncoder);

        return ResponseEntity.ok(memberService.saveMemberInfo(reqMemberDto));
    }

}
