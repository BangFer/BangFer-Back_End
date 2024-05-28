package com.capstone.BnagFer.domain.board.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.board.dto.response.BoardDetailResponseDto;
import com.capstone.BnagFer.domain.board.dto.response.BoardResponseDto;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.board.repository.BoardRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryService {

    private final BoardRepository boardRepository;
    private final UserJpaRepository userJpaRepository;

    public Page<BoardDetailResponseDto.BoardList> getBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        return boards.map(BoardDetailResponseDto.BoardList::from);
    }

    public Page<BoardDetailResponseDto.BoardList> getMyBoards(User user, Pageable pageable) {
        Page<Board> boards = getBoardsByUser(user, pageable);
        return boards.map(BoardDetailResponseDto.BoardList::from);
    }

    public Page<BoardDetailResponseDto.BoardList> getUserBoards(Long userId, Pageable pageable) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND));
        Page<Board> boards = getBoardsByUser(user, pageable);
        return boards.map(BoardDetailResponseDto.BoardList::from);
    }

    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        return BoardResponseDto.from(board);
    }

    public Page<Board> getBoardsByUser(User user, Pageable pageable) {
        return boardRepository.findByUser(user, pageable);
    }
}
