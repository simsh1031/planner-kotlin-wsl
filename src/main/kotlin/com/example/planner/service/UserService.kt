package com.example.planner.service

import com.example.planner.domain.user.Role
import com.example.planner.domain.user.User
import com.example.planner.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {

    fun signUp(email: String, password: String, nickname: String): User {

        val user = User(
            email = email,
            password = password,
            nickname = nickname,
            role = Role.USER,
            createdAt = LocalDateTime.now()
        )

        return userRepository.save(user)
    }

    fun findUser(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }
    }

    fun login(email: String, password: String): User {

        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Invalid email or password")

        if (user.password != password) {
            throw IllegalArgumentException("Invalid email or password")
        }

        return user
    }
}