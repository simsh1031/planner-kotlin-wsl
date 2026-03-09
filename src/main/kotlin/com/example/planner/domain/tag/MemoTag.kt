package com.example.planner.domain.tag

import com.example.planner.domain.memo.Memo
import jakarta.persistence.*

@Entity
@Table(
    name = "memo_tag",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["memo_id", "tag_id"])
    ]
)
class MemoTag(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memoTagId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id", nullable = false)
    val memo: Memo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    val tag: Tag
)