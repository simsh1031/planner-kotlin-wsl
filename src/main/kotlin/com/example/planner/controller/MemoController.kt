package com.example.planner.controller

import com.example.planner.dto.common.DeleteResponse
import com.example.planner.dto.memo.*
import com.example.planner.service.MemoService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/memos")
class MemoController(
    private val memoService: MemoService,
) {

    @PostMapping
    fun createMemo(
        @RequestBody request: CreateMemoRequest,
        httpRequest: HttpServletRequest
    ): MemoResponse {
        val userId = httpRequest.getAttribute("userId") as Long

        val memo = memoService.createMemo(
            userId = userId,
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
    fun getMemos(request: HttpServletRequest): List<MemoResponse> {
        val userId = request.getAttribute("userId") as Long

        return memoService.getMemosByUserId(userId).map {
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

        val memo = memoService.updateMemo(memoId, request.title, request.content)

        return MemoResponse(
            memoId = memo.memoId,
            userId = memo.user.userId,
            title = memo.title,
            content = memo.content,
            createdAt = memo.createdAt.toString()
        )
    }

    @DeleteMapping("/{memoId}")
    fun deleteMemo(@PathVariable memoId: Long): DeleteResponse {
        memoService.deleteMemo(memoId)
        return DeleteResponse(message = "Memo deleted successfully")
    }
}