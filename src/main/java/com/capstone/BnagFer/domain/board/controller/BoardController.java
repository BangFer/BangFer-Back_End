package com.capstone.BnagFer.domain.board.controller;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.dto.request.BoardRequestDto;
import com.capstone.BnagFer.domain.board.dto.response.BoardDetailResponseDto;
import com.capstone.BnagFer.domain.board.dto.response.CreateBoardResponseDto;
import com.capstone.BnagFer.domain.board.service.BoardQueryService;
import com.capstone.BnagFer.domain.board.service.BoardService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static com.capstone.BnagFer.global.common.ApiResponse.onSuccess;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final BoardQueryService boardQueryService;

    @GetMapping //게시판 리스트 조회
    public ApiResponse<Page<BoardDetailResponseDto.BoardList>> getBoardsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardDetailResponseDto.BoardList> boardsList = boardQueryService.getBoards(pageable);
        return onSuccess(boardsList);

    }

    @GetMapping("/{boardId}")
    public ApiResponse<BoardDetailResponseDto> getBoard (@PathVariable Long boardId) {
        BoardDetailResponseDto board = boardQueryService.getBoard(boardId);
        return ApiResponse.onSuccess(board);
    }

    @GetMapping("/myboards")
    public ApiResponse<Page<BoardDetailResponseDto.BoardList>> getMyBoardsList(
            @LoginUser User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardDetailResponseDto.BoardList> myBoardsList = boardQueryService.getMyBoards(user, pageable);
        return onSuccess(myBoardsList);
    }

    @PostMapping
    public ApiResponse<CreateBoardResponseDto> createBoard(@RequestBody @Valid BoardRequestDto request, @LoginUser User user) {
        CreateBoardResponseDto board = boardService.createBoard(request, user);
        return ApiResponse.onSuccess(board);
    }

    @PutMapping("/{boardId}")
    public ApiResponse<CreateBoardResponseDto> updateBoard(@PathVariable long boardId, @RequestBody @Valid BoardRequestDto request, @LoginUser User user) {
        CreateBoardResponseDto updatedBoard = boardService.updateBoard(boardId, request, user);
        return ApiResponse.onSuccess(updatedBoard);
    }
    @DeleteMapping("/{boardId}")
    public ApiResponse<Object> deleteBoard(@PathVariable Long boardId, @LoginUser User user) {
        boardService.deleteBoard(boardId, user);
        return ApiResponse.noContent();
    }

    @PostMapping("/{boardId}/like")
    public ApiResponse<Object> likeBoard(@PathVariable Long boardId, @LoginUser User user) {
        return boardService.likeButton(boardId, user);
    }
}
