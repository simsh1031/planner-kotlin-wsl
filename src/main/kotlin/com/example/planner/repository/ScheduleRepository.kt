package com.example.planner.repository

import com.example.planner.domain.schedule.Schedule
import com.example.planner.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface ScheduleRepository : JpaRepository<Schedule, Long> {

    fun findByUserUserId(userId: Long): List<Schedule>  // ✅ 추가

    @Query("SELECT s FROM Schedule s WHERE s.user.userId = :userId AND s.startDate <= :end AND s.endDate >= :start")
    fun findByUserUserIdAndDateOverlapping(  // ✅ 추가
        userId: Long,
        start: LocalDateTime,
        end: LocalDateTime
    ): List<Schedule>

    @Modifying
    @Query("""
        UPDATE Schedule s
        SET s.title = :title, s.description = :description,
            s.startDate = :startDate, s.endDate = :endDate
        WHERE s.scheduleId = :scheduleId
    """)
    fun updateSchedule(
        scheduleId: Long, title: String, description: String?,
        startDate: LocalDateTime, endDate: LocalDateTime
    ): Int

    fun deleteAllByUser(user: User)

    fun findAllByUser(user: User): List<Schedule>
}