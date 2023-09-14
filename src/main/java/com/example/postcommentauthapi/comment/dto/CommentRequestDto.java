package com.example.postcommentauthapi.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long boardId;
    private String content;
}

