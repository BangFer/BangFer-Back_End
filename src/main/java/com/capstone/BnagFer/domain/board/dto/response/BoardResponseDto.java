package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record BoardResponseDto(
        Long id,
        Long writerId,
        String writerNickName,
        String boardTitle,
        String boardContent,
        List<Comment> commentContent,
        int likeCount,
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
                .likeCount(board.getLikes().size())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }
    @Builder
    public record CommentList(
            Long commentId,
            Long boardId,
            Long userId,
            String nickName,
            String commentText,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static CommentList from(Comment comment) {
            return CommentList.builder()
                    .commentId(comment.getId())
                    .userId(comment.getUser().getId())
                    .boardId(comment.getBoard().getId())
                    .nickName(comment.getUser().getProfile().getNickname())
                    .commentText(comment.getCommentText())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }
        public static List<CommentList> from(List<Comment> comments) {
            return comments.stream().map(CommentList::from).collect(Collectors.toList());
        }
    }
    @Builder
    public record BoardList(
            Long id,
            Long userId,
            String writerNickName,
            String boardTitle,
            int likeCount

    ) {
        public static BoardList from(Board board) {
            return BoardList.builder()
                    .id(board.getId())
                    .userId(board.getUser().getId())
                    .writerNickName(board.getUser().getProfile().getNickname())
                    .boardTitle(board.getBoardTitle())
                    .likeCount(board.getLikes().size())
                    .build();
        }
        public static List<BoardList> from(List<Board> boards) {
            return boards.stream().map(BoardList::from).collect(Collectors.toList());
        }
    }

}
