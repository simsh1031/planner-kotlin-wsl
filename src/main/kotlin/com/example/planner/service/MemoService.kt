package com.example.planner.service

import com.example.planner.domain.memo.Memo
import com.example.planner.repository.MemoRepository
import com.example.planner.repository.MemoTagRepository
import com.example.planner.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemoService(
    private val memoRepository: MemoRepository,
    private val userRepository: UserRepository,
    private val memoTagRepository: MemoTagRepository
) {

    fun createMemo(userId: Long, title: String, content: String): Memo {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        return memoRepository.save(Memo(title = title, content = content, user = user))
    }

    fun getMemosByUserId(userId: Long): List<Memo> {
        return memoRepository.findByUserUserId(userId)  // ✅ userId로 필터링
    }

    fun deleteMemo(memoId: Long) {
        memoTagRepository.deleteByMemoMemoId(memoId)
        memoRepository.deleteById(memoId)
    }

    fun updateMemo(memoId: Long, title: String, content: String): Memo {
        val memo = memoRepository.findById(memoId)
            .orElseThrow { IllegalArgumentException("Memo not found") }

        return memoRepository.save(
            Memo(memoId = memo.memoId, title = title, content = content, user = memo.user)
        )
    }
}