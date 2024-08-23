package com.varughese.tracktracker.postgresql.data

import com.adamratzman.spotify.models.Track
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import lombok.Data
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
@Data
data class User(

    @Id
    val id: Long = 0,

    val username: String,

    val email: String,

    val passwordHash: String,

    val createdAt: LocalDateTime = LocalDateTime.now()

)