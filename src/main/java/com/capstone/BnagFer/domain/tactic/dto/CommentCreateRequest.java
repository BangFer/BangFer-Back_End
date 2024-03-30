package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @NotBlank(message = "[ERROR] 댓글내용은 필수입니다.")
        String comment
) {
        public TacticComment toEntity(User user, Tactic tactic){
                return TacticComment.builder()
                        .comment(comment)
                        .user(user)
                        .tactic(tactic)
                        .build();
        }
}
