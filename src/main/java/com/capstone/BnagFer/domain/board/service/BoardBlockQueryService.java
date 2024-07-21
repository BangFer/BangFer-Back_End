package com.capstone.BnagFer.domain.board.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.dto.response.GetMyBoardBlockResponseDto;
import com.capstone.BnagFer.domain.board.entity.BoardBlock;
import com.capstone.BnagFer.domain.board.repository.BoardBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardBlockQueryService {
    private final BoardBlockRepository boardBlockRepository;
    public List<GetMyBoardBlockResponseDto> getBlockedUsers(User user) {
        List<BoardBlock> blockedUsers = boardBlockRepository.findByBlockUser(user);
        return blockedUsers.stream()
                .map(GetMyBoardBlockResponseDto::from)
                .collect(Collectors.toList());
    }

}
