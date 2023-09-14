package com.example.postcommentauthapi.comment.entity;

import com.example.postcommentauthapi.board.entity.Board;
import com.example.postcommentauthapi.comment.dto.CommentRequestDto;
import com.example.postcommentauthapi.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id", nullable = false)
    private Board board;

    public Comment(CommentRequestDto requestDto, String username) {
        this.username = username;
        this.content = requestDto.getContent();
    }

    public void update(CommentRequestDto requestDto, String username) {
        this.content = requestDto.getContent();
        this.username = username;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
