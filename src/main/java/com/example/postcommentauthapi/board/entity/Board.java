package com.example.postcommentauthapi.board.entity;

import com.example.postcommentauthapi.board.dto.BoardRequestDto;
import com.example.postcommentauthapi.comment.entity.Comment;
import com.example.postcommentauthapi.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="board")
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @OneToMany(mappedBy = "board", orphanRemoval = true, fetch = FetchType.LAZY)
    List<Comment> commentList = new ArrayList<>();


    public Board(BoardRequestDto requestDto, String username) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = username;
    }

    public void update(BoardRequestDto requestDto, String username) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = username;
    }

    public void addComment(Comment newComment) {
        this.commentList.add(newComment);
        newComment.setBoard(this);
    }
}