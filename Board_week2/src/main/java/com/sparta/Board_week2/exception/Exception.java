package com.sparta.Board_week2.exception;

import lombok.Getter;

@Getter
public enum Exception {
    NOT_VALID_TOKEN(400,"토큰이 유효하지 않습니다."),
    NOT_VALID_USER(400,"사용자가 존재하지 않습니다."),
    NOT_FOUND_WRTTING(400,"게시글/댓글이 존재하지 않습니다."),
    NOT_WRITER(400, "작성자만 삭제/수정할 수 없습니다."),
    DUPLICATED_USERNAME(400,"중복된 사용자 이름 입니다.")
    ;

    private int ecode ;
    private String msg;
    Exception(int ecode , String msg) {
        this.ecode = ecode;
        this.msg=msg;
    }
}
