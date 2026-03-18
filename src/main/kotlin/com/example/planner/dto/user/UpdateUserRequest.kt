package com.example.planner.dto.user

data class UpdateUserRequest(
    val nickname: String?,           // null이면 변경 안 함
    val currentPassword: String?,    // 비밀번호 변경 시 현재 비번 확인용
    val newPassword: String?         // null이면 변경 안 함
)