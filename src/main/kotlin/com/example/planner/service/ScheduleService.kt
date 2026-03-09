package com.example.planner.service

import com.example.planner.domain.schedule.Schedule
import com.example.planner.repository.ScheduleRepository
import com.example.planner.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
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

    fun deleteSchedule(scheduleId: Long) {
        scheduleRepository.deleteById(scheduleId)
    }
}