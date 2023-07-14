package com.suite.suite_user_service.member.controller;

import com.suite.suite_user_service.member.dto.EmailDto;
import com.suite.suite_user_service.member.dto.Message;
import com.suite.suite_user_service.member.dto.ReqMemberDto;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import com.suite.suite_user_service.member.service.EmailService;
import com.suite.suite_user_service.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<Message> signupSuite(@RequestBody ReqMemberDto reqMemberDto) {
        reqMemberDto.encodePassword(passwordEncoder);

        return ResponseEntity.ok(memberService.saveMemberInfo(reqMemberDto));
    }

    @PostMapping("/mail")
    public ResponseEntity<Message> verifyEmail(@Valid @RequestBody EmailDto emailDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        return ResponseEntity.ok(emailService.sendEmailCode(emailDto));
    }

}
