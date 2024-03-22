package com.capstone.BnagFer.domain.tactic.entity;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
}
