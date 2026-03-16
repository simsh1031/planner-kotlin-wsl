package com.example.planner.repository

import com.example.planner.domain.schedule.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.Modifying

interface ScheduleRepository : JpaRepository<Schedule, Long> {

    fun findByStartDate(startDate: LocalDateTime): List<Schedule>

    fun findByStartDateBetween(
        start: LocalDateTime,
        end: LocalDateTime
    ): List<Schedule>

    // 검색 날짜가 일정의 startDate ~ endDate 범위 안에 포함되는 일정 조회
    @Query("SELECT s FROM Schedule s WHERE s.startDate <= :end AND s.endDate >= :start")
    fun findByDateOverlapping(
        start: LocalDateTime,
        end: LocalDateTime
    ): List<Schedule>

    @Modifying
    @Query("""
        UPDATE Schedule s
        SET s.title = :title,
            s.description = :description,
            s.startDate = :startDate,
            s.endDate = :endDate
        WHERE s.scheduleId = :scheduleId
    """)
    fun updateSchedule(
        scheduleId: Long,
        title: String,
        description: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Int  // 수정된 row 수 반환
}