package com.example.planner.service

import com.example.planner.domain.tag.MemoTag
import com.example.planner.repository.MemoRepository
import com.example.planner.repository.MemoTagRepository
import com.example.planner.repository.TagRepository
import org.springframework.stereotype.Service

@Service
class MemoTagService(
    private val memoRepository: MemoRepository,
    private val tagRepository: TagRepository,
    private val memoTagRepository: MemoTagRepository
) {

    fun addTagToMemo(memoId: Long, tagId: Long): MemoTag {

        val memo = memoRepository.findById(memoId)
            .orElseThrow { IllegalArgumentException("Memo not found") }

        val tag = tagRepository.findById(tagId)
            .orElseThrow { IllegalArgumentException("Tag not found") }

        val memoTag = MemoTag(
            memo = memo,
            tag = tag
        )

        return memoTagRepository.save(memoTag)
    }
}