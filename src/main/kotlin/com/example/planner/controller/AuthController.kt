package com.example.planner.controller

import com.example.planner.dto.auth.LoginRequest
import com.example.planner.dto.auth.LoginResponse
import com.example.planner.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): LoginResponse {

        val user = userService.login(
            request.email,
            request.password
        )

        val token = "jwt-token"

        return LoginResponse(
            accessToken = token,
            tokenType = "Bearer",
            expiresIn = 3600
        )
    }
}