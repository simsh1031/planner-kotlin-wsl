package com.example.planner.dto.user

data class UserResponse(
    val userId: Long?,
    val email: String,
    val nickname: String,
    val role: String
)