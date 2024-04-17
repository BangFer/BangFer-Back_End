package com.capstone.BnagFer.domain.tactic.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
import com.capstone.BnagFer.domain.tactic.dto.*;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import com.capstone.BnagFer.domain.tactic.entity.TacticLike;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
import com.capstone.BnagFer.domain.tactic.repository.CommentRepository;
import com.capstone.BnagFer.domain.tactic.repository.LikeRepository;
import com.capstone.BnagFer.domain.tactic.repository.TacticRepository;
import com.capstone.BnagFer.global.common.ApiResponse;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.capstone.BnagFer.global.common.ApiResponse;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class TacticService {
    private final TacticRepository tacticRepository;
    private final AccountsServiceUtils accountsServiceUtils;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public TacticResponse createTactic(TacticCreateRequest request){
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = request.toEntity(user);

        if(user.getProfile() != null){
            tacticRepository.save(tactic);
        }else{
            throw new TacticExceptionHandler(ErrorCode.PROFILE_NOT_EXIST);
        }

        return TacticResponse.from(tactic);
    }

    public void deleteTactic(Long tacticId) {
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        if(tactic.getUser() != user)
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        tacticRepository.deleteById(tacticId);
    }

    public TacticResponse updateTactic(Long tacticId, TacticUpdateRequest request) {
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        if(tactic.getUser() != user)
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        tactic.setTacticName(request.tacticName());
        tactic.setUser(user);
        tactic.setAnonymous(request.anonymous());
        tactic.setFamousCoachName(request.famousCoachName());
        tactic.setMainFormation(request.mainFormation());
        tactic.setAttackFormation(request.attackFormation());
        tactic.setDefenseFormation(request.defenseFormation());
        tactic.setTacticDetails(request.tacticDetails());
        tactic.setAttackDetails(request.attackDetails());
        tactic.setDefenseDetails(request.attackDetails());
        Tactic updatedTactic = tacticRepository.save(tactic);
        return TacticResponse.from(updatedTactic);
    }

    public TacticResponse copyTactic(Long tacticId) {
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        if(tactic.getUser() == user)
            throw new TacticExceptionHandler(ErrorCode.CANNOT_COPY_MYSELF);

        Tactic copyTactic = new Tactic();
        copyTactic.setCopyDetail(user, tactic);

        if(user.getProfile() != null){
            tacticRepository.save(copyTactic);
        }else{
            throw new TacticExceptionHandler(ErrorCode.PROFILE_NOT_EXIST);
        }
        return TacticResponse.from(copyTactic);
    }

    public CommentResponse createComment(Long tacticId, CommentCreateRequest request){
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        TacticComment tacticComment = request.toEntity(user, tactic);

        if(user.getProfile() != null){
            commentRepository.save(tacticComment);
        }else{
            throw new TacticExceptionHandler(ErrorCode.PROFILE_NOT_EXIST);
        }

        return CommentResponse.from(tacticComment);
    }

    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request) {
        User user = accountsServiceUtils.getCurrentUser();
        TacticComment tacticComment = commentRepository.findById(commentId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.Comment_NOT_FOUND));

        if(tacticComment.getUser() != user)
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        tacticComment.setUser(user);
        tacticComment.setComment(request.comment());
        TacticComment updateComment = commentRepository.save(tacticComment);
        return CommentResponse.from(updateComment);

    }

    public void deleteComment(Long commentId) {
        User user = accountsServiceUtils.getCurrentUser();
        TacticComment tacticComment = commentRepository.findById(commentId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.Comment_NOT_FOUND));

        if(tacticComment.getUser() != user)
            throw new TacticExceptionHandler(ErrorCode.USER_NOT_MATCHED);

        commentRepository.deleteById(commentId);
    }

    public ApiResponse<Object> likeButton(Long tacticId) {
        User user = accountsServiceUtils.getCurrentUser();
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
