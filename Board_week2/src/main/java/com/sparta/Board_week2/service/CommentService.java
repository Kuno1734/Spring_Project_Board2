package com.sparta.Board_week2.service;

import com.sparta.Board_week2.dto.CommentRequestDto;
import com.sparta.Board_week2.dto.CommentResponseDto;
import com.sparta.Board_week2.dto.MessageResponseDto;
import com.sparta.Board_week2.entity.Board;
import com.sparta.Board_week2.entity.Comment;
import com.sparta.Board_week2.entity.User;
import com.sparta.Board_week2.entity.UserRoleEnum;
import com.sparta.Board_week2.exception.Exception;
import com.sparta.Board_week2.jwt.JwtUtil;
import com.sparta.Board_week2.repository.BoardRepository;
import com.sparta.Board_week2.repository.CommentRepository;
import com.sparta.Board_week2.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.sparta.Board_week2.exception.ExceptionHandling.responseException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity<Object> createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        //request 에서 토큰확인
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // token이 없거나 유효하지 않다면 -> 작성불가
        if (token == null || !(jwtUtil.validateToken(token))) {
            return responseException(Exception.NOT_VALID_TOKEN);
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        //token에서 가저온 사용자 정보 조회
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) {
            return responseException(Exception.NOT_VALID_USER);
        }
        //선택한 게시글 조회
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            return responseException(Exception.NOT_FOUND_WRTTING);
        }

        Comment comment = commentRepository.save(Comment.builder()
                .requestDto(requestDto)
                .board(board.get())
                .user(user.get())
                .build());
        return ResponseEntity.ok(CommentResponseDto.builder().comment(comment).build());

    }

    @Transactional
    public ResponseEntity<Object> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        //request 에서 토큰확인
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // token이 없거나 유효하지 않다면 -> 작성불가
        if (token == null || !(jwtUtil.validateToken(token))) {
            return responseException(Exception.NOT_VALID_TOKEN);
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        //token에서 가저온 사용자 정보 조회
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) {
            return responseException(Exception.NOT_VALID_USER);
        }
        //선택한 댓글 유무 조회
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            return responseException(Exception.NOT_FOUND_WRTTING);
        }

        Optional<Comment> found = commentRepository.findByIdAndUser(id, user.get());
        if (found.isEmpty() && user.get().getRole() == UserRoleEnum.USER) {
            return responseException(Exception.NOT_WRITER);
        }

        comment.get().update(requestDto, user.get());
        commentRepository.saveAndFlush(comment.get());

        return ResponseEntity.ok(CommentResponseDto.builder().comment(comment.get()).build());
    }


    public ResponseEntity<Object> deleteComment(Long id, HttpServletRequest request) {
        //request 에서 토큰확인
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // token이 없거나 유효하지 않다면 -> 작성불가
        if (token == null || !(jwtUtil.validateToken(token))) {
            return responseException(Exception.NOT_VALID_TOKEN);
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        //token에서 가저온 사용자 정보 조회
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) {
            return responseException(Exception.NOT_VALID_USER);
        }
        //선택한 댓글 유무 조회
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            return responseException(Exception.NOT_FOUND_WRTTING);
        }

        Optional<Comment> found = commentRepository.findByIdAndUser(id, user.get());
        if (found.isEmpty() && user.get().getRole() == UserRoleEnum.USER) {
            return responseException(Exception.NOT_WRITER);
        }

        commentRepository.deleteById(id);
        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("댓글 삭제 완료")
                .statusCode(HttpStatus.OK.value())
                .build());

    }
}

