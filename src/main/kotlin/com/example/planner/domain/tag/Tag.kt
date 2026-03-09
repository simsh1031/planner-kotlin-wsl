package com.example.planner.domain.tag

import jakarta.persistence.*

@Entity
class Tag(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val tagId: Long? = null,

    @Column(nullable = false, unique = true)
    val name: String
)