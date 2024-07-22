package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.BoardBlock;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record BoardBlockResponseDto(
        Long id,
        Long blockUser,
        Long isBlockedUser,
        LocalDateTime blockedAt
) {
    public static BoardBlockResponseDto from(BoardBlock boardBlock) {
        return BoardBlockResponseDto.builder()
                .id(boardBlock.getId())
                .blockUser(boardBlock.getBlockUser().getId())
                .isBlockedUser(boardBlock.getIsBlockedUser().getId())
                .blockedAt(boardBlock.getBlockedAt())
                .build();
    }
}
