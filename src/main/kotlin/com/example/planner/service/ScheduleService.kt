package com.example.planner.service

import com.example.planner.domain.schedule.Schedule
import com.example.planner.repository.ScheduleRepository
import com.example.planner.repository.TodoRepository
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
    private val todoRepository: TodoRepository
) {

    fun createSchedule(
        userId: Long, title: String, description: String?,
        startDate: LocalDateTime, endDate: LocalDateTime
    ): Schedule {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        return scheduleRepository.save(
            Schedule(title = title, description = description, startDate = startDate, endDate = endDate, user = user)
        )
    }

    fun getSchedules(userId: Long): List<Schedule> {
        return scheduleRepository.findByUserUserId(userId)  // ✅ userId로 필터링
    }

    fun getSchedulesByDate(userId: Long, date: LocalDate): List<Schedule> {
        val start = date.atStartOfDay()
        val end = date.atTime(23, 59, 59)
        return scheduleRepository.findByUserUserIdAndDateOverlapping(userId, start, end)  // ✅
    }

    fun getSchedulesByPeriod(userId: Long, start: LocalDate, end: LocalDate): List<Schedule> {
        val startDateTime = start.atStartOfDay()
        val endDateTime = end.atTime(23, 59, 59)
        return scheduleRepository.findByUserUserIdAndDateOverlapping(userId, startDateTime, endDateTime)  // ✅
    }

    fun deleteSchedule(scheduleId: Long) {
        todoRepository.deleteByScheduleScheduleId(scheduleId)
        scheduleRepository.deleteById(scheduleId)
    }

    fun updateSchedule(
        scheduleId: Long, title: String, description: String?,
        startDate: LocalDateTime, endDate: LocalDateTime
    ): Schedule {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw IllegalArgumentException("Schedule not found")
        }
        scheduleRepository.updateSchedule(scheduleId, title, description, startDate, endDate)
        return scheduleRepository.findById(scheduleId).get()
    }
}