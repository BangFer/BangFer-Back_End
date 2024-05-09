package com.capstone.BnagFer.domain.myteam.repository;

import com.capstone.BnagFer.domain.myteam.entity.CalendarEvent;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamCalendarRepository extends JpaRepository<CalendarEvent, Long> {
    @Query("select c " +
            "from CalendarEvent c " +
            "where c.team = :team")
    List<CalendarEvent> findByTeam(@Param("team") Team team);

    boolean existsByTeamAndId(Team team, Long id);

}
