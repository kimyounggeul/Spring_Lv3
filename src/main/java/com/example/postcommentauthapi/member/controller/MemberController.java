package com.example.postcommentauthapi.member.controller;

import com.example.postcommentauthapi.common.dto.StringResponseDto;
import com.example.postcommentauthapi.member.dto.LoginRequestDto;
import com.example.postcommentauthapi.member.dto.SignupRequestDto;
import com.example.postcommentauthapi.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    //로그인
    @PostMapping("/auth/signup")
    public ResponseEntity<StringResponseDto> signup (@Valid @RequestBody SignupRequestDto requestDto){
        return ResponseEntity.ok(memberService.signup(requestDto));
    }

    // 회원가입
    @PostMapping("/auth/login")
    public ResponseEntity<StringResponseDto> login (@RequestBody LoginRequestDto requestDto, HttpServletResponse res){
        return ResponseEntity.ok(memberService.login(requestDto, res));
    }
}

