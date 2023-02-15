
package com.sparta.Board_week2.entity;

import com.sparta.Board_week2.dto.BoardRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본생성자 JPA에서 DDL을 생성, ORM으로서 일읗 할 수 있다.
//ACCESS를 제한.
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)// String varcha(255)가 기본 -> 타이틀은 줄여주는게 좋다.
    private String title;

//    @Column(nullable = false)
//    private String username;

    @Column(columnDefinition = "Text",nullable = false, length = 3000)
    private String contents; //컨텐트의 경우 varchar(2000)처럼 늘려주는게 좋다.

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();


    @Builder
    public Board(BoardRequestDto boardRequestDto, User user){
        this.title = boardRequestDto.getTitle();
        this.contents = boardRequestDto.getContents();
        this.user= user;
    }

    public void update(BoardRequestDto boardRequestDto, User user){
        this.title = boardRequestDto.getTitle();
        this.contents = boardRequestDto.getContents();
        this.user =user;

    }
}
