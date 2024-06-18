package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.Board;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record BoardListDto(
        Long id,
        Long userId,
        String writerNickName,
        String boardTitle,
        Long likeCount,
        Long commentCount

) {
    public static BoardListDto from(Board board, Long commentCount, Long likeCount) {
        return BoardListDto.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .writerNickName(board.getUser().getProfile().getNickname())
                .boardTitle(board.getBoardTitle())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }
    public static BoardListDto from(Board board) {
        return from(board, 0L, 0L);
    }
    public static List<BoardListDto> from(List<Board> boards) {
        return boards.stream().map(board -> from(board, 0L, 0L)).collect(Collectors.toList());
    }
}


