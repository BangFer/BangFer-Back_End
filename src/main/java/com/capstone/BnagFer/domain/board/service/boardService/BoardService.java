package com.capstone.BnagFer.domain.board.service.boardService;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.board.dto.request.BoardRequestDto;
import com.capstone.BnagFer.domain.board.dto.response.BoardResponseDto;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.Like;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.board.repository.BoardLikeRepository;
import com.capstone.BnagFer.domain.board.repository.BoardRepository;
import com.capstone.BnagFer.global.common.ApiResponse;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final AccountsCommonService accountsCommonService;
    private final BoardLikeRepository boardLikeRepository;

    public BoardResponseDto createBoard(BoardRequestDto request, User user) {
        accountsCommonService.checkUserProfile(user);
        Board board = request.toEntity(user);
        board.initializeLike(); //NullPointerException 방지를 위한 좋아요 초기화
        boardRepository.save(board);
        return BoardResponseDto.from(board);
    }

    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto request, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        accountsCommonService.checkUserProfile(user);
        if(!board.getUser().getId().equals(user.getId())) {
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        board.updateBoard(request);
        boardRepository.save(board);
        return BoardResponseDto.from(board);
    }
    public void deleteBoard(Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        if(!board.getUser().getId().equals(user.getId())) {
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        boardRepository.deleteById(boardId);
    }

    public ApiResponse<Object> likeButton(Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        Optional<Like> like = boardLikeRepository.findByUserAndBoard(user, board);
        if(like.isPresent()) {
            boardLikeRepository.delete(like.get());
            return ApiResponse.CANCELED_LIKE();
        }
        else {
            boardLikeRepository.save(new Like(user, board));
            return ApiResponse.SUCCESS_LIKE();
        }
    }
}
