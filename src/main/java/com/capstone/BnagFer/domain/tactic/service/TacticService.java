package com.capstone.BnagFer.domain.tactic.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.tactic.dto.TacticRequest;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
import com.capstone.BnagFer.domain.tactic.repository.TacticRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TacticService {
    private final TacticRepository tacticRepository;
    private final UserJpaRepository userRepository;

    public TacticResponse.tacticDetail createTactic(TacticRequest.CreateDTO request){
        User user = userRepository.findByEmail("kijiwi1@gmail.com").orElseThrow(() -> new TacticExceptionHandler(ErrorCode.USER_NOT_FOUND));
        Tactic tactic = request.toEntity();
        tacticRepository.save(tactic);
        return TacticResponse.tacticDetail.from(tactic);
    }

    public void deleteTactic(Long tacticId) {
        tacticRepository.deleteById(tacticId);
    }

    public TacticResponse.tacticDetail updateTactic(TacticRequest.UpdateDTO request) {
        User user = userRepository.findByEmail("kijiwi1@gmail.com").orElseThrow(() -> new TacticExceptionHandler(ErrorCode.USER_NOT_FOUND));
        Tactic tactic = tacticRepository.findById(user.getId()).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));
        tactic.updateTactic(request);
        Tactic updatedTactic = tacticRepository.save(tactic);
        return TacticResponse.tacticDetail.from(updatedTactic);
    }
}
