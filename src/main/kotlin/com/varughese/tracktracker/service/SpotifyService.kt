package com.varughese.tracktracker.service

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.models.Track
import org.springframework.stereotype.Service

@Service
class SpotifyService(private val spotifyClientApi: SpotifyClientApi) {
    suspend fun searchTracks(query: String, limit: Int = 20): List<Track> {
        return spotifyClientApi.search.search(query, limit = limit).tracks?.items ?: emptyList()
    }

    suspend fun getTrack(spotifyId: String): Track? {
        return spotifyClientApi.tracks.getTrack(spotifyId)
    }

    suspend fun getTrackAudioFeatures(spotifyId: String) {
        return spotifyClientApi.tracks.getAudioFeatures(spotifyId)
    }
} 