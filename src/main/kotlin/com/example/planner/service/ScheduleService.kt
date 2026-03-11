package com.example.planner.service

import com.example.planner.domain.schedule.Schedule
import com.example.planner.repository.ScheduleRepository
import com.example.planner.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
@Transactional
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
    private val userRepository: UserRepository
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

        return scheduleRepository.findByStartDateBetween(start, end)
    }

    fun getSchedulesByPeriod(start: LocalDate, end: LocalDate): List<Schedule> {

        val startDateTime = start.atStartOfDay()
        val endDateTime = end.atTime(23, 59, 59)

        return scheduleRepository.findByStartDateBetween(
            startDateTime,
            endDateTime
        )
    }

    fun deleteSchedule(scheduleId: Long) {
        scheduleRepository.deleteById(scheduleId)
    }
}