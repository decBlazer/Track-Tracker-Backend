package com.varughese.tracktracker.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "track_ratings")
data class TrackRating(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "track_id", nullable = false)
    val track: Track,

    @Column(nullable = false)
    val rating: Int, // 1-5 stars

    @Column
    val review: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) 