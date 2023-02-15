package com.sparta.Board_week2.dto;

import com.sparta.Board_week2.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.PrimitiveIterator;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String contents;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    @Builder
    public CommentResponseDto(Comment comment){
        this.id =  comment.getId();
        this.contents = comment.getContents();
        this.username = comment.getUser().getUsername();
        this.createAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
