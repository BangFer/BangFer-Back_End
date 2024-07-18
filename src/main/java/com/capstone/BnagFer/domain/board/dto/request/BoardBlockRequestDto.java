package com.capstone.BnagFer.domain.board.dto.request;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.entity.BoardBlock;

public record BoardBlockRequestDto() {
    public BoardBlock toEntity(User blockUser, User isBlockedUser) {
        return BoardBlock.builder()
                .blockUser(blockUser)
                .isBlockedUser(isBlockedUser)
                .build();
    }
}
