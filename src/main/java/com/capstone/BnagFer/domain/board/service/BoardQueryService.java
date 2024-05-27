package com.capstone.BnagFer.domain.board.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.jwt.util.RedisUtil;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryService {
    private final BoardRepository boardRepository;
    private final RedisUtil redisUtil;

    public Page<BoardResponseDto.BoardList> getBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        return boards.map(BoardResponseDto.BoardList::from);
    }

    public Page<BoardResponseDto.BoardList> getMyBoards(User user, Pageable pageable) {
        Page<Board> boards = getBoardsByUser(user, pageable);
        return boards.map(BoardResponseDto.BoardList::from);
    }

    public BoardDetailResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        Long likeCount = redisUtil.getLikeCount(boardId);

        if (likeCount == null) {
            likeCount = (long) board.getLikes().size();
            redisUtil.saveLikeCount(boardId, likeCount);
        }
        return BoardDetailResponseDto.from(board, likeCount);
    }

    public Page<Board> getBoardsByUser(User user, Pageable pageable) {
        return boardRepository.findByUser(user, pageable);
    }
}
