package com.capstone.BnagFer.domain.board.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.board.dto.request.BoardBlockRequestDto;
import com.capstone.BnagFer.domain.board.dto.response.BoardBlockResponseDto;
import com.capstone.BnagFer.domain.board.entity.BoardBlock;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.board.repository.BoardBlockRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardBlockService {
    private final BoardBlockRepository boardBlockRepository;
    private final UserJpaRepository userRepository;

    public BoardBlockResponseDto blockUser(User blockUser, Long isBlockedUserId) {
        User blockedUser = checkIfIsBlocked(isBlockedUserId);
        validateSelfAction(blockUser, isBlockedUserId, ErrorCode.CANNOT_REPORT_YOURSELF);
        if(boardBlockRepository.existsByBlockUserAndIsBlockedUser(blockUser, blockedUser)){
            throw new BoardExceptionHandler(ErrorCode.ALREADY_BLOCKED);
        }
        BoardBlockRequestDto request = new BoardBlockRequestDto();
        BoardBlock boardBlock = request.toEntity(blockUser, blockedUser);
        boardBlockRepository.save(boardBlock);
        return BoardBlockResponseDto.from(boardBlock);
    }

    public void unblockUser(User blockUser, Long isBlockedUserId) {
        User blockedUser = checkIfIsBlocked(isBlockedUserId);
        validateSelfAction(blockUser, isBlockedUserId, ErrorCode.CANNOT_UNBLOCK_YOURSELF);
        boardBlockRepository.deleteByBlockUserAndIsBlockedUser(blockUser, blockedUser);
    }

    private User checkIfIsBlocked(Long isBlockedUserId) {
        return userRepository.findById(isBlockedUserId)
                .orElseThrow(() -> new BoardExceptionHandler(ErrorCode.USER_NOT_FOUND));
    }

    private void validateSelfAction(User blockUser, Long isBlockedUserId, ErrorCode errorCode){
        if (blockUser.getId().equals(isBlockedUserId)) {
            throw new BoardExceptionHandler(errorCode);
        }
    }


}
