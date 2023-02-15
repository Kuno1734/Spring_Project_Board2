package com.sparta.Board_week2.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class SignupRequestDto {

    @Pattern(regexp ="^[a-z0-9]{4,10}$", message = "아이디는 4자 이상 10자리 미만의 영소문자, 숫자만 사용이 가능합니다.")
    private String username;
    @Pattern(regexp ="^[a-zA-Z\\d`~!@#$%^&*()-_=+]{8,15}$", message = "비밀번호는 8자 이상 15자리 미만의 영대소문자, 숫자만 사용이 가능합니다.")
    private String password;

    private Boolean admin = false;


}
