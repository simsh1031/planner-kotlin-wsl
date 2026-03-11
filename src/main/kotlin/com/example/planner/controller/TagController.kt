package com.example.planner.controller

import com.example.planner.dto.tag.CreateTagRequest
import com.example.planner.dto.tag.TagResponse
import com.example.planner.service.TagService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagService: TagService
) {

    @PostMapping
    fun createTag(
        @RequestBody request: CreateTagRequest
    ): TagResponse {

        val tag = tagService.createTag(request.name)

        return TagResponse(
            tagId = tag.tagId,
            name = tag.name
        )
    }

    @GetMapping
    fun getTags(): List<TagResponse> {

        val tags = tagService.getTags()

        return tags.map {
            TagResponse(
                tagId = it.tagId,
                name = it.name
            )
        }
    }
}