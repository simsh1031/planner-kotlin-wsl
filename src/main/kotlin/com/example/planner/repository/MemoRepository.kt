package com.example.planner.repository

import com.example.planner.domain.memo.Memo
import com.example.planner.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<Memo, Long> {
    fun findByUserUserId(userId: Long): List<Memo>  // ✅ 추가

    fun deleteAllByUser(user: User)
}