package com.sparta.Board_week2.repository;

import com.sparta.Board_week2.entity.Comment;
import com.sparta.Board_week2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findByIdAndUser(Long id, User user);
}
