package com.example.planner.dto.schedule

data class UpdateScheduleRequest(
    val title: String,
    val description: String?,
    val startDate: String,
    val endDate: String
)