package com.capstone.BnagFer.domain.board.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.jwt.util.RedisUtil;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.board.dto.request.BoardRequestDto;
import com.capstone.BnagFer.domain.board.dto.request.CreateCommentRequestDto;
import com.capstone.BnagFer.domain.board.dto.request.UpdateCommentRequestDto;
import com.capstone.BnagFer.domain.board.dto.response.CommentResponseDto;
import com.capstone.BnagFer.domain.board.dto.response.CreateBoardResponseDto;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.Comment;
import com.capstone.BnagFer.domain.board.entity.Like;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.board.repository.BoardCommentRepository;
import com.capstone.BnagFer.domain.board.repository.BoardLikeRepository;
import com.capstone.BnagFer.domain.board.repository.BoardRepository;
import com.capstone.BnagFer.global.common.ApiResponse;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final AccountsCommonService accountsCommonService;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final RedisUtil redisUtil;

    public CreateBoardResponseDto createBoard(BoardRequestDto request, User user) {
        accountsCommonService.checkUserProfile(user);
        Board board = request.toEntity(user);
        boardRepository.save(board);
        return CreateBoardResponseDto.from(board);
    }

    public CreateBoardResponseDto updateBoard(Long boardId, BoardRequestDto request, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        if(!board.getUser().getId().equals(user.getId())) {
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        board.updateBoard(request);
        boardRepository.save(board);
        return CreateBoardResponseDto.from(board);
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
        String redisKey = "board:likes" + boardId; // if boardId = 1 -> redisKey = boardlikes1
        Long currentLikes = redisUtil.getLikes(redisKey); //redisUtil을 통해 Redis 캐시에서 redisKey에 해당하는 좋아요 수를 가져온다. 없으면 null반환
        if(currentLikes == null) {
            currentLikes = (long) board.getLikes().size(); //레디스 캐시에 저장할 좋아요 수
            redisUtil.save(redisKey, currentLikes, 30L, TimeUnit.DAYS); // 조회한 좋아요 수를 Redis 캐시에 30일 동안 저장하는 역할
        }
        if (like.isPresent()) {
            boardLikeRepository.delete(like.get());
            redisUtil.save(redisKey, currentLikes - 1, 30L, TimeUnit.DAYS);
            return ApiResponse.CANCELED_LIKE();
        }
        else {
            boardLikeRepository.save(new Like(user, board));
            redisUtil.save(redisKey, currentLikes + 1, 30L, TimeUnit.DAYS);
            return ApiResponse.SUCCESS_LIKE();
        }
    }
//    }

    public CommentResponseDto createComment(Long boardId, CreateCommentRequestDto request, User user, Long parentCommentId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        Comment parent = null;
        if (parentCommentId != null) {
            parent = boardCommentRepository.findById(parentCommentId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));
        }

        Comment comment = request.toEntity(user, board, parent);

        accountsCommonService.checkUserProfile(user);
        boardCommentRepository.save(comment);
        return CommentResponseDto.from(comment);
    }

    public CommentResponseDto updateComment(Long commentId, UpdateCommentRequestDto request, User user) {
        Comment comment = boardCommentRepository.findById(commentId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getUser().getId().equals(user.getId()))
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        comment.updateComment(request);
        Comment updatedComment = boardCommentRepository.save(comment);
        return CommentResponseDto.from(updatedComment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = boardCommentRepository.findById(commentId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));
        if(!comment.getUser().getId().equals(user.getId())) {
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        boardCommentRepository.deleteById(commentId);
    }
}
