package com.capstone.BnagFer.domain.tactic.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.notification.event.TacticCommentCreatedEvent;
import com.capstone.BnagFer.global.util.RedisUtil;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsCommonService;
import com.capstone.BnagFer.domain.tactic.dto.*;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import com.capstone.BnagFer.domain.tactic.entity.TacticLike;
import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
import com.capstone.BnagFer.domain.tactic.repository.CommentRepository;
import com.capstone.BnagFer.domain.tactic.repository.LikeRepository;
import com.capstone.BnagFer.domain.tactic.repository.TacticPositionDetailRepository;
import com.capstone.BnagFer.domain.tactic.repository.TacticRepository;
import com.capstone.BnagFer.global.common.ApiResponse;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TacticService {

    private final RedisUtil redisUtil;
    private final TacticRepository tacticRepository;
    private final AccountsCommonService accountsCommonService;
    private final CommentRepository commentRepository;
    private final TacticPositionDetailRepository tacticPositionDetailRepository;
    private final LikeRepository likeRepository;
    private final ApplicationEventPublisher eventPublisher;

    private void saveUserTactic(User user, Object tacticOrComment) {
        accountsCommonService.checkUserProfile(user);
        if (tacticOrComment instanceof Tactic) {
            tacticRepository.save((Tactic) tacticOrComment);
        } else if (tacticOrComment instanceof TacticComment) {
            commentRepository.save((TacticComment) tacticOrComment);
        }
    }

    public TacticResponse createTactic(TacticCreateRequest request, User user){

        Tactic tactic = request.toEntity(user);

        accountsCommonService.checkUserActivity(user);

        // 프로필 존재 확인
        saveUserTactic(user, tactic);

        // 11개의 개별 포지션에 대한 설명
        createPositionDetails(request.positionDetails(), tactic);

        return TacticResponse.from(tactic);
    }

    public void deleteTactic(Long tacticId, User user) {

        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        if(!tactic.getUser().getId().equals(user.getId()))
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        tacticRepository.deleteById(tacticId);
    }

    public TacticResponse updateTactic(Long tacticId, TacticUpdateRequest request, User user) {

        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        accountsCommonService.checkUserActivity(user);

        if(!tactic.getUser().getId().equals(user.getId()))
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        tactic.updateTactic(request);

        // 11개의 개별 포지션에 대한 설명 삭제
        tacticPositionDetailRepository.deleteByTactic_TacticId(tacticId);

        createPositionDetails(request.positionDetails(), tactic);

        return TacticResponse.from(tactic);
    }

    private void createPositionDetails(List<DetailCreateRequest> positionDetails, Tactic tactic) {

        for (int i = 0; i < 11; i++) {
            DetailCreateRequest detailRequest = positionDetails.get(i);
            TacticPositionDetail detail = detailRequest.toEntity(tactic, i);
            tacticPositionDetailRepository.save(detail);
        }
    }

    public TacticResponse copyTactic(Long tacticId, User user) {

        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        if(tactic.getUser().getId().equals(user.getId()))
            throw new TacticExceptionHandler(ErrorCode.CANNOT_COPY_MYSELF);

        Tactic copyTactic = Tactic.createTactic();
        copyTactic.setCopyDetail(user, tactic);

        // 프로필 존재 확인
        saveUserTactic(user, copyTactic);

        return TacticResponse.from(copyTactic);
    }

    public CommentResponse createComment(Long tacticId, CommentCreateRequest request, User user, Long parentCommentId){

        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        accountsCommonService.checkUserActivity(user);

        long commentCount = redisUtil.getCommentCount(tacticId);

        TacticComment parent = null;
        if (parentCommentId != null) {
            parent = commentRepository.findById(parentCommentId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));
        }

        TacticComment tacticComment = request.toEntity(user, tactic, parent);

        // 프로필 존재 확인
        saveUserTactic(user, tacticComment);
        commentCount++;
        redisUtil.saveCommentCount(tacticId, commentCount);

        // FCM 알림 전송
        sendNotification(user, tactic, parentCommentId, parent);

        return CommentResponse.from(tacticComment);
    }

    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request, User user) {

        TacticComment tacticComment = commentRepository.findById(commentId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));

        accountsCommonService.checkUserActivity(user);

        if(!tacticComment.getUser().getId().equals(user.getId()))
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        tacticComment.updateComment(request);

        return CommentResponse.from(tacticComment);
    }

    public void deleteComment(Long commentId, User user) {

        TacticComment tacticComment = commentRepository.findById(commentId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.COMMENT_NOT_FOUND));
        if (!tacticComment.getUser().getId().equals(user.getId()))
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);
        tacticComment.deleteComment();
    }

    public ApiResponse<Object> likeButton(Long tacticId, User user) {

        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        Optional<TacticLike> like = likeRepository.findByUserAndTactic(user, tactic);

        long likeCount = redisUtil.getLikeCount(tacticId);

        if (like.isPresent()) {
            likeRepository.delete(like.get());
            likeCount--;
            redisUtil.saveLikeCount(tacticId, likeCount);
            return ApiResponse.CANCELED_LIKE();
        } else {
            likeRepository.save(new TacticLike(user, tactic));
            likeCount++;
            redisUtil.saveLikeCount(tacticId, likeCount);
            return ApiResponse.SUCCESS_LIKE();
        }
    }

    private void sendNotification(User user, Tactic tactic, Long parentCommentId, TacticComment parent) {
        if (parentCommentId == null) {
            // 새 댓글인 경우
            if (!user.getId().equals(tactic.getUser().getId())) {
                // 전술 작성자가 댓글을 단 경우가 아닐 때만 알림 발송
                eventPublisher.publishEvent(new TacticCommentCreatedEvent(user.getId(), tactic.getUser().getId(), TacticCommentCreatedEvent.NotificationType.NEW_COMMENT));
            }
        } else {
            // 대댓글인 경우
            if (!user.getId().equals(tactic.getUser().getId())) {
                // 전술 작성자가 대댓글을 단 경우가 아닐 때 게시글 작성자에게 알림
                eventPublisher.publishEvent(new TacticCommentCreatedEvent(user.getId(), tactic.getUser().getId(), TacticCommentCreatedEvent.NotificationType.NEW_COMMENT));
            }

            if (!user.getId().equals(parent.getUser().getId()) && !parent.getUser().getId().equals(tactic.getUser().getId())) {
                // 부모 댓글 작성자가 대댓글을 단 경우가 아니고, 부모 댓글 작성자가 전술 작성자가 아닐 때 부모 댓글 작성자에게 알림
                eventPublisher.publishEvent(new TacticCommentCreatedEvent(user.getId(), parent.getUser().getId(), TacticCommentCreatedEvent.NotificationType.NEW_REPLY));
            }
        }
    }

}
