package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public record BoardResponseDto(
        Long id,
        Long writerId,
        String writerNickName,
        String boardTitle,
        String boardContent,
        int likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    public static BoardResponseDto from(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getUser().getId(),
                board.getUser().getProfile().getNickname(),
                board.getBoardTitle(),
                board.getBoardContent(),
                board.getLikes().size(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
