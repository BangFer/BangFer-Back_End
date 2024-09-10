package com.capstone.BnagFer.domain.tactic.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.dto.response.BoardListDto;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.repository.BoardBlockRepository;
import com.capstone.BnagFer.global.util.RedisUtil;
import com.capstone.BnagFer.domain.tactic.dto.TacticDetailResponse;
import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
import com.capstone.BnagFer.domain.tactic.repository.TacticRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TacticQueryService {

    private final TacticRepository tacticRepository;
    private final RedisUtil redisUtil;
    private final BoardBlockRepository boardBlockRepository;

    public Page<TacticDetailResponse.AllTacticList> getTactics(User user, Pageable pageable) {
        Page<Tactic> tactics = tacticRepository.findAllByAnonymousFalse(pageable);

        List<Long> blockedUserIds = boardBlockRepository.findIsBlockUserIdsByBlockUserId(user.getId());

        Map<Long, Long> likeCounts = new HashMap<>();
        Map<Long, Long> commentCounts = new HashMap<>();

        List<TacticDetailResponse.AllTacticList> filteredTactics = tactics.getContent().stream()
                .filter(tactic -> !blockedUserIds.contains(tactic.getUser().getId()) || tactic.getUser().getId().equals(user.getId()))
                .map(tactic -> {
                    Long likeCount = redisUtil.getLikeCount(tactic.getTacticId());
                    if (likeCount == null) {
                        likeCount = (long) tactic.getLikes().size();
                        redisUtil.saveLikeCount(tactic.getTacticId(), likeCount);
                    }
                    likeCounts.put(tactic.getTacticId(), likeCount);

                    Long commentCount = redisUtil.getCommentCount(tactic.getTacticId());
                    if (commentCount == null) {
                        commentCount = (long) tactic.getComments().size();
                        redisUtil.saveCommentCount(tactic.getTacticId(), commentCount);
                    }
                    commentCounts.put(tactic.getTacticId(), commentCount);

                    return TacticDetailResponse.AllTacticList.from(tactic, likeCount, commentCount);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(filteredTactics, pageable, tactics.getTotalElements());
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

        Long commentCount = redisUtil.getCommentCount(tacticId);

        if(commentCount == null){
            commentCount = (long) tactic.getComments().size();
            redisUtil.saveCommentCount(tacticId, commentCount);
        }

        return TacticDetailResponse.from(tactic, likeCount, commentCount);
    }

    public Page<TacticResponse.TacticList> searchByTitle(String title, User user, Pageable pageable) {
        if (title == null) title = "";

        Page<Tactic> byTitleContaining = tacticRepository.findByTacticNameContainingAndAnonymousFalse(title, pageable);

        List<Long> blockedUserIds = boardBlockRepository.findIsBlockUserIdsByBlockUserId(user.getId());

        List<TacticResponse.TacticList> filteredBoards = byTitleContaining.getContent().stream()
                .filter(tactic -> !blockedUserIds.contains(tactic.getUser().getId()) || tactic.getUser().getId().equals(user.getId()))
                .map(TacticResponse.TacticList::from)
                .toList();

        return new PageImpl<>(filteredBoards, pageable, byTitleContaining.getTotalElements());
    }
}
