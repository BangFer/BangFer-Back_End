package com.capstone.BnagFer.domain.board.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.board.dto.response.BoardDetailResponseDto;
import com.capstone.BnagFer.domain.board.dto.response.BoardListDto;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.board.repository.BoardBlockRepository;
import com.capstone.BnagFer.domain.board.repository.BoardRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import com.capstone.BnagFer.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryService {

    private final BoardRepository boardRepository;
    private final RedisUtil redisUtil;
    private final UserJpaRepository userJpaRepository;
    private final BoardBlockRepository boardBlockRepository;
    public Page<BoardListDto> getBoards(User user, Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);

        // `Page<Board>`를 `List<Board>`로 변환
        List<BoardListDto> filteredBoards = boards.stream()
                .filter(board -> !isUserBlocked(user, board.getUser()))
                .map(BoardListDto::from)
                .collect(Collectors.toList());

        // `List<BoardListDto>`를 `Page<BoardListDto>`로 변환하여 반환
        return new PageImpl<>(filteredBoards, pageable, boards.getTotalElements());
    }

    public Page<BoardListDto> getMyBoards(User user, Pageable pageable) {
        Page<Board> boards = getBoardsByUser(user, pageable);
        return boards.map(BoardListDto::from);
    }

    public Page<BoardListDto> getUserBoards(Long userId, Pageable pageable) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND));
        Page<Board> boards = getBoardsByUser(user, pageable);
        return boards.map(BoardListDto::from);
    }

    public BoardDetailResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        Long likeCount = redisUtil.boardGetLikeCount(boardId);

        if (likeCount == null) {
            likeCount = (long) board.getLikes().size();
            redisUtil.boardSaveLikeCount(boardId, likeCount);
        }
        Long commentCount = redisUtil.boardGetCommentCount(boardId);
        if(commentCount == null){
            commentCount = (long) board.getComments().size();
            redisUtil.boardSaveCommentCount(boardId, commentCount);
        }

        return BoardDetailResponseDto.from(board, likeCount, commentCount);
    }

    public Page<Board> getBoardsByUser(User user, Pageable pageable) {
        return boardRepository.findByUser(user, pageable);
    }

    public boolean isUserBlocked(User blockUser, User isBlockedUser) {
        return boardBlockRepository.existsByBlockUserAndIsBlockedUser(blockUser, isBlockedUser);
    }
}
