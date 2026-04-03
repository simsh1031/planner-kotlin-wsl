package com.example.planner.service

import com.example.planner.domain.user.Role
import com.example.planner.domain.user.User
import com.example.planner.repository.MemoRepository
import com.example.planner.repository.MemoTagRepository
import com.example.planner.repository.ScheduleRepository
import com.example.planner.repository.TodoRepository
import com.example.planner.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminService(
    private val userRepository: UserRepository,
    private val memoRepository: MemoRepository,
    private val memoTagRepository: MemoTagRepository,
    private val scheduleRepository: ScheduleRepository,
    private val todoRepository: TodoRepository
) {

    fun checkAdmin(httpRequest: HttpServletRequest) {
        val tokenUserId = httpRequest.getAttribute("userId")?.toString()?.toLongOrNull()
            ?: throw IllegalArgumentException("인증 정보가 없습니다.")

        val user = userRepository.findById(tokenUserId)
            .orElseThrow { IllegalArgumentException("유저를 찾을 수 없습니다.") }

        if (user.role != Role.ADMIN) {
            throw IllegalArgumentException("관리자만 접근할 수 있습니다.")
        }
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun findUser(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }
    }

    fun updateUser(userId: Long, nickname: String?, newPassword: String?): User {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (nickname != null) user.nickname = nickname
        if (newPassword != null) user.password = newPassword

        return userRepository.save(user)
    }

    fun deleteUser(userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

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