package com.example.planner.repository

import com.example.planner.domain.todo.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long> {

    fun findByCompleted(completed: Boolean): List<Todo>

    fun deleteByScheduleScheduleId(scheduleId: Long)  // ← 추가
}