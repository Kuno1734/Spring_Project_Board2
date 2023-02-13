package com.sparta.Board_week2.service;

import com.sparta.Board_week2.dto.BoardRequestDto;
import com.sparta.Board_week2.dto.BoardResponseDto;
import com.sparta.Board_week2.dto.MessageResponseDto;
import com.sparta.Board_week2.entity.Board;
import com.sparta.Board_week2.entity.User;
import com.sparta.Board_week2.jwt.JwtUtil;
import com.sparta.Board_week2.repository.BoardRepository;
import com.sparta.Board_week2.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    //전체 게시글 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoard() {
        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board i : boardList) {
            BoardResponseDto list = new BoardResponseDto(i); //생성자에 boardList의 요소 가지고옴
            boardResponseDtoList.add(list); //객체를 다시 boardResposeDto에 담아서 리스트로 출력
        }
        return boardResponseDtoList; //List<BoardResponseDto>를 반환
    }

    //선택한 게시글 조회
    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new BoardResponseDto(board);
    }

    //게시글 작성 (토큰 확인)
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Board make = Board.builder()
                    .boardRequestDto(requestDto)
                    .user(user)
                    .build();
            boardRepository.save(make);

            return BoardResponseDto.builder()
                    .board(make)
                    .build();
        } else {
            return null;
        }
    }

    //게시글 업데이트
    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
            }

            userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시글이 존재제하지 않습니다.")
            );

            board.update(requestDto);
            return BoardResponseDto.builder()
                    .board(board)
                    .build();
        } else {
            return null;
        }
    }

    @Transactional
    public MessageResponseDto delete(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
            }
            userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            boardRepository.deleteById(id);
            return MessageResponseDto.builder()
                    .message("삭제완료!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }else {
            return null;
        }
    }
}


