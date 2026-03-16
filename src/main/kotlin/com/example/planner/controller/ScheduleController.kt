package com.example.planner.controller

import com.example.planner.dto.common.DeleteResponse
import com.example.planner.dto.schedule.CreateScheduleRequest
import com.example.planner.dto.schedule.ScheduleResponse
import com.example.planner.dto.schedule.UpdateScheduleRequest
import com.example.planner.service.ScheduleService
import jakarta.servlet.http.HttpServletRequest
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
        @RequestBody request: CreateScheduleRequest,
        httpRequest: HttpServletRequest
    ): ScheduleResponse {
        val userId = httpRequest.getAttribute("userId") as Long

        val schedule = scheduleService.createSchedule(
            userId = userId,
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
        @RequestParam(required = false) end: String?,
        request: HttpServletRequest
    ): List<ScheduleResponse> {
        val userId = request.getAttribute("userId") as Long

        val schedules = when {
            date != null -> scheduleService.getSchedulesByDate(userId, LocalDate.parse(date))
            start != null && end != null -> scheduleService.getSchedulesByPeriod(userId, LocalDate.parse(start), LocalDate.parse(end))
            else -> scheduleService.getSchedules(userId)
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

    @PutMapping("/{scheduleId}")
    fun updateSchedule(
        @PathVariable scheduleId: Long,
        @RequestBody request: UpdateScheduleRequest
    ): ScheduleResponse {
        val schedule = scheduleService.updateSchedule(
            scheduleId = scheduleId,
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

    @DeleteMapping("/{scheduleId}")
    fun deleteSchedule(@PathVariable scheduleId: Long): DeleteResponse {
        scheduleService.deleteSchedule(scheduleId)
        return DeleteResponse(message = "Schedule deleted successfully")
    }
}