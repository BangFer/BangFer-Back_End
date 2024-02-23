package com.capstone.BnagFer.domain.accounts.myteam.entity;

import com.capstone.BnagFer.domain.accounts.User;
import com.capstone.BnagFer.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class Team extends BaseEntity {
    //팀 id 값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", nullable = false)
    private Long id;

    //팀 이름
    @Column(name = "team_name", nullable = false)
    private String teamName;

    //방장 id 값
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User leader_id;

    //팀 맴버
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamMember> teamMembers;

    //전술
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tactic_id")
    private Tactic tactic;

    //캘린더
    @OneToMany(mappedBy = "team")
    private List<CalendarEvent> calendarEvents;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}
