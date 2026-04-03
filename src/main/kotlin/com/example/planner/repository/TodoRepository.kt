package com.example.planner.repository

import com.example.planner.domain.schedule.Schedule
import com.example.planner.domain.todo.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long> {
    fun findByScheduleUserUserId(userId: Long): List<Todo>                          // ✅ 추가
    fun findByScheduleUserUserIdAndCompleted(userId: Long, completed: Boolean): List<Todo>  // ✅ 추가
    fun findByCompleted(completed: Boolean): List<Todo>
    fun deleteByScheduleScheduleId(scheduleId: Long)
    fun deleteAllByScheduleIn(schedules: List<Schedule>)
}