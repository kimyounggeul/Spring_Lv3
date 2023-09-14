package com.example.postcommentauthapi.board.controller;

import com.example.postcommentauthapi.board.dto.BoardRequestDto;
import com.example.postcommentauthapi.board.dto.BoardResponseDto;
import com.example.postcommentauthapi.board.service.BoardService;

import com.example.postcommentauthapi.common.dto.StringResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // Board Create
    @PostMapping("/board")
    public ResponseEntity<BoardResponseDto> boardCreate(@RequestBody BoardRequestDto requestDto, HttpServletRequest req) {
        return ResponseEntity.ok(boardService.boardCreate(requestDto, req));
    }

    // Board Read
    // List
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> boardList() {
        return ResponseEntity.ok(boardService.boardList());
    }

    // Detail
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardResponseDto> boardDetail(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.boardDetail(id));
    }

    // Board Update
    @PutMapping("/board/{id}")
    public ResponseEntity<BoardResponseDto> boardUpdate(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest req) {
        return ResponseEntity.ok(boardService.boardUpdate(id,requestDto, req));
    }
    // Board Delete
    @DeleteMapping("/board/{id}")
    public ResponseEntity<StringResponseDto> boardDelete(@PathVariable Long id, HttpServletRequest req) {
        return ResponseEntity.ok(boardService.boarDelete(id, req));
    }
}
