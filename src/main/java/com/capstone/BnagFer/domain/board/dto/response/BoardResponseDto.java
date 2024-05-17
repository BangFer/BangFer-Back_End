package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BoardResponseDto(
        Long id,
        Long writerId,
        String writerNickName,
        String boardTitle,
        String boardContent,
        List<Comment> commentContent,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BoardResponseDto from(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .writerId(board.getUser().getId())
                .writerNickName(board.getUser().getProfile().getNickname())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .commentContent(board.getComments())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }
}
