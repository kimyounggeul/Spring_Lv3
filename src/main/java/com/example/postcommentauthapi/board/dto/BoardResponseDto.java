package com.example.postcommentauthapi.board.dto;

import com.example.postcommentauthapi.board.entity.Board;
import com.example.postcommentauthapi.comment.dto.CommentRequestDto;
import com.example.postcommentauthapi.comment.dto.CommentResponseDto;
import com.example.postcommentauthapi.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.commentList = board.getCommentList().stream().map(CommentResponseDto::new).toList();
    }
}
