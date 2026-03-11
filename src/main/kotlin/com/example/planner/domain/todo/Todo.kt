package com.example.planner.domain.todo

import com.example.planner.domain.schedule.Schedule
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Todo(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val todoId: Long? = null,

    @Column(nullable = false)
    val content: String,

    val completed: Boolean = false,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val dueDate: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    val schedule: Schedule
)