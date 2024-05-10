package com.capstone.BnagFer.domain.myteam.entity;

import com.capstone.BnagFer.domain.myteam.dto.request.UpdateTeamCalendarRequestDto;
import com.capstone.BnagFer.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "myteam_calendarevent")
public class CalendarEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "event_info", nullable = false)
    private String matchInfo;

    @Column(name="event_date", nullable = false)
    private LocalDate matchDate;

    public void updateMatchInfo(UpdateTeamCalendarRequestDto updatedMatchInfo) {
        matchInfo = updatedMatchInfo.getMatchInfo();
    }
}
