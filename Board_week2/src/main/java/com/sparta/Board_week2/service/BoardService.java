package com.sparta.Board_week2.service;

import com.sparta.Board_week2.dto.BoardRequestDto;
import com.sparta.Board_week2.dto.BoardResponseDto;
import com.sparta.Board_week2.dto.MessageResponseDto;
import com.sparta.Board_week2.entity.Board;
import com.sparta.Board_week2.entity.Comment;
import com.sparta.Board_week2.entity.User;
import com.sparta.Board_week2.entity.UserRoleEnum;
import com.sparta.Board_week2.jwt.JwtUtil;
import com.sparta.Board_week2.repository.BoardRepository;
import com.sparta.Board_week2.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.sparta.Board_week2.exception.Exception.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    //전체 게시글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<BoardResponseDto>> getBoard() {

        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();

        // 댓글리스트 작성일자 기준 내림차순 정렬
        for (Board board : boardList) {
            board.getCommentList().sort(Comparator.comparing(Comment::getModifiedAt).reversed());
        }

        List<BoardResponseDto> responseDto = boardList.stream().map(BoardResponseDto::new).toList();

        return ResponseEntity.ok(responseDto);

    }

    //선택한 게시글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getBoard(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        if(board.isEmpty()){
            throw new RuntimeException(NOT_FOUND_WRTTING.getMsg());
        }
        board.get().getCommentList().sort(Comparator.comparing(Comment::getModifiedAt).reversed());
        return ResponseEntity.ok(new BoardResponseDto(board.get()));
    }

    //게시글 작성 (토큰 확인)
    @Transactional
    public ResponseEntity<Object> createBoard(BoardRequestDto requestDto, HttpServletRequest request) {
        //Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        //Token이 없거나 유효하지 않으면? -> 안됨
        if (token == null || !(jwtUtil.validateToken(token))) {
            return ResponseEntity.badRequest().body(MessageResponseDto.builder()
                    .message(String.valueOf(NOT_VALID_TOKEN))
                    .statusCode(HttpStatus.OK.value())
                    .build());
        }
        //토큰을 통한 아이디 확인
        claims = jwtUtil.getUserInfoFromToken(token);
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageResponseDto.builder()
                    .message(String.valueOf(NOT_VALID_USER))
                    .statusCode(HttpStatus.OK.value())
                    .build());
        }
        //게시글 저장
        Board board = boardRepository.save(Board.builder()
                .boardRequestDto(requestDto)
                .user(user.get())
                .build());
        //게시글 ResposneEntity로 반환
        return ResponseEntity.ok(new BoardResponseDto(board));
    }


    //게시글 업데이트
    @Transactional
    public ResponseEntity<Object> update(Long id, BoardRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        //Token이 없거나 유효하지 않으면? -> 안됨
        if (token == null || !(jwtUtil.validateToken(token))) {
            return ResponseEntity.badRequest().body(MessageResponseDto.builder()
                    .message(String.valueOf(NOT_VALID_TOKEN))
                    .statusCode(HttpStatus.OK.value())
                    .build());
        }
        //토큰을 통한 아이디 확인
        claims = jwtUtil.getUserInfoFromToken(token);
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageResponseDto.builder()
                    .message(String.valueOf(NOT_VALID_USER))
                    .statusCode(HttpStatus.OK.value())
                    .build());
        }
        //아이디를 통해 게시글이 DB에 존제하는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageResponseDto.builder()
                    .message(String.valueOf(NOT_FOUND_WRTTING))
                    .statusCode(HttpStatus.OK.value())
                    .build());
        }
        Optional<Board> found = boardRepository.findByIdAndUser(id, user.get());
        if (found.isEmpty() && user.get().getRole() == UserRoleEnum.USER) {
            throw new RuntimeException(String.valueOf(NOT_WRITER));
        }

        board.get().update(requestDto, user.get());
        boardRepository.saveAndFlush(board.get()); // responseDto에 modifedAt 업데이트 해주기 위해 saveAndFlush 사용

        return ResponseEntity.ok(new BoardResponseDto(board.get()));

    }

    @Transactional
    public ResponseEntity delete(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        //Token이 없거나 유효하지 않으면? -> 안됨
        if (token == null || !(jwtUtil.validateToken(token))) {
            return ResponseEntity.badRequest().body(MessageResponseDto.builder()
                    .message(String.valueOf(NOT_VALID_TOKEN))
                    .statusCode(HttpStatus.OK.value())
                    .build());
        }
        //토큰을 통한 아이디 확인
        claims = jwtUtil.getUserInfoFromToken(token);
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageResponseDto.builder()
                    .message(String.valueOf(NOT_VALID_USER))
                    .statusCode(HttpStatus.OK.value())
                    .build());
        }
        //아이디를 통해 게시글이 DB에 존제하는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageResponseDto.builder()
                    .message(String.valueOf(NOT_FOUND_WRTTING))
                    .statusCode(HttpStatus.OK.value())
                    .build());
        }

        boardRepository.deleteById(id);
        return ResponseEntity.ok().body(MessageResponseDto.builder()
                .message("삭제완료!")
                .statusCode(HttpStatus.OK.value())
                .build());
    }

}


