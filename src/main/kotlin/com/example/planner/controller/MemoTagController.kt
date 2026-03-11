package com.example.planner.controller

import com.example.planner.dto.memotag.AddTagToMemoRequest
import com.example.planner.dto.memotag.MemoTagResponse
import com.example.planner.dto.tag.TagResponse
import com.example.planner.repository.MemoTagRepository
import com.example.planner.service.MemoTagService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/memos")
class MemoTagController(
    private val memoTagService: MemoTagService,
) {

    @PostMapping("/{memoId}/tags")
    fun addTagToMemo(
        @PathVariable memoId: Long,
        @RequestBody request: AddTagToMemoRequest
    ): MemoTagResponse {

        val memoTag = memoTagService.addTagToMemo(
            memoId = memoId,
            tagId = request.tagId
        )

        return MemoTagResponse(
            memoId = memoTag.memo.memoId,
            tagId = memoTag.tag.tagId,
            tagName = memoTag.tag.name
        )
    }

    @GetMapping("/{memoId}/tags")
    fun getMemoTags(
        @PathVariable memoId: Long
    ): List<TagResponse> {

        val tags = memoTagService.getMemoTags(memoId)

        return tags.map {
            TagResponse(
                tagId = it.tagId,
                name = it.name
            )
        }
    }
}