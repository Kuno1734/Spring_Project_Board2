package com.sparta.Board_week2.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class UserRequestDto {

    private String username;
    private String password;

}
