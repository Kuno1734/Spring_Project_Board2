package com.sparta.Board_week2.service;

import com.sparta.Board_week2.dto.MessageResponseDto;
import com.sparta.Board_week2.dto.UserRequestDto;
import com.sparta.Board_week2.entity.User;
import com.sparta.Board_week2.jwt.JwtUtil;
import com.sparta.Board_week2.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
//    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseEntity signup(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 회원이 존재합니다.");
        }

        Pattern namePattern = Pattern.compile("^[a-z0-9]{4,10}$"); //최소 4자-10자 이하
        Matcher nameMatcher = namePattern.matcher(username);

        Pattern passwordPattern = Pattern.compile("^[a-zA-Z0-9]{8,15}$");
        Matcher PasswordMarcher = passwordPattern.matcher(password);

        MessageResponseDto notName = MessageResponseDto.builder()
                .message("아이디는 4자 이상 10자리 미만의 영소문자, 숫자만 사용이 가능합니다.")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        MessageResponseDto notPassword = MessageResponseDto.builder()
                .message("비밀번호는 8자 이상 15자리 미만의 영대소문자, 숫자만 사용이 가능합니다.")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        if (!PasswordMarcher.find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notPassword);
        } else if (!nameMatcher.find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notName);
        } else {
            User user = User.builder()
                    .username(username)
                    .password(password)
                    .build();
            userRepository.save(user);

            MessageResponseDto ok = MessageResponseDto.builder() //ok메시지
                    .message("회원가입 완료")
                    .statusCode(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(ok); //
        }
    }


    @Transactional(readOnly = true)
    public ResponseEntity<Object> login(UserRequestDto userRequestDto, HttpServletResponse response) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

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
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }
}
