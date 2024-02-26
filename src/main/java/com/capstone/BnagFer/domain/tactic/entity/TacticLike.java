package com.capstone.BnagFer.domain.tactic.entity;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TacticLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community")
    private Tactic tactic;

    public void LikeEntity(User user, Tactic tactic) {
        this.user = user;
        this.tactic = tactic;
    }

}
