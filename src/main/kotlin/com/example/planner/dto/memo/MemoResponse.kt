package com.example.planner.dto.memo

data class MemoResponse(
    val memoId: Long?,
    val userId: Long?,
    val title: String,
    val content: String,
    val createdAt: String
)