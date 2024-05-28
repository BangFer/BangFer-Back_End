package com.capstone.BnagFer.domain.tactic.dto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @NotBlank(message = "[ERROR] 댓글내용은 필수입니다.")
        @Schema(description = "comment", example = "전술이 너무 맘에 들어요!")
        String comment
) {
        public TacticComment toEntity(User user, Tactic tactic, TacticComment parent){
                TacticComment tacticComment = TacticComment.builder()
                        .comment(comment)
                        .user(user)
                        .tactic(tactic)
                        .build();
                if (parent != null) {
                        tacticComment.setParent(parent);
                }
                return tacticComment;
        }
}
