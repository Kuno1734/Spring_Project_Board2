package com.sparta.Board_week2.service;

import com.sparta.Board_week2.dto.MessageResponseDto;
import com.sparta.Board_week2.dto.SignupRequestDto;
import com.sparta.Board_week2.entity.User;
import com.sparta.Board_week2.jwt.JwtUtil;
import com.sparta.Board_week2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.sparta.Board_week2.exception.Exception.DUPLICATED_USERNAME;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
//    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseEntity signup(SignupRequestDto requestDto, BindingResult bindingResult) {

        //signupDto에서 입력한 username, password 의 유효성 검사가 실패할 경우.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest() //status : BAD request
                    .body(MessageResponseDto.builder()
                            .message(bindingResult.getAllErrors().get(0).getDefaultMessage()));//vaild에서 예외 발생시 해당 메시지를 출력한다.
        }
        //username의 중복확인
        String username = requestDto.getUsername();
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return ResponseEntity.badRequest().body(MessageResponseDto.builder()
                    .message(String.valueOf(DUPLICATED_USERNAME))
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build());
        }
        // 입력한 username, password -> requestDto 로 user객체를 만들어서 repository에 저장
        User user = User.builder()
                .requestDto(requestDto)
                .build();
        userRepository.save(user);
        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("회원가입 성공")
                .statusCode(HttpStatus.OK.value())
                .build());
    }


    @Transactional(readOnly = true)
    public ResponseEntity<Object> login(SignupRequestDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 존재하지 않습니다.")
        );
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        MessageResponseDto messageResponseDto = MessageResponseDto.builder()
                .message("로그인 완료")
                .statusCode(HttpStatus.OK.value())
                .build();
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }
}
