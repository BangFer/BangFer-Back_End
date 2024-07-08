package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record BoardDetailResponseDto(
        Long id,
        Long writerId,
        String writerNickName,
        String boardTitle,
        String boardContent,
        List<CommentList> commentList,
        Long likeCount,
        Long commentCount
) {
    public static BoardDetailResponseDto from(Board board, Long likeCount, Long commentCount) {
        return BoardDetailResponseDto.builder()
                .id(board.getId())
                .writerId(board.getUser().getId())
                .writerNickName(board.getUser().getProfile().getNickname())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .commentList(CommentList.from(board.getComments()))
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }

    public static BoardDetailResponseDto from(Board board) {
        return BoardDetailResponseDto.builder()
                .id(board.getId())
                .writerId(board.getUser().getId())
                .writerNickName(board.getUser().getProfile().getNickname())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
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
            LocalDateTime updatedAt,
            List<CommentList> children
    ) {
        public static CommentList from(Comment comment) {
            return CommentList.builder()
                    .commentId(comment.getCommentId())
                    .userId(comment.getUser().getId())
                    .boardId(comment.getBoard().getId())
                    .nickName(comment.getUser().getProfile().getNickname())
                    .commentText(comment.getCommentText())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .children(comment.getChildren().stream().map(CommentList::from).collect(Collectors.toList()))
                    //최상위 댓글로만 자식 댓글이 달릴 수 있게!
                    .build();
        }
        public static List<CommentList> from(List<Comment> comments) {
            return comments.stream().filter(c -> c.getParent() == null).map(CommentList::from).collect(Collectors.toList());
        }
    }

}
