package com.example.planner.dto.todo

data class CreateTodoRequest(
    val scheduleId: Long,
    val content: String
)