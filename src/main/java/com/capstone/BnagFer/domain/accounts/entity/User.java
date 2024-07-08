package com.capstone.BnagFer.domain.accounts.entity;

import com.capstone.BnagFer.domain.accounts.dto.account.ChangeEmailRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.profile.UpdateProfileRequestDto;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
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

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_staff", nullable = false)
    @ColumnDefault("false")
    private Boolean isStaff;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @Column(length = 100) // provider 추가 (kakao)
    private String provider;

    @Column(name = "deleted")
    @ColumnDefault("false")
    private Boolean deleted; // Soft delete 를 위한 필드

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // Soft delete된 회원의 삭제 시간을 저장하는 필드

    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL)
    private List<Team> team;

    @OneToMany(mappedBy = "user")
    private List<Tactic> tactics;

    @OneToMany(mappedBy = "user")
    private List<Board> boards;
    //신고 상태을 위한 Enum 컬럼
    @Enumerated(EnumType.STRING)
    private UserActivity userActivity;

    @PrePersist
    protected void onCreate() {
        if(this.userActivity == null) {
            this.userActivity = UserActivity.NORMAL;
        }
    }

    public void updatePassword(String pw) {
        password = pw;
    }

    public void updateUser(UpdateProfileRequestDto requestDto) {
        name = requestDto.name();
    }

    public void softDelete() {
        deleted = true;
        deletedAt = LocalDateTime.now();
    }
    public void recoverDelete() {
        deleted = false;
        deletedAt = null;
    }

    public void changeActivity(UserActivity userActivity) {
        this.userActivity = userActivity;
    }

    public void grantStaffAuthority() {
        this.isStaff = Boolean.TRUE;
    }

    public void revokeStaffAuthority() {
        this.isStaff = Boolean.FALSE;
    }

    public void updateEmail(ChangeEmailRequestDto requestDto) {
        email = requestDto.newEmail();
    }

}