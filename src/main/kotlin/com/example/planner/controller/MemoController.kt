package com.example.planner.controller

import com.example.planner.dto.common.DeleteResponse
import com.example.planner.dto.memo.*
import com.example.planner.repository.MemoRepository
import com.example.planner.service.MemoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/memos")
class MemoController(
    private val memoService: MemoService,
) {

    @PostMapping
    fun createMemo(
        @RequestBody request: CreateMemoRequest
    ): MemoResponse {

        val memo = memoService.createMemo(
            userId = request.userId,
            title = request.title,
            content = request.content
        )

        return MemoResponse(
            memoId = memo.memoId,
            userId = memo.user.userId,
            title = memo.title,
            content = memo.content,
            createdAt = memo.createdAt.toString()
        )
    }

    @GetMapping
    fun getMemos(): List<MemoResponse> {

        val memos = memoService.getMemos()

        return memos.map {
            MemoResponse(
                memoId = it.memoId,
                userId = it.user.userId,
                title = it.title,
                content = it.content,
                createdAt = it.createdAt.toString()
            )
        }
    }

    @PutMapping("/{memoId}")
    fun updateMemo(
        @PathVariable memoId: Long,
        @RequestBody request: UpdateMemoRequest
    ): MemoResponse {

        val memo = memoService.updateMemo(
            memoId,
            request.title,
            request.content
        )

        return MemoResponse(
            memoId = memo.memoId,
            userId = memo.user.userId,
            title = memo.title,
            content = memo.content,
            createdAt = memo.createdAt.toString()
        )
    }

    @DeleteMapping("/{memoId}")
    fun deleteMemo(
        @PathVariable memoId: Long
    ): DeleteResponse {

        memoService.deleteMemo(memoId)

        return DeleteResponse(
            message = "Memo deleted successfully"
        )
    }
}