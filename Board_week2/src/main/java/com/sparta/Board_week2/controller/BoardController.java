package com.sparta.Board_week2.controller;

import com.sparta.Board_week2.dto.MessageResponseDto;
import com.sparta.Board_week2.dto.BoardRequestDto;
import com.sparta.Board_week2.dto.BoardResponseDto;
import com.sparta.Board_week2.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board")
    public List<BoardResponseDto> getBoard() { return boardService.getBoard(); }

    @GetMapping("/board/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request){
        return boardService.createBoard(requestDto,request) ;
    }

    @PutMapping("/board/{id}")
    public BoardResponseDto update(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request){
        return boardService.update(id,requestDto,request);
    }

    @DeleteMapping("/board/{id}")
    public MessageResponseDto delete(@PathVariable Long id, HttpServletRequest request){
        return boardService.delete(id,request);
    }


}
