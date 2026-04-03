package com.example.planner.dto.schedule

data class CreateScheduleRequest(
    val userId: Long,
    val title: String,
    val description: String?,
    val startDate: String,
    val endDate: String
)