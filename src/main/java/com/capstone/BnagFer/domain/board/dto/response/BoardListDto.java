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
    public static BoardListDto from(Board board) {
        return BoardListDto.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .writerNickName(board.getUser().getProfile().getNickname())
                .boardTitle(board.getBoardTitle())
                .likeCount((long) board.getLikes().size())
                .commentCount((long) board.getComments().size())
                .build();
    }

    public static List<BoardListDto> from(List<Board> boards, List<Long> blockedUserIds) {
        return boards.stream()
                .filter(board -> !blockedUserIds.contains(board.getUser().getId()))
                .map(BoardListDto::from)
                .collect(Collectors.toList());
    }


}


