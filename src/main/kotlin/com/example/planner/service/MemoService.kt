package com.example.planner.service

import com.example.planner.domain.memo.Memo
import com.example.planner.repository.MemoRepository
import com.example.planner.repository.MemoTagRepository  // ← 추가
import com.example.planner.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemoService(
    private val memoRepository: MemoRepository,
    private val userRepository: UserRepository,
    private val memoTagRepository: MemoTagRepository  // ← 추가
) {

    fun createMemo(userId: Long, title: String, content: String): Memo {

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val memo = Memo(
            title = title,
            content = content,
            user = user
        )

        return memoRepository.save(memo)
    }

    fun getMemos(): List<Memo> {
        return memoRepository.findAll()
    }

    fun deleteMemo(memoId: Long) {
        memoTagRepository.deleteByMemoMemoId(memoId)  // ← MemoTag 먼저 삭제
        memoRepository.deleteById(memoId)
    }

    fun updateMemo(
        memoId: Long,
        title: String,
        content: String
    ): Memo {

        val memo = memoRepository.findById(memoId)
            .orElseThrow { IllegalArgumentException("Memo not found") }

        val updated = Memo(
            memoId = memo.memoId,
            title = title,
            content = content,
            user = memo.user
        )

        return memoRepository.save(updated)
    }
}