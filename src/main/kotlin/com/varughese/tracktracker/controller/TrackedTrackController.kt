package com.varughese.tracktracker.controller

import com.varughese.tracktracker.service.TrackedTrackService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tracked")
class TrackedTrackController(private val trackedTrackService: TrackedTrackService) {
    @PostMapping
    fun trackTrack(
        @RequestParam userId: Long,
        @RequestParam trackId: Long,
        @RequestParam(required = false) notes: String?
    ): ResponseEntity<Any> {
        val trackedTrack = trackedTrackService.trackTrack(userId, trackId, notes)
        return ResponseEntity.ok(trackedTrack)
    }

    @PostMapping("/{userId}/track/{trackId}/play")
    fun incrementPlayCount(
        @PathVariable userId: Long,
        @PathVariable trackId: Long
    ): ResponseEntity<Any> {
        trackedTrackService.incrementPlayCount(userId, trackId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/user/{userId}/track/{trackId}")
    fun getTrackedTrack(@PathVariable userId: Long, @PathVariable trackId: Long) = trackedTrackService.getTrackedTrack(userId, trackId)
        .map { ResponseEntity.ok(it) }
        .orElse(ResponseEntity.notFound().build())

    @GetMapping("/user/{userId}")
    fun getUserTrackedTracks(@PathVariable userId: Long) = ResponseEntity.ok(trackedTrackService.getUserTrackedTracks(userId))

    @GetMapping("/user/{userId}/recent")
    fun getRecentTrackedTracks(@PathVariable userId: Long) = ResponseEntity.ok(trackedTrackService.getRecentTrackedTracks(userId))
} 