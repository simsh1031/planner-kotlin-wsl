package com.example.planner.dto.user

data class SignUpRequest(
    val email: String,
    val password: String,
    val nickname: String
)