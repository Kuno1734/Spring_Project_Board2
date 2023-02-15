package com.sparta.Board_week2.controller;

import com.sparta.Board_week2.dto.SignupRequestDto;
import com.sparta.Board_week2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupRequestDto userRequestDto, BindingResult bindingResult) {
        return userService.signup(userRequestDto,bindingResult);
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody SignupRequestDto userRequestDto , HttpServletResponse httpServletResponse){
        return userService.login(userRequestDto,httpServletResponse);
    }
}
