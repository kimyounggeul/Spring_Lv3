package com.example.postcommentauthapi.comment.repository;

import com.example.postcommentauthapi.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
