package com.varughese.tracktracker.controller

import com.varughese.tracktracker.service.TrackRatingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ratings")
class TrackRatingController(private val trackRatingService: TrackRatingService) {
    @PostMapping
    fun createOrUpdateRating(
        @RequestParam userId: Long,
        @RequestParam trackId: Long,
        @RequestParam rating: Int,
        @RequestParam(required = false) review: String?
    ): ResponseEntity<Any> {
        if (rating !in 1..5) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Rating must be between 1 and 5"))
        }
        val trackRating = trackRatingService.createOrUpdateRating(userId, trackId, rating, review)
        return ResponseEntity.ok(trackRating)
    }

    @GetMapping("/user/{userId}/track/{trackId}")
    fun getRating(@PathVariable userId: Long, @PathVariable trackId: Long) = trackRatingService.getRating(userId, trackId)
        .map { ResponseEntity.ok(it) }
        .orElse(ResponseEntity.notFound().build())

    @GetMapping("/track/{trackId}")
    fun getTrackRatings(@PathVariable trackId: Long) = ResponseEntity.ok(trackRatingService.getTrackRatings(trackId))

    @GetMapping("/user/{userId}")
    fun getUserRatings(@PathVariable userId: Long) = ResponseEntity.ok(trackRatingService.getUserRatings(userId))

    @GetMapping("/track/{trackId}/average")
    fun getAverageRating(@PathVariable trackId: Long) = ResponseEntity.ok(mapOf("average" to trackRatingService.getAverageRating(trackId)))
} 