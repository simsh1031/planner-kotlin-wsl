package com.example.planner.controller

import com.example.planner.dto.common.DeleteResponse
import com.example.planner.dto.user.UpdateUserRequest
import com.example.planner.dto.user.UserResponse
import com.example.planner.service.AdminService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val adminService: AdminService
) {

    // 전체 유저 목록 조회
    @GetMapping("/users")
    fun getAllUsers(httpRequest: HttpServletRequest): List<UserResponse> {
        adminService.checkAdmin(httpRequest)

        return adminService.getAllUsers().map { user ->
            UserResponse(
                userId   = user.userId,
                email    = user.email,
                nickname = user.nickname,
                role     = user.role.name
            )
        }
    }

    // 특정 유저 조회
    @GetMapping("/users/{userId}")
    fun getUser(
        @PathVariable userId: Long,
        httpRequest: HttpServletRequest
    ): UserResponse {
        adminService.checkAdmin(httpRequest)

        val user = adminService.findUser(userId)
        return UserResponse(
            userId   = user.userId,
            email    = user.email,
            nickname = user.nickname,
            role     = user.role.name
        )
    }

    // 특정 유저 정보 수정 (비밀번호 확인 없이 관리자 권한으로 변경)
    @PatchMapping("/users/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody request: UpdateUserRequest,
        httpRequest: HttpServletRequest
    ): UserResponse {
        adminService.checkAdmin(httpRequest)

        val user = adminService.updateUser(
            userId      = userId,
            nickname    = request.nickname,
            newPassword = request.newPassword
        )
        return UserResponse(
            userId   = user.userId,
            email    = user.email,
            nickname = user.nickname,
            role     = user.role.name
        )
    }

    // 특정 유저 삭제 (비밀번호 확인 없이 관리자 권한으로 삭제)
    @DeleteMapping("/users/{userId}")
    fun deleteUser(
        @PathVariable userId: Long,
        httpRequest: HttpServletRequest
    ): DeleteResponse {
        adminService.checkAdmin(httpRequest)
        adminService.deleteUser(userId)
        return DeleteResponse(message = "유저가 삭제되었습니다.")
    }
}