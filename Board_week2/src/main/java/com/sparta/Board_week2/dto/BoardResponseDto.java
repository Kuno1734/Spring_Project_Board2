package com.sparta.Board_week2.dto;

import com.sparta.Board_week2.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public BoardResponseDto(Board board) {
        id = board.getId();
        title = board.getTitle();
        contents = board.getContents();
        username = board.getUser().getUsername();
        createdAt = board.getCreatedAt();
        modifiedAt = board.getModifiedAt();

    }
}
