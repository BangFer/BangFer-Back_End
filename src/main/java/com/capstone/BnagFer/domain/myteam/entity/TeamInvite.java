package com.capstone.BnagFer.domain.myteam.entity;

import com.capstone.BnagFer.domain.accounts.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "myteam_team_invite")
public class TeamInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_user_id")
    private User invitedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus;

    @PrePersist
    protected void onCreate() {
        if(this.invitationStatus == null) {
            this.invitationStatus = InvitationStatus.PENDING;
        }
    }

    public void accept() {
        this.invitationStatus = InvitationStatus.ACCEPTED;
    }

    public void reject() {
        this.invitationStatus = InvitationStatus.REJECTED;
    }
}