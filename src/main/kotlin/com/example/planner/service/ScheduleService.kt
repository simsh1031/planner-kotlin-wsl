package com.example.planner.service

import com.example.planner.domain.schedule.Schedule
import com.example.planner.repository.ScheduleRepository
import com.example.planner.repository.TodoRepository  // ← 추가
import com.example.planner.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
@Transactional
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
    private val userRepository: UserRepository,
    private val todoRepository: TodoRepository  // ← 추가
) {

    fun createSchedule(
        userId: Long,
        title: String,
        description: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Schedule {

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val schedule = Schedule(
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate,
            user = user
        )

        return scheduleRepository.save(schedule)
    }

    fun getSchedules(): List<Schedule> {
        return scheduleRepository.findAll()
    }

    fun getSchedulesByDate(date: LocalDate): List<Schedule> {
        val start = date.atStartOfDay()
        val end = date.atTime(23, 59, 59)

        // 검색 날짜가 일정 기간 안에 포함되는 모든 일정 반환
        return scheduleRepository.findByDateOverlapping(start, end)
    }

    fun getSchedulesByPeriod(start: LocalDate, end: LocalDate): List<Schedule> {
        val startDateTime = start.atStartOfDay()
        val endDateTime = end.atTime(23, 59, 59)

        // 검색 기간과 겹치는 모든 일정 반환
        return scheduleRepository.findByDateOverlapping(startDateTime, endDateTime)
    }

    fun deleteSchedule(scheduleId: Long) {
        todoRepository.deleteByScheduleScheduleId(scheduleId)  // ← Todo 먼저 삭제
        scheduleRepository.deleteById(scheduleId)
    }

    fun updateSchedule(
        scheduleId: Long,
        title: String,
        description: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Schedule {

        // 존재 여부 확인
        if (!scheduleRepository.existsById(scheduleId)) {
            throw IllegalArgumentException("Schedule not found")
        }

        scheduleRepository.updateSchedule(
            scheduleId = scheduleId,
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate
        )

        return scheduleRepository.findById(scheduleId).get()
    }
}