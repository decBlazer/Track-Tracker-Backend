package com.varughese.tracktracker.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tracks")
data class Track(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val spotifyId: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val artist: String,

    @Column
    val album: String? = null,

    @Column
    val albumArtUrl: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "track", cascade = [CascadeType.ALL])
    val ratings: MutableList<TrackRating> = mutableListOf(),

    @OneToMany(mappedBy = "track", cascade = [CascadeType.ALL])
    val trackedBy: MutableList<TrackedTrack> = mutableListOf()
) 