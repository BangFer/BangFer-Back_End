package com.capstone.BnagFer.domain.tactic.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.accounts.service.AccountsServiceUtils;
import com.capstone.BnagFer.domain.tactic.dto.TacticRequest;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
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
@Transactional(readOnly = true)
public class TacticService {
    private final TacticRepository tacticRepository;
    private final UserJpaRepository userRepository;
    private final AccountsServiceUtils accountsServiceUtils;

    public TacticResponse createTactic(TacticRequest.CreateDTO request){
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = request.toEntity();
        tacticRepository.save(tactic);
        return TacticResponse.from(tactic);
    }

    public void deleteTactic(Long tacticId) {
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = tacticRepository.findById(user.getId()).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        if(tactic.getUser() != user)
            throw new TacticExceptionHandler(ErrorCode.UPDATE_AUTHORIZED_ERROR);

        tacticRepository.deleteById(tacticId);
    }

    public TacticResponse updateTactic(TacticRequest.UpdateDTO request) {
        User user = accountsServiceUtils.getCurrentUser();
        Tactic tactic = tacticRepository.findById(user.getId()).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        if(tactic.getUser() != user)
            throw new TacticExceptionHandler(ErrorCode.UPDATE_AUTHORIZED_ERROR);

        tactic.updateTactic(request);
        Tactic updatedTactic = tacticRepository.save(tactic);
        return TacticResponse.from(updatedTactic);
    }

    public List<TacticResponse.TacticList> getTactics() {
        List<Tactic> tactics = tacticRepository.findAll();
        return TacticResponse.TacticList.from(tactics);
    }

    public TacticResponse getTacticById(Long tacticId) {
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));
        return TacticResponse.from(tactic);
    }
}
