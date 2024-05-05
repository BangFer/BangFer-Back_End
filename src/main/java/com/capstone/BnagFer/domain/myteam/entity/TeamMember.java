package com.capstone.BnagFer.domain.myteam.entity;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.TeamMemberRequestDto;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "myteam_teammember")
 public class TeamMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Role role; //LEADER(1) or MEMBER(2)

    @Column(name = "position")
    private Position position;

    public void updateUser(User updateUser) {
        user = updateUser;
    }

    public void updateRole(Role upateRole) {
        role = upateRole;
    }

    public void updateTeam(Team updateTeam) {
        team = updateTeam;
    }

    public void updateUserRoleAndTeam(User updateUser, Team updateTeam) {
        user = updateUser;
        role = Role.LEADER;
        team  = updateTeam;
    }

    public void updatePosition(Position updatePosition) { position = updatePosition; }


    public static TeamMember createTeamMember() {
        return new TeamMember();
    }
}