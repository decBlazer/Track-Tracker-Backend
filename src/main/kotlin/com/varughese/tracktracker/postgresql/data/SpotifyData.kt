package com.varughese.tracktracker.postgresql.data

import com.adamratzman.spotify.models.Track
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime




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

    val id: String,

    val spotifyId: String,

    val username: String,

    val email: String,

    val passwordHash: String,

    val createdAt: LocalDateTime = LocalDateTime.now()

)