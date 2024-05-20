package com.capstone.BnagFer.domain.tactic.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class TacticService {

    private final TacticRepository tacticRepository;
    private final AccountsCommonService accountsCommonService;
    private final CommentRepository commentRepository;
    private final TacticPositionDetailRepository tacticPositionDetailRepository;
    private final LikeRepository likeRepository;

    public TacticResponse createTactic(TacticCreateRequest request, User user){
        Tactic tactic = request.toEntity(user);

        // 프로필 존재 확인
        accountsCommonService.checkUserProfile(user);
        tacticRepository.save(tactic);

        for(DetailCreateRequest detailRequest : request.positionDetails()){
            TacticPositionDetail detail = detailRequest.toEntity(tactic);
            tacticPositionDetailRepository.save(detail);
        }
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

        if(!tactic.getUser().getId().equals(user.getId()))
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        tactic.updateTactic(request);
        Tactic updatedTactic = tacticRepository.save(tactic);

        tacticPositionDetailRepository.deleteByTactic_TacticId(tacticId);

        for(DetailCreateRequest detailRequest : request.positionDetails()){
            TacticPositionDetail detail = detailRequest.toEntity(tactic);
            tacticPositionDetailRepository.save(detail);
        }

        return TacticResponse.from(updatedTactic);
    }

    public TacticResponse copyTactic(Long tacticId, User user) {

        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        if(tactic.getUser().getId().equals(user.getId()))
            throw new TacticExceptionHandler(ErrorCode.CANNOT_COPY_MYSELF);

        Tactic copyTactic = Tactic.createTactic();
        copyTactic.setCopyDetail(user, tactic);

        // 프로필 존재 확인
        accountsCommonService.checkUserProfile(user);
        tacticRepository.save(copyTactic);

        return TacticResponse.from(copyTactic);
    }

    public CommentResponse createComment(Long tacticId, CommentCreateRequest request, User user, Long parentCommentId){

        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        TacticComment parent = null;
        if (parentCommentId != null) {
            parent = commentRepository.findById(parentCommentId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.Comment_NOT_FOUND));
        }

        TacticComment tacticComment = request.toEntity(user, tactic, parent);

        // 프로필 존재 확인
        accountsCommonService.checkUserProfile(user);
        commentRepository.save(tacticComment);

        return CommentResponse.from(tacticComment);
    }

    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request, User user) {

        TacticComment tacticComment = commentRepository.findById(commentId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.Comment_NOT_FOUND));

        if(!tacticComment.getUser().getId().equals(user.getId()))
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        tacticComment.updateComment(request);
        TacticComment updateComment = commentRepository.save(tacticComment);
        return CommentResponse.from(updateComment);

    }

    public void deleteComment(Long commentId, User user) {

        TacticComment tacticComment = commentRepository.findById(commentId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.Comment_NOT_FOUND));

        if(!tacticComment.getUser().getId().equals(user.getId()))
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        commentRepository.deleteById(commentId);
    }

    public ApiResponse<Object> likeButton(Long tacticId, User user) {

        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        Optional<TacticLike> like = likeRepository.findByUserAndTactic(user, tactic);

        if (like.isPresent()) {
            likeRepository.delete(like.get());
            return ApiResponse.CANCELED_LIKE();
        }
        else {
            likeRepository.save(new TacticLike(user, tactic));
            return ApiResponse.SUCCESS_LIKE();
        }
    }
}
