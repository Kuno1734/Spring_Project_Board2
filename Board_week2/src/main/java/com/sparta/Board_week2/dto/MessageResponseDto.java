package com.sparta.Board_week2.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageResponseDto {

    private String message;
    private int statusCode;


    @Builder
    public MessageResponseDto(String message, int statusCode) {
        this.message =  message;
        this.statusCode = statusCode;
    }
}
