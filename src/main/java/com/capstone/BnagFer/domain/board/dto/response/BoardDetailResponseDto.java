package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.entity.BoardImage;
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
        List<BoardImageList> images,
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
                .images(BoardImageList.from(board.getImages()))
                .commentList(CommentList.from(board.getComments()))
                .likeCount(likeCount)
                .commentCount(commentCount)
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
                    .children(comment.getChildren().stream().map(CommentList::from).toList())
                    //최상위 댓글로만 자식 댓글이 달릴 수 있게!
                    .build();
        }

        public static List<CommentList> from(List<Comment> comments) {
            return comments.stream().filter(c -> c.getParent() == null).map(CommentList::from).toList();
        }
    }

    @Builder
    public record BoardImageList(
            Long imageId,
            String boardImageUrl
    ){
        public static BoardImageList from(BoardImage image) {
            return BoardImageList.builder()
                    .imageId(image.getId())
                    .boardImageUrl(image.getImageUrl())
                    .build();
        }

        public static List<BoardImageList> from(List<BoardImage> images) {
            return images.stream()
                    .map(BoardImageList::from)
                    .toList();
        }
    }
}
