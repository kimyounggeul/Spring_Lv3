package com.example.postcommentauthapi.member.service;

import com.example.postcommentauthapi.common.dto.StringResponseDto;
import com.example.postcommentauthapi.common.jwt.JwtUtil;
import com.example.postcommentauthapi.member.dto.LoginRequestDto;
import com.example.postcommentauthapi.member.dto.SignupRequestDto;
import com.example.postcommentauthapi.member.entity.Member;
import com.example.postcommentauthapi.member.entity.MemberRoleEnum;
import com.example.postcommentauthapi.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원가입
    @Transactional
    public StringResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<Member> signMember = memberRepository.findByUsername(username);
        if (signMember.isPresent()){
            return new StringResponseDto("아이디가 중복됩니다", "400");
        }

        MemberRoleEnum roleEnum = MemberRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            roleEnum = MemberRoleEnum.ADMIN;
        }

        Member member = new Member(username, password, roleEnum);
        memberRepository.save(member);
        return new StringResponseDto("회원가입을 축하합니다", "200");
    }

    // 로그인
    public StringResponseDto login(LoginRequestDto requestDto, HttpServletResponse res) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        Member member = memberRepository.findByUsername(username).orElseThrow(()
                -> new IllegalArgumentException("일치하는 아이디가 없습니다")
        );

        if (!passwordEncoder.matches(password,member.getPassword())) {
            return new StringResponseDto("회원을 찾을 수 없습니다", "400");
        }


        String token = jwtUtil.createToken(member.getUsername(), member.getRoleEnum());
        jwtUtil.addJwtToCookie(token, res);
        return new StringResponseDto("로그인에 성공하셨습니다", "200");
    }
}