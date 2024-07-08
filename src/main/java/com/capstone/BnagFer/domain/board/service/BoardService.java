package com.capstone.BnagFer.domain.board.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.board.dto.request.BoardRequestDto;
import com.capstone.BnagFer.domain.board.dto.request.CreateCommentRequestDto;
import com.capstone.BnagFer.domain.board.dto.request.UpdateCommentRequestDto;
import com.capstone.BnagFer.domain.board.dto.response.CommentResponseDto;
import com.capstone.BnagFer.domain.board.dto.response.CreateBoardResponseDto;
import com.capstone.BnagFer.domain.board.entity.*;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.board.repository.BoardCommentRepository;
import com.capstone.BnagFer.domain.board.repository.BoardLikeRepository;
import com.capstone.BnagFer.domain.board.repository.BoardRepository;
import com.capstone.BnagFer.global.common.ApiResponse;
import com.capstone.BnagFer.global.common.ErrorCode;
import com.capstone.BnagFer.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

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
        if (!board.getUser().getId().equals(user.getId())) {
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        board.updateBoard(request);
        boardRepository.save(board);
        return CreateBoardResponseDto.from(board);
    }

    public void deleteBoard(Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        if (!board.getUser().getId().equals(user.getId())) {
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        boardRepository.deleteById(boardId);
    }

    public ApiResponse<Object> likeButton(Long boardId, User user) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));

        Optional<Like> like = boardLikeRepository.findByUserAndBoard(user, board);

        long likeCount = redisUtil.boardGetLikeCount(boardId);

        if (like.isPresent()) {
            boardLikeRepository.delete(like.get());
            likeCount--;
            redisUtil.boardSaveLikeCount(boardId, likeCount);
            return ApiResponse.CANCELED_LIKE();
        } else {
            boardLikeRepository.save(new Like(user, board));
            likeCount++;
            redisUtil.boardSaveLikeCount(boardId, likeCount);
            return ApiResponse.SUCCESS_LIKE();
        }
    }

    public CommentResponseDto createComment(Long boardId, CreateCommentRequestDto request, User user, Long parentCommentId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        long commentCount = redisUtil.boardGetCommentCount(boardId);
        Comment parent = null;
        if (parentCommentId != null) {
            parent = boardCommentRepository.findById(parentCommentId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));
        }
//        if(user.getIsBlocked()) {
//            throw new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND);
//        }
        Comment comment = request.toEntity(user, board, parent);
        accountsCommonService.checkUserProfile(user);
        boardCommentRepository.save(comment);
        commentCount++;
        redisUtil.boardSaveCommentCount(boardId, commentCount);
        return CommentResponseDto.from(comment);
    }

    public CommentResponseDto updateComment(Long commentId, UpdateCommentRequestDto request, User user) {
        Comment comment = boardCommentRepository.findById(commentId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(user.getId()))
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        comment.updateComment(request);
        Comment updatedComment = boardCommentRepository.save(comment);
        return CommentResponseDto.from(updatedComment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = boardCommentRepository.findById(commentId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));
        long boardId = comment.getBoard().getId();
        long commentCount = redisUtil.boardGetCommentCount(boardId);
        long childCnt = comment.getChildren().size();
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new BoardExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        }
        boardCommentRepository.deleteById(commentId);
        commentCount -= (childCnt + 1L);
        redisUtil.boardSaveCommentCount(boardId, commentCount);
    }
}


