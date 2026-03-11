package com.example.planner.repository

import com.example.planner.domain.memo.Memo
import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<Memo, Long>