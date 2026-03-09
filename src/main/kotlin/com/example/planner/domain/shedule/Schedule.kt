package com.example.planner.domain.schedule

import com.example.planner.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Schedule(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val scheduleId: Long? = null,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "TEXT")
    val description: String?,

    val startDate: LocalDateTime,

    val endDate: LocalDateTime,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
)