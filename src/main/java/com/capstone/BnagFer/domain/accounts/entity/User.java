package com.capstone.BnagFer.domain.accounts.entity;

import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import com.capstone.BnagFer.domain.tactic.entity.TacticLike;
import com.capstone.BnagFer.global.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "accounts_user")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name; // 사용자 이름

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_staff", nullable = false)
    @ColumnDefault("false")
    private Boolean isStaff;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @Column(length = 100) // provider 추가 (kakao)
    private String provider;

    @OneToMany(mappedBy = "leader_id")
    private List<Team> team;

    @OneToMany(mappedBy = "user")
    private List<TeamMember> teamMember;

    @OneToMany(mappedBy = "user")
    private List<Tactic> tactics;

    @OneToMany(mappedBy = "user")
    private List<TacticComment> tacticComments;

    @OneToMany(mappedBy = "user")
    private List<TacticLike> tacticLikes;

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
