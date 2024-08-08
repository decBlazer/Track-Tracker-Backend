package com.varughese.tracktracker.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


data class Track(
    val name: String,
    val artists: List<Artist>
)

data class Artist(
    val name: String
)

data class PlaylistResponse(
    val items: List<TrackWrapper>
)

data class TrackWrapper(
    val track: Track
)

@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val username: String,
    val email: String
)