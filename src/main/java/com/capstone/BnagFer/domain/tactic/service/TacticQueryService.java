package com.capstone.BnagFer.domain.tactic.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.jwt.util.RedisUtil;
import com.capstone.BnagFer.domain.tactic.dto.TacticDetailResponse;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
import com.capstone.BnagFer.domain.tactic.repository.TacticRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TacticQueryService {

    private final TacticRepository tacticRepository;
    private final RedisUtil redisUtil;

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

        String redisKey = "tactic:likes:" + tacticId;
        Long likeCount = redisUtil.getLikes(redisKey);

        if (likeCount == null) {
            // Redis에 좋아요 개수가 없으면 데이터베이스에서 가져와 Redis에 저장
            likeCount = (long) tactic.getLikes().size();
            redisUtil.save(redisKey, likeCount, 30L, TimeUnit.DAYS);
        }

        return TacticDetailResponse.from(tactic, likeCount);
    }



}
