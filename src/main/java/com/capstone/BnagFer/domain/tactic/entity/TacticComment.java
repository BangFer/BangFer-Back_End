package com.capstone.BnagFer.domain.tactic.entity;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.tactic.dto.CommentUpdateRequest;
import com.capstone.BnagFer.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tactic_tacticcomment")
public class TacticComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tactic")
    private Tactic tactic;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "deleted")
    @ColumnDefault("false")
    private boolean deleted;  // true면 삭제된 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TacticComment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<TacticComment> children = new ArrayList<>();

    public void setParent(TacticComment parent) {
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }
        this.parent = parent;
        if (parent != null) {
            parent.getChildren().add(this);
        }
    }
    public void updateComment(CommentUpdateRequest request){
        comment = request.comment();
    }

    public void deleteComment() {
        comment = "삭제된 댓글 입니다.";
        deleted = true;
    }
}
