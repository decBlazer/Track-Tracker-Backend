package com.varughese.tracktracker.controller

import com.adamratzman.spotify.models.SimpleArtist
import com.varughese.tracktracker.postgresql.data.TrackRating
import com.varughese.tracktracker.postgresql.repository.TrackRatingRepository
import com.varughese.tracktracker.postgresql.service.SpotifyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException

data class TrackInfo(
    val id: String,
    val name: String,
    val artist: String,
    val album: String,
    val artwork: String,
    val popularity: Double,
    val rating: Double? = null
)

data class RateTrackRequest(
    val id: String,
    val name: String,
    val artist: String,
    val album: String,
    val artwork: String,
    val popularity: Double,
    val rating: Double? = null
)

@RestController
class SpotifyController(private val spotifyService: SpotifyService, private val trackRatingRepository: TrackRatingRepository) {

    private val trackRatings = mutableMapOf<String, Double>()  // In-memory storage for ratings

    @GetMapping("/search")
    suspend fun searchTracks(@RequestParam query: String): ResponseEntity<List<TrackInfo>> {
        return try {
            val result = spotifyService.searchTracks(query)
            val tracks = result.items

            val trackInfos = tracks.map { track ->
                TrackInfo(
                    id = track.id,
                    name = track.name,
                    artist = track.artists.joinToString(", ") { it.name ?: "Unknown Artist" },
                    album = track.album.name,
                    artwork = track.album.images?.firstOrNull()?.url ?: "No Artwork Available",
                    popularity = track.popularity,
                    rating = trackRatings[track.id]  // Retrieve the rating from the in-memory storage
                )
            }

            ResponseEntity.ok(trackInfos)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).body(emptyList())
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
                popularity = track?.popularity ?: 0.0,
                rating = track?.id?.let { trackRatings[it] }  // Retrieve the rating if it exists
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
                        popularity = track?.asTrack?.popularity ?: 0.0,
                        rating = track?.id?.let { trackRatings[it] }  // Include the rating
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

    @PostMapping("/song/{id}/rate")
    suspend fun rateSong(@PathVariable id: String, @RequestParam rating: Double): ResponseEntity<String> {
        return try {
            // Fetch track details to populate the entity (this could be done with another service or method)
            val track = spotifyService.getSong(id) ?: throw Exception("Track not found")

            val trackRating = TrackRating(
                id = id,
                name = track.name ?: "Unknown Title",
                artist = track.artists.joinToString(", ") { it.name ?: "Unknown Artist" } ?: "Unknown Artist",
                album = track.album.name ?: "Unknown Album",
                artwork = track.album.images?.firstOrNull()?.url ?: "No Artwork Available",
                popularity = track.popularity ?: 0.0,
                rating = rating
            )

            withContext(Dispatchers.IO) {
                trackRatingRepository.save(trackRating)
            }
            ResponseEntity.ok("Rating submitted successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).body("Error submitting rating: ${e.message}")
        }
    }
}
