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
    var title: String,

    @Column(columnDefinition = "TEXT")
    var description: String?,

    var startDate: LocalDateTime,

    var endDate: LocalDateTime,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
)