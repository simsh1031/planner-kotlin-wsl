package com.example.planner.dto.memo

data class CreateMemoRequest(
    val userId: Long,
    val title: String,
    val content: String
)