package com.varughese.tracktracker.repository

import com.varughese.tracktracker.model.Track
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TrackRepository : JpaRepository<Track, Long> {
    fun findBySpotifyId(spotifyId: String): Optional<Track>
    fun findByNameContainingIgnoreCase(name: String): List<Track>
    fun findByArtistContainingIgnoreCase(artist: String): List<Track>
} 