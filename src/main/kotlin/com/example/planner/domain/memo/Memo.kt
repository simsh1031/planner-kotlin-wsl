package com.example.planner.domain.memo

import com.example.planner.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Memo(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memoId: Long? = null,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "TEXT")
    val content: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
)