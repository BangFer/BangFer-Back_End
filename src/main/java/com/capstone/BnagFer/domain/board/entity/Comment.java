package com.capstone.BnagFer.domain.board.entity;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.dto.request.UpdateCommentRequestDto;
import com.capstone.BnagFer.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "board_comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // 댓글 작성자
    private User user;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Board board;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @Column(name = "deleted")
    @ColumnDefault("false")
    private boolean deleted;  // true면 삭제된 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public void setParent(Comment parent) {
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }
        this.parent = parent;
        if (parent != null) {
            parent.getChildren().add(this);
        }
    }
    public void updateComment(UpdateCommentRequestDto request){
        commentText = request.commentText();
    }

    public void deleteComment() {
        commentText = "삭제된 댓글 입니다.";
        deleted = true;
    }
}
