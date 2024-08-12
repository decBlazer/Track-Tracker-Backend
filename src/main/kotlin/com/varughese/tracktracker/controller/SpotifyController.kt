package com.varughese.tracktracker.controller


import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.SimpleArtist
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.varughese.tracktracker.SpotifyService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.HttpClientErrorException


@RestController
class SpotifyController(private val spotifyService: SpotifyService) {

    @GetMapping("/search")
    suspend fun searchTracks(@RequestParam query: String): ResponseEntity<String> {
        return try {
            val result = spotifyService.searchTracks(query)
            ResponseEntity.ok(result.toString())
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Error fetching tracks: ${e.message}")
        }
    }

    @GetMapping("/track/{id}")
    suspend fun getTrack(@PathVariable id: String): ResponseEntity<String> {
        return try {
            val result = spotifyService.getTrack(id)
            ResponseEntity.ok(result.toString())
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Error fetching track: ${e.message}")
        }
    }

    @GetMapping("/playlist/{id}")
    suspend fun getPlaylist(@PathVariable id: String): ResponseEntity<String> {
        return try {
            val result = spotifyService.getPlaylist(id)
            ResponseEntity.ok(result.toString())
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Error fetching playlist: ${e.message}")
        }
    }

    @GetMapping("/random-songs")
    suspend fun getRandomSongs(): ResponseEntity<String> {
        return try {
            val playlist = spotifyService.getPlaylist("69fEt9DN5r4JQATi52sRtq")

            val tracks = playlist?.tracks?.items

            if (!tracks.isNullOrEmpty()) {
                val randomTrack = tracks.random().track

                val trackName = randomTrack?.asTrack?.name ?: "Unknown Title"
                val artistNames = randomTrack?.asTrack?.artists?.joinToString(", ") { artist: SimpleArtist -> artist.name.toString() } ?: "Unknown Artist"
                val trackInfo = "$trackName by $artistNames"
                ResponseEntity.ok(trackInfo)
            } else {
                ResponseEntity.status(404).body("No tracks found in the playlist")
            }
        } catch (e: HttpClientErrorException) {
            ResponseEntity.status(e.statusCode).body(e.message)
        }
    }
}