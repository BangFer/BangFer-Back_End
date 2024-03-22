package com.capstone.BnagFer.domain.tactic.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
import com.capstone.BnagFer.domain.tactic.dto.TacticCreateRequest;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.dto.TacticUpdateRequest;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
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
        tactic.setAttackDetails(request.attackDetails());
        Tactic updatedTactic = tacticRepository.save(tactic);
        return TacticResponse.from(updatedTactic);
    }
}
