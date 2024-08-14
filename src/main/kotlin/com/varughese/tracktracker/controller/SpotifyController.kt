package com.varughese.tracktracker.controller

import com.adamratzman.spotify.models.SimpleArtist
import com.varughese.tracktracker.SpotifyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException

data class TrackInfo(
    val id: String,
    val name: String,
    val artist: String,
    val album: String,
    val artwork: String,
    val popularity: Double
)

@RestController
class SpotifyController(private val spotifyService: SpotifyService) {

    @GetMapping("/search")
    suspend fun searchTracks(@RequestParam query: String): ResponseEntity<List<TrackInfo>> {
        return try {
            // Fetch search results
            val result = spotifyService.searchTracks(query)
            val tracks = result.items

            // Map the tracks to TrackInfo
            val trackInfos = tracks.map { track ->
                TrackInfo(
                    id = track.id,
                    name = track.name,
                    artist = track.artists.joinToString(", ") { it.name ?: "Unknown Artist" },
                    album = track.album.name,
                    artwork = track.album.images?.firstOrNull()?.url ?: "No Artwork Available",
                    popularity = track.popularity
                )
            }

            ResponseEntity.ok(trackInfos)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).body(emptyList())
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

    @GetMapping("/song/{id}")
    suspend fun getSong(@PathVariable id: String): ResponseEntity<TrackInfo> {
        return try {
            val track = spotifyService.getSong(id)

            val trackInfo = TrackInfo(
                id = track?.id ?: "Unknown ID",
                name = track?.name ?: "Unknown Title",
                artist = track?.artists?.joinToString(", ") { it.name ?: "Unknown Artist" } ?: "Unknown Artist",
                album = track?.album?.name ?: "Unknown Album",
                artwork = track?.album?.images?.firstOrNull()?.url ?: "No Artwork Available",
                popularity = track?.popularity ?: 0.0
            )

            ResponseEntity.ok(trackInfo)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).body(
                TrackInfo(
                    id = "error",
                    name = "Error",
                    artist = "Error",
                    album = "Error",
                    artwork = "No Artwork Available",
                    popularity = 0.0
                )
            )
        }
    }

    @GetMapping("/random-songs")
    suspend fun getRandomSongs(): ResponseEntity<List<TrackInfo>> {
        return try {
            val playlist = spotifyService.getPlaylist("69fEt9DN5r4JQATi52sRtq")

            val tracks = playlist?.tracks?.items

            if (!tracks.isNullOrEmpty()) {
                val randomTracks = tracks.shuffled().take(4).map { it.track }

                val trackInfos = randomTracks.map { track ->
                    TrackInfo(
                        id = track?.id ?: "unknown_id",
                        name = track?.asTrack?.name ?: "Unknown Title",
                        artist = track?.asTrack?.artists?.joinToString(", ") { artist: SimpleArtist -> artist.name.toString() } ?: "Unknown Artist",
                        album = track?.asTrack?.album?.name ?: "Unknown Album",
                        artwork = track?.asTrack?.album?.images?.firstOrNull()?.url ?: "No artwork available",
                        popularity = track?.asTrack?.popularity ?: 0.0
                    )
                }

                ResponseEntity.ok(trackInfos)
            } else {
                ResponseEntity.status(404).body(emptyList())
            }
        } catch (e: HttpClientErrorException) {
            ResponseEntity.status(e.statusCode).body(emptyList())
        }
    }
}