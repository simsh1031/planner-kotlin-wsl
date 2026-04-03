package com.example.planner.controller

import com.example.planner.dto.user.SignUpRequest
import com.example.planner.dto.user.UserResponse
import com.example.planner.service.UserService
import org.springframework.web.bind.annotation.*
import com.example.planner.dto.common.DeleteResponse
import com.example.planner.dto.user.DeleteUserRequest
import com.example.planner.dto.user.UpdateUserRequest
import jakarta.servlet.http.HttpServletRequest

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

    // PATCH /api/users/{userId} — 회원정보 수정
    @PatchMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody request: UpdateUserRequest,
        httpRequest: HttpServletRequest
    ): UserResponse {
        val tokenUserId = httpRequest.getAttribute("userId").toString().toLong()

        if (tokenUserId != userId) {
            throw IllegalArgumentException("본인 정보만 수정할 수 있습니다.")
        }

        val user = userService.updateUser(
            userId = userId,
            nickname = request.nickname,
            currentPassword = request.currentPassword,
            newPassword = request.newPassword
        )

        return UserResponse(
            userId = user.userId,
            email = user.email,
            nickname = user.nickname,
            role = user.role.name
        )
    }

    // DELETE /api/users/{userId} — 회원탈퇴
    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable userId: Long,
        @RequestHeader("X-Password") password: String,  // ← body 대신 header
        httpRequest: HttpServletRequest
    ): DeleteResponse {
        val tokenUserId = httpRequest.getAttribute("userId").toString().toLong()

        if (tokenUserId != userId) {
            throw IllegalArgumentException("본인 계정만 탈퇴할 수 있습니다.")
        }

        userService.deleteUser(userId, password)

        return DeleteResponse(message = "회원탈퇴가 완료되었습니다.")
    }
}