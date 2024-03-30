package com.capstone.BnagFer.domain.tactic.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
import com.capstone.BnagFer.domain.tactic.dto.*;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
import com.capstone.BnagFer.domain.tactic.repository.CommentRepository;
import com.capstone.BnagFer.domain.tactic.repository.TacticRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TacticService {
    private final TacticRepository tacticRepository;
    private final AccountsServiceUtils accountsServiceUtils;
    private final CommentRepository commentRepository;

    public TacticResponse createTactic(TacticCreateRequest request){
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = request.toEntity(user);
        tacticRepository.save(tactic);
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

    public CommentResponse createComment(Long tacticId, CommentCreateRequest request){
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        TacticComment tacticComment = request.toEntity(user, tactic);
        commentRepository.save(tacticComment);

        return CommentResponse.from(tacticComment);
    }

    public CommentResponse updateComment(Long tacticId, Long commentId, CommentUpdateRequest request) {
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));
        TacticComment tacticComment = commentRepository.findById(commentId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.Comment_NOT_FOUND));

        if(tacticComment.getUser() != user || tacticComment.getTactic() != tactic)
            throw new TacticExceptionHandler(ErrorCode.USERANDTACTIC_NOT_MATCHED);

        tacticComment.setUser(user);
        tacticComment.setTactic(tactic);
        tacticComment.setComment(request.comment());
        TacticComment updateComment = commentRepository.save(tacticComment);
        return CommentResponse.from(updateComment);

    }

    public void deleteComment(Long tacticId, Long commentId) {
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));
        TacticComment tacticComment = commentRepository.findById(commentId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.Comment_NOT_FOUND));

        if(tacticComment.getUser() != user || tacticComment.getTactic() != tactic)
            throw new TacticExceptionHandler(ErrorCode.USERANDTACTIC_NOT_MATCHED);

        commentRepository.deleteById(commentId);
    }
}
