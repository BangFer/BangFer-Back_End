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
            Long commentCount

    ) {
        public static BoardList from(Board board, Long commentCount, Long likeCount) {
            return BoardList.builder()
                    .id(board.getId())
                    .userId(board.getUser().getId())
                    .writerNickName(board.getUser().getProfile().getNickname())
                    .boardTitle(board.getBoardTitle())
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .build();
        }
        public static BoardList from(Board board) {
            return from(board, 0L, 0L);
        }
        public static List<BoardList> from(List<Board> boards) {
            return boards.stream().map(BoardList::from).collect(Collectors.toList());
        }
    }
}