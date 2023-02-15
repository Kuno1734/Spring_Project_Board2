package com.sparta.Board_week2.exception;

import com.sparta.Board_week2.dto.MessageResponseDto;
import org.springframework.http.ResponseEntity;

public class ExceptionHandling {

    public static ResponseEntity<Object> responseException(Exception except){
        MessageResponseDto responseDto = MessageResponseDto.builder()
                .message(except.getMsg())
                .statusCode(except.getEcode())
                .build();
        return ResponseEntity.badRequest().body(responseDto);
    }
}
