package com.example.postcommentauthapi.board.repository;

import com.example.postcommentauthapi.board.dto.BoardResponseDto;
import com.example.postcommentauthapi.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByModifiedAtDesc();
}