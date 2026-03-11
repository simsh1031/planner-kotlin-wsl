package com.example.planner.dto.schedule

data class ScheduleResponse(
    val scheduleId: Long?,
    val title: String,
    val description: String?,
    val startDate: String,
    val endDate: String
)