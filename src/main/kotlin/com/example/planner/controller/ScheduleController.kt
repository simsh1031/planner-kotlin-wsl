package com.example.planner.controller

import com.example.planner.dto.common.DeleteResponse
import com.example.planner.dto.schedule.CreateScheduleRequest
import com.example.planner.dto.schedule.ScheduleResponse
import com.example.planner.service.ScheduleService
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/schedules")
class ScheduleController(
    private val scheduleService: ScheduleService
) {

    @PostMapping
    fun createSchedule(
        @RequestBody request: CreateScheduleRequest
    ): ScheduleResponse {

        val schedule = scheduleService.createSchedule(
            userId = request.userId,
            title = request.title,
            description = request.description,
            startDate = LocalDateTime.parse(request.startDate),
            endDate = LocalDateTime.parse(request.endDate)
        )

        return ScheduleResponse(
            scheduleId = schedule.scheduleId,
            title = schedule.title,
            description = schedule.description,
            startDate = schedule.startDate.toString(),
            endDate = schedule.endDate.toString()
        )
    }

    @GetMapping
    fun getSchedules(
        @RequestParam(required = false) date: String?,
        @RequestParam(required = false) start: String?,
        @RequestParam(required = false) end: String?
    ): List<ScheduleResponse> {

        val schedules = when {

            date != null -> {
                scheduleService.getSchedulesByDate(
                    LocalDate.parse(date)
                )
            }

            start != null && end != null -> {
                scheduleService.getSchedulesByPeriod(
                    LocalDate.parse(start),
                    LocalDate.parse(end)
                )
            }

            else -> scheduleService.getSchedules()
        }

        return schedules.map {
            ScheduleResponse(
                scheduleId = it.scheduleId,
                title = it.title,
                description = it.description,
                startDate = it.startDate.toString(),
                endDate = it.endDate.toString()
            )
        }
    }

    @DeleteMapping("/{scheduleId}")
    fun deleteSchedule(
        @PathVariable scheduleId: Long
    ): DeleteResponse {

        scheduleService.deleteSchedule(scheduleId)

        return DeleteResponse(
            message = "Schedule deleted successfully"
        )
    }
}