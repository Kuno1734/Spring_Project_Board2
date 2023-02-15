package com.sparta.Board_week2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.sparta.Board_week2.entity.Board;
import com.sparta.Board_week2.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter //response에 사용이유 -> jaxson라이브러리 에서 사용해서 필요
//@RequiredArgsConstructor //여기서는 필요가없다.
public class BoardResponseDto {

    private Long id;
    private String title;
    private String username;
    private String contents;

    private List<CommentResponseDto> commentList = new ArrayList<>();
    @JsonFormat(pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
    private LocalDateTime modifiedAt;


    @Builder
    public BoardResponseDto(Board board) {
        //데이터를 넘겨주기전 프론트와 상의해서 필요한 부분만 넘겨주는 방법을 고려한다.
        id = board.getId();
        title = board.getTitle();
        contents = board.getContents();
        username = board.getUser().getUsername();
        createdAt = board.getCreatedAt();
        modifiedAt = board.getModifiedAt();
        commentList = board.getCommentList().stream().map(CommentResponseDto::new).toList();

    }
}
