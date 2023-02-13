package com.sparta.Board_week2.controller;

import com.sparta.Board_week2.dto.UserRequestDto;
import com.sparta.Board_week2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserRequestDto userRequestDto) {
        return userService.signup(userRequestDto);
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserRequestDto userRequestDto , HttpServletResponse httpServletResponse){
        return userService.login(userRequestDto,httpServletResponse);
    }
}
