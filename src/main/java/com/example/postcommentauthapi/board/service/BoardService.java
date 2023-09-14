package com.example.postcommentauthapi.board.service;

import com.example.postcommentauthapi.board.dto.BoardRequestDto;
import com.example.postcommentauthapi.board.dto.BoardResponseDto;
import com.example.postcommentauthapi.board.entity.Board;
import com.example.postcommentauthapi.board.repository.BoardRepository;
import com.example.postcommentauthapi.common.jwt.JwtUtil;
import com.example.postcommentauthapi.common.dto.StringResponseDto;
import com.example.postcommentauthapi.member.entity.Member;
import com.example.postcommentauthapi.member.entity.MemberRoleEnum;
import com.example.postcommentauthapi.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.persistence.Id;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;
    public BoardResponseDto boardCreate(BoardRequestDto requestDto, HttpServletRequest req) {
        Claims userinfo = userInfo(req);
        if (userinfo.getSubject() == null) {
            throw new NullPointerException("회원 정보가 존재하지 않습니다.");
        }
        Board newBoard = new Board(requestDto, userinfo.getSubject());
        return new BoardResponseDto(boardRepository.save(newBoard));
    }

    public List<BoardResponseDto> boardList() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    public BoardResponseDto boardDetail(Long id) {
        return new BoardResponseDto(findById(id));
    }

    @Transactional
    public BoardResponseDto boardUpdate(Long id, BoardRequestDto requestDto, HttpServletRequest req) {
        Claims userInfo = userInfo(req);
        Member member = memberRepository.findByUsername(userInfo.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원정보입니다.")
        );
        Board board = findById(id);
        if (member.getRoleEnum() == MemberRoleEnum.ADMIN) {
            board.update(requestDto, userInfo.getSubject());
        } else {
            if (userInfo.getSubject().equals(board.getUsername())) {
                board.update(requestDto, userInfo.getSubject());
            } else {
                throw new IllegalArgumentException("회원 정보가 유요하지 않습니다.");
            }
        }
        return new BoardResponseDto(board);
    }

    public StringResponseDto boarDelete(Long id, HttpServletRequest req) {
        Claims userInfo = userInfo(req);
        Member member = memberRepository.findByUsername(userInfo.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원정보입니다.")
        );
        Board board = findById(id);
        if (member.getRoleEnum() == MemberRoleEnum.ADMIN) {
            boardRepository.delete(board);
            return new StringResponseDto("삭제성공", "200");
        } else {
            if (userInfo.getSubject().equals(board.getUsername())) {
                boardRepository.delete(board);
                return new StringResponseDto("삭제성공", "200");
            } else {
                throw new IllegalArgumentException("회원 정보가 유요하지 않습니다.");
            }
        }
    }

    private Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("유요하지 않은 정보입니다.")
        );
    }

    private Claims userInfo(HttpServletRequest req) {
        String givenToken = jwtUtil.getTokenFromRequest(req);
        givenToken = jwtUtil.substringToken(givenToken);
        if (!jwtUtil.validateToken(givenToken)) throw new IllegalArgumentException("유효하지 않은 회원 정보입니다");
        return jwtUtil.getUserInfoFromToken(givenToken);
    }
}
