package com.capstone.BnagFer.domain.myteam.repository;

import com.capstone.BnagFer.domain.myteam.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamCalendarRepository extends JpaRepository<CalendarEvent, Long> {

}
