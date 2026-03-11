package com.example.planner.repository

import com.example.planner.domain.schedule.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ScheduleRepository : JpaRepository<Schedule, Long> {

    fun findByStartDate(startDate: LocalDateTime): List<Schedule>

    fun findByStartDateBetween(
        start: LocalDateTime,
        end: LocalDateTime
    ): List<Schedule>
}