package com.example.planner.controller

import com.example.planner.dto.user.SignUpRequest
import com.example.planner.dto.user.UserResponse
import com.example.planner.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun signUp(
        @RequestBody request: SignUpRequest
    ): UserResponse {

        val user = userService.signUp(
            email = request.email,
            password = request.password,
            nickname = request.nickname
        )

        return UserResponse(
            userId = user.userId,
            email = user.email,
            nickname = user.nickname,
            role = user.role.name
        )
    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: Long
    ): UserResponse {

        val user = userService.findUser(userId)

        return UserResponse(
            userId = user.userId,
            email = user.email,
            nickname = user.nickname,
            role = user.role.name
        )
    }
}