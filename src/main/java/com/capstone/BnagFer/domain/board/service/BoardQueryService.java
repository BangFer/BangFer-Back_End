package com.capstone.BnagFer.domain.board.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.board.dto.response.BoardDetailResponseDto;
import com.capstone.BnagFer.domain.board.dto.response.BoardListDto;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.Comment;
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
        // 모든 게시글을 조회
        Page<Board> boards = boardRepository.findAll(pageable);

        // 사용자가 차단한 다른 사용자들의 ID 목록을 가져온다
        List<Long> blockedUserIds = boardBlockRepository.findIsBlockUserIdsByBlockUserId(user.getId());

        // 차단한 사용자의 게시글이 제외된 리스트 생성
        List<BoardListDto> filteredBoards = boards.getContent().stream()
                .filter(board -> !blockedUserIds.contains(board.getUser().getId()) || board.getUser().getId().equals(user.getId()))
                .map(BoardListDto::from)
                .collect(Collectors.toList());

        // 필터링된 리스트를 Page 객체로 변환하여 반환
        return new PageImpl<>(filteredBoards, pageable, boards.getTotalElements());
    }

    public Page<BoardListDto> getMyBoards(User user, Pageable pageable) {
        Page<Board> boards = getBoardsByUser(user, pageable);
        return boards.map(BoardListDto::from);
    }

    public Page<BoardListDto> getUserBoards(User user, Long userId, Pageable pageable) {
        User targetUser = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND));

        // 사용자가 차단한 다른 사용자들의 ID 목록을 가져온다
        List<Long> blockedUserIds = boardBlockRepository.findIsBlockUserIdsByBlockUserId(user.getId());

        // 만약 조회하려는 사용자가 차단된 사용자 목록에 있으면 예외를 던진다
        if (blockedUserIds.contains(userId)) {
            throw new BoardExceptionHandler(ErrorCode.IS_BLOCKED_USER);
        }

        Page<Board> boards = getBoardsByUserId(userId, pageable);
        return boards.map(BoardListDto::from);
    }

    public BoardDetailResponseDto getBoard(User user, Long boardId) {
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

        List<Comment> filteredComments = board.getComments().stream()
                .filter(comment -> !isUserBlocked(user, comment.getUser()))
                .collect(Collectors.toList());

        return BoardDetailResponseDto.from(board, likeCount, commentCount, filteredComments);
    }

    public Page<Board> getBoardsByUser(User user, Pageable pageable) {
        return boardRepository.findByUser(user, pageable);
    }

    // 특정 사용자 ID로 게시글을 조회하는 메소드
    public Page<Board> getBoardsByUserId(Long userId, Pageable pageable) {
        return boardRepository.findByUserId(userId, pageable);
    }

    public boolean isUserBlocked(User blockUser, User isBlockedUser) {
        return boardBlockRepository.existsByBlockUserAndIsBlockedUser(blockUser, isBlockedUser);
    }


}
