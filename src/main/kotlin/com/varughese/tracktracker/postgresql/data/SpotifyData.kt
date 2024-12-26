package com.varughese.tracktracker.postgresql.data

import com.adamratzman.spotify.models.Album
import com.adamratzman.spotify.models.SpotifyUri
import com.adamratzman.spotify.models.Track
import jakarta.persistence.*
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
@Table(name = "songs")
data class Song(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,
    val artist: String,
    val album: String,
    val spotifyUrl: String
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