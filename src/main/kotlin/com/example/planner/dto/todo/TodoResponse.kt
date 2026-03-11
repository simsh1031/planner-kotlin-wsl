package com.example.planner.dto.todo

data class TodoResponse(
    val todoId: Long?,
    val scheduleId: Long,
    val content: String,
    val completed: Boolean
)