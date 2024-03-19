package com.capstone.BnagFer.domain.myteam.entity;


import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.dto.CUTeamRequestDto;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "myteam_team")
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
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User leader;

    //팀 맴버
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamMember> teamMembers;

    //전술
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tactic_id")
    private Tactic tactic;

    //Leader Setter
    public void setLeader(User leader) {
        this.leader = leader;
    }

    public void updateTeam(CUTeamRequestDto updateDTO) { this.teamName = updateDTO.teamName();}
    //캘린더
    @OneToMany(mappedBy = "team")
    private List<CalendarEvent> calendarEvents;
}
