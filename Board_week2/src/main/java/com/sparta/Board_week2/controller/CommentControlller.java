package com.sparta.Board_week2.controller;

import com.sparta.Board_week2.dto.CommentRequestDto;
import com.sparta.Board_week2.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentControlller {
    private final CommentService commentService;

    @PostMapping("/comment/{id}")
    public ResponseEntity<Object> createComment(@PathVariable Long id , @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
        return commentService.createComment(id,requestDto,request);
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<Object> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
        return commentService.updateComment(id,requestDto,request);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long id, HttpServletRequest request){
        return commentService.deleteComment(id,request);
    }
}
