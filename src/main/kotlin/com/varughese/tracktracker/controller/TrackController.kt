package com.varughese.tracktracker.controller

import com.varughese.tracktracker.service.TrackService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tracks")
class TrackController(private val trackService: TrackService) {
    @PostMapping
    fun createTrack(
        @RequestParam spotifyId: String,
        @RequestParam name: String,
        @RequestParam artist: String,
        @RequestParam(required = false) album: String?,
        @RequestParam(required = false) albumArtUrl: String?
    ): ResponseEntity<Any> {
        val track = trackService.createTrack(spotifyId, name, artist, album, albumArtUrl)
        return ResponseEntity.ok(track)
    }

    @GetMapping("/spotify/{spotifyId}")
    fun getTrackBySpotifyId(@PathVariable spotifyId: String) = trackService.findBySpotifyId(spotifyId)
        .map { ResponseEntity.ok(it) }
        .orElse(ResponseEntity.notFound().build())

    @GetMapping("/search/name")
    fun searchByName(@RequestParam query: String) = ResponseEntity.ok(trackService.findByName(query))

    @GetMapping("/search/artist")
    fun searchByArtist(@RequestParam query: String) = ResponseEntity.ok(trackService.findByArtist(query))

    @GetMapping("/{id}")
    fun getTrack(@PathVariable id: Long) = trackService.findById(id)
        .map { ResponseEntity.ok(it) }
        .orElse(ResponseEntity.notFound().build())
} 