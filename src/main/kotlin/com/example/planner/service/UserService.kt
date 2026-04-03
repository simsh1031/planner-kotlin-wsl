package com.example.planner.service

import com.example.planner.common.exception.AuthException
import com.example.planner.domain.user.Role
import com.example.planner.domain.user.User
import com.example.planner.repository.MemoRepository
import com.example.planner.repository.MemoTagRepository
import com.example.planner.repository.ScheduleRepository
import com.example.planner.repository.TodoRepository
import com.example.planner.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val memoRepository: MemoRepository,
    private val memoTagRepository: MemoTagRepository,
    private val scheduleRepository: ScheduleRepository,
    private val todoRepository: TodoRepository
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
            ?: throw AuthException("이메일 또는 비밀번호가 올바르지 않습니다.")  // 401

        if (user.password != password) {
            throw AuthException("이메일 또는 비밀번호가 올바르지 않습니다.")     // 401
        }

        return user
    }

    fun updateUser(
        userId: Long,
        nickname: String?,
        currentPassword: String?,
        newPassword: String?
    ): User {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (newPassword != null) {
            if (currentPassword == null || user.password != currentPassword) {
                throw IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.")
            }
            user.password = newPassword
        }

        if (nickname != null) {
            user.nickname = nickname
        }

        return userRepository.save(user)
    }

    fun deleteUser(userId: Long, password: String) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (user.password != password) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        // 1. Todo 삭제 (Schedule 참조)
        val schedules = scheduleRepository.findAllByUser(user)
        if (schedules.isNotEmpty()) {
            todoRepository.deleteAllByScheduleIn(schedules)
        }

        // 2. MemoTag 삭제 (Memo 참조)
        val memos = memoRepository.findByUserUserId(userId)
        memos.forEach { memo ->
            memoTagRepository.deleteByMemoMemoId(memo.memoId!!)
        }

        // 3. Memo 삭제
        memoRepository.deleteAllByUser(user)

        // 4. Schedule 삭제
        scheduleRepository.deleteAllByUser(user)

        // 5. User 삭제
        userRepository.delete(user)
    }
}