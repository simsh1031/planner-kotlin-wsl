package com.example.planner.config

import com.example.planner.domain.user.Role
import com.example.planner.domain.user.User
import com.example.planner.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DataInitializer(
    private val userRepository: UserRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        if (userRepository.findByEmail("admin@admin.com") == null) {
            val admin = User(
                email     = "admin@admin.com",
                password  = "pass",
                nickname  = "admin",
                role      = Role.ADMIN,
                createdAt = LocalDateTime.now()
            )
            userRepository.save(admin)
            println("[DataInitializer] 관리자 계정 생성 완료: admin@admin.com")
        }
    }
}