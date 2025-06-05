package com.varughese.tracktracker.service

import com.varughese.tracktracker.model.Track
import com.varughese.tracktracker.repository.TrackRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class TrackService(private val trackRepository: TrackRepository) {
    @Transactional
    fun createTrack(spotifyId: String, name: String, artist: String, album: String? = null, albumArtUrl: String? = null): Track {
        val track = Track(
            spotifyId = spotifyId,
            name = name,
            artist = artist,
            album = album,
            albumArtUrl = albumArtUrl
        )
        return trackRepository.save(track)
    }

    fun findBySpotifyId(spotifyId: String): Optional<Track> = trackRepository.findBySpotifyId(spotifyId)
    fun findByName(name: String): List<Track> = trackRepository.findByNameContainingIgnoreCase(name)
    fun findByArtist(artist: String): List<Track> = trackRepository.findByArtistContainingIgnoreCase(artist)
    fun findById(id: Long): Optional<Track> = trackRepository.findById(id)
} 