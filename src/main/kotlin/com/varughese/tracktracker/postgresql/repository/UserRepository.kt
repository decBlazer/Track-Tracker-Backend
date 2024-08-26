package com.varughese.tracktracker.postgresql.repository

import com.varughese.tracktracker.postgresql.data.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findBySpotifyId(spotifyId: String): Optional<User>
}