
package com.sparta.Board_week2.entity;

import com.sparta.Board_week2.dto.BoardRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor //기본생성자 자동으로
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

//    @Column(nullable = false)
//    private String username;

    @Column(columnDefinition = "Text",nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


    @Builder
    public Board(BoardRequestDto boardRequestDto, User user){
        this.title = boardRequestDto.getTitle();
        this.contents = boardRequestDto.getContents();
        this.user= user;
    }

    public void update(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.contents = boardRequestDto.getContents();

    }
}
