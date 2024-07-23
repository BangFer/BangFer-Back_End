package com.capstone.BnagFer.domain.report.service;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.Comment;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.board.repository.BoardCommentRepository;
import com.capstone.BnagFer.domain.board.repository.BoardRepository;
import com.capstone.BnagFer.domain.report.dto.UserResponseDto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.exception.ReportExceptionHandler;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
import com.capstone.BnagFer.domain.tactic.repository.CommentRepository;
import com.capstone.BnagFer.domain.tactic.repository.TacticRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import com.capstone.BnagFer.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class StaffActionService {
    private final UserJpaRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final RedisUtil redisUtil;
    private final BoardRepository boardRepository;
    private final TacticRepository tacticRepository;

    public UserResponseDto grantStaffAuthority(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
        if(user.getIsStaff().equals(Boolean.FALSE) && !user.getUserActivity().equals(UserActivity.BAN)
                && !user.getUserActivity().equals(UserActivity.FLAGGED) && user.getId().equals(userId)) {
            user.grantStaffAuthority();
            userRepository.save(user);
        }
        else {
            throw new ReportExceptionHandler(ErrorCode._BAD_REQUEST);
        }
        return UserResponseDto.from(user);
    }
    public UserResponseDto revokeStaffAuthority(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
        if(user.getIsStaff().equals(Boolean.TRUE) && user.getId().equals(userId)) {
            user.revokeStaffAuthority();
            userRepository.save(user);
        }
        else {
            throw new ReportExceptionHandler(ErrorCode._BAD_REQUEST);
        }
        return UserResponseDto.from(user);
    }
    public UserResponseDto changeUserActivity(UserActivity userActivity, Long reportedUserId) {
        User reportedUser = userRepository.findById(reportedUserId).orElseThrow(() -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
        reportedUser.changeActivity(userActivity);
        return UserResponseDto.from(reportedUser);
    }

    public void deleteComment(Long commentId) {

        Comment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new BoardExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));

        long boardId = comment.getBoard().getId();
        long commentCount = redisUtil.boardGetCommentCount(boardId);
        long childCnt = comment.getChildren().size();

        boardCommentRepository.deleteById(commentId);
        commentCount -= (childCnt + 1L);
        redisUtil.boardSaveCommentCount(boardId, commentCount);
    }

    public void deleteTacticComment(Long commentId) {

        TacticComment tacticComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TacticExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));

        long tacticId = tacticComment.getTactic().getTacticId();

        long commentCount = redisUtil.getCommentCount(tacticId);
        long chlidCnt = tacticComment.getChildren().size();

        commentRepository.deleteById(commentId);
        commentCount -= (chlidCnt + 1L);
        redisUtil.saveCommentCount(tacticId, commentCount);
    }

    public void deleteBoard(Long boardId) {

        boardRepository.findById(boardId).orElseThrow(() -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        boardRepository.deleteById(boardId);
    }


    public void deleteTactic(Long tacticId) {

        tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));
        tacticRepository.deleteById(tacticId);
    }
}
