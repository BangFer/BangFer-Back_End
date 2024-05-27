package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record BoardResponseDto(
        Long id,
        Long writerId,
        String writerNickName,
        String boardTitle,
        String boardContent,
        int likeCount,
        int commentCount,
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
                board.getComments().size(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
    @Builder
    public record BoardList(
            Long id,
            Long userId,
            String writerNickName,
            String boardTitle,
            Long likeCount,
            int commentCount

    ) {
        public static BoardList from(Board board) {
            return BoardList.builder()
                    .id(board.getId())
                    .userId(board.getUser().getId())
                    .writerNickName(board.getUser().getProfile().getNickname())
                    .boardTitle(board.getBoardTitle())
                    .likeCount((long) board.getLikes().size())
                    .commentCount(board.getComments().size())
                    .build();
        }
        public static List<BoardList> from(List<Board> boards) {
            return boards.stream().map(BoardList::from).collect(Collectors.toList());
        }
    }
}