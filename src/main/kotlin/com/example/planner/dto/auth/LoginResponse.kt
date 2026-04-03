package com.example.planner.dto.auth

data class LoginResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val userId: Long
)