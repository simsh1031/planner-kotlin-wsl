package com.example.planner.repository

import com.example.planner.domain.tag.MemoTag
import org.springframework.data.jpa.repository.JpaRepository

interface MemoTagRepository : JpaRepository<MemoTag, Long>