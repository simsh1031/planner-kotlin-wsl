package com.example.planner.controller

import com.example.planner.dto.auth.LoginRequest
import com.example.planner.dto.auth.LoginResponse
import com.example.planner.security.JwtUtil
import com.example.planner.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): LoginResponse {

        val user = userService.login(
            request.email,
            request.password
        )

        val token = jwtUtil.generateToken(
            userId = user.userId!!,
            email = user.email
        )

        return LoginResponse(
            accessToken = token,
            tokenType = "Bearer",
            expiresIn = 3600,
            userId = user.userId!!
        )
    }
}