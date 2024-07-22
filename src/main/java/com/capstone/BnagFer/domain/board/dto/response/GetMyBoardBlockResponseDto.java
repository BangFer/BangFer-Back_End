package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.BoardBlock;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record GetMyBoardBlockResponseDto(
        Long id,
        Long isBlockedUser,
        String nickName,
        LocalDateTime blockedAt
) {
    public static GetMyBoardBlockResponseDto from(BoardBlock boardBlock) {
        return GetMyBoardBlockResponseDto.builder()
                .id(boardBlock.getId())
                .isBlockedUser(boardBlock.getIsBlockedUser().getId())
                .nickName(boardBlock.getIsBlockedUser().getProfile().getNickname())
                .blockedAt(boardBlock.getBlockedAt())
                .build();
    }
}
