package com.varughese.tracktracker.controller

import com.varughese.tracktracker.service.SpotifyService
import com.varughese.tracktracker.service.TrackService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/spotify")
class SpotifyController(
    private val spotifyService: SpotifyService,
    private val trackService: TrackService
) {
    @GetMapping("/search")
    fun searchTracks(@RequestParam query: String, @RequestParam(defaultValue = "20") limit: Int): ResponseEntity<Any> {
        return runBlocking {
            try {
                val tracks = spotifyService.searchTracks(query, limit)
                ResponseEntity.ok(tracks)
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(mapOf("error" to e.message))
            }
        }
    }

    @GetMapping("/tracks/{spotifyId}")
    fun getTrack(@PathVariable spotifyId: String): ResponseEntity<Any> {
        return runBlocking {
            try {
                val spotifyTrack = spotifyService.getTrack(spotifyId)
                if (spotifyTrack != null) {
                    // Save or update track in our database
                    val track = trackService.findBySpotifyId(spotifyId)
                        .orElseGet {
                            trackService.createTrack(
                                spotifyId = spotifyTrack.id,
                                name = spotifyTrack.name,
                                artist = spotifyTrack.artists.firstOrNull()?.name ?: "Unknown Artist",
                                album = spotifyTrack.album?.name,
                                albumArtUrl = spotifyTrack.album?.images?.firstOrNull()?.url
                            )
                        }
                    ResponseEntity.ok(track)
                } else {
                    ResponseEntity.notFound().build()
                }
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(mapOf("error" to e.message))
            }
        }
    }

    @GetMapping("/tracks/{spotifyId}/features")
    fun getTrackFeatures(@PathVariable spotifyId: String): ResponseEntity<Any> {
        return runBlocking {
            try {
                val features = spotifyService.getTrackAudioFeatures(spotifyId)
                ResponseEntity.ok(features)
            } catch (e: Exception) {
                ResponseEntity.badRequest().body(mapOf("error" to e.message))
            }
        }
    }
}
