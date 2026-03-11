package com.example.planner.service

import com.example.planner.domain.tag.Tag
import com.example.planner.repository.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TagService(
    private val tagRepository: TagRepository
) {

    fun createTag(name: String): Tag {

        val existing = tagRepository.findByName(name)

        if (existing != null) {
            return existing
        }

        val tag = Tag(name = name)

        return tagRepository.save(tag)
    }

    fun getTags(): List<Tag> {
        return tagRepository.findAll()
    }
}