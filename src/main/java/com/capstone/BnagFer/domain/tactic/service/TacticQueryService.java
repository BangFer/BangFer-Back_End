package com.capstone.BnagFer.domain.tactic.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.global.config.AppConfig;
import com.capstone.BnagFer.global.util.RedisUtil;
import com.capstone.BnagFer.domain.tactic.dto.TacticDetailResponse;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
import com.capstone.BnagFer.domain.tactic.repository.TacticRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TacticQueryService {

    private final TacticRepository tacticRepository;
    private final RedisUtil redisUtil;
    private final AppConfig appConfig;

    public Page<TacticResponse.TacticList> getTactics(Pageable pageable) {
        Page<Tactic> tactics = tacticRepository.findAllByAnonymousFalse(pageable);
        return tactics.map(TacticResponse.TacticList::from);
    }

    public Page<TacticResponse.TacticList> getUserTactics(User user, Pageable pageable) {
        Page<Tactic> tactics = tacticRepository.findAllByUser(user, pageable);
        return tactics.map(TacticResponse.TacticList::from);
    }

    public TacticDetailResponse getTacticById(Long tacticId) {
        Tactic tactic = tacticRepository.findById(tacticId).orElseThrow(() -> new TacticExceptionHandler(ErrorCode.TACTIC_NOT_FOUND));

        Long likeCount = redisUtil.getLikeCount(tacticId);

        if (likeCount == null) {
            likeCount = (long) tactic.getLikes().size();
            redisUtil.saveLikeCount(tacticId, likeCount);
        }

        return TacticDetailResponse.from(tactic, likeCount, appConfig.getBaseUrl());
    }
}
