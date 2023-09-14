package com.example.postcommentauthapi.comment.service;

import com.example.postcommentauthapi.board.entity.Board;
import com.example.postcommentauthapi.board.repository.BoardRepository;
import com.example.postcommentauthapi.comment.dto.CommentRequestDto;
import com.example.postcommentauthapi.comment.dto.CommentResponseDto;
import com.example.postcommentauthapi.comment.entity.Comment;
import com.example.postcommentauthapi.comment.repository.CommentRepository;
import com.example.postcommentauthapi.common.jwt.JwtUtil;
import com.example.postcommentauthapi.common.dto.StringResponseDto;
import com.example.postcommentauthapi.member.entity.Member;
import com.example.postcommentauthapi.member.entity.MemberRoleEnum;
import com.example.postcommentauthapi.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.JwtDsl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public CommentResponseDto commentCreate(CommentRequestDto requestDto, HttpServletRequest req) {
        Claims userInfo = userInfo(req);
        Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(
                () -> new NullPointerException("유효하지 않은 정보입니다.")
        );
        Comment newComment = new Comment(requestDto, userInfo.getSubject());
        board.addComment(newComment);
        return new CommentResponseDto(commentRepository.save(newComment));
    }

    @Transactional
    public CommentResponseDto commentUpdate(Long id, CommentRequestDto requestDto, HttpServletRequest req) {
        Claims userInfo = userInfo(req);
        Member member = memberRepository.findByUsername(userInfo.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원정보입니다.")
        );
        Comment comment = findById(id);
        if (member.getRoleEnum() == MemberRoleEnum.ADMIN) {
            comment.update(requestDto, userInfo.getSubject());
            return new CommentResponseDto(comment);
        } else {
            if (userInfo.getSubject().equals(comment.getUsername())) {
                comment.update(requestDto, userInfo.getSubject());
                return new CommentResponseDto(comment);
            } else {
                throw new IllegalArgumentException("유효하지 않은 회원정보입니다.");
            }
        }
    }

    public StringResponseDto commentDelete(Long id, HttpServletRequest req) {
        Claims userInfo = userInfo(req);
        Member member = memberRepository.findByUsername(userInfo.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원정보입니다.")
        );
        Comment comment = findById(id);
        if (member.getRoleEnum() == MemberRoleEnum.ADMIN) {
            commentRepository.delete(comment);
            return new StringResponseDto("삭제 성공", "200");
        } else {
            if (userInfo.getSubject().equals(comment.getUsername())) {
                commentRepository.delete(comment);
                return new StringResponseDto("삭제 성공", "200");
            } else {
                throw new IllegalArgumentException("유효하지 않은 회원정보입니다.");
            }
        }
    }

    private Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new NullPointerException("유효하지 않은 댓글입니다")
        );
    }

    private Claims userInfo(HttpServletRequest req) {
        String givenToken = jwtUtil.getTokenFromRequest(req);
        givenToken = jwtUtil.substringToken(givenToken);
        if (!jwtUtil.validateToken(givenToken)) throw new IllegalArgumentException("Invalid User Credentials");
        return jwtUtil.getUserInfoFromToken(givenToken);
    }

}

