package com.example.planner.domain.user

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long? = null,

    @Column(unique = true, nullable = false)
    val email: String,

    @Column(nullable = false)
    var password: String,      // ← var로 변경

    @Column(nullable = false)
    var nickname: String,      // ← var로 변경

    @Enumerated(EnumType.STRING)
    val role: Role = Role.USER,

    val createdAt: LocalDateTime = LocalDateTime.now()
)