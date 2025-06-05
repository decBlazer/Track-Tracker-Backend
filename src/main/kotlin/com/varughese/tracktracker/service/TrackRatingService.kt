package com.varughese.tracktracker.service

import com.varughese.tracktracker.model.TrackRating
import com.varughese.tracktracker.repository.TrackRatingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TrackRatingService(private val trackRatingRepository: TrackRatingRepository) {
    @Transactional
    fun createOrUpdateRating(userId: Long, trackId: Long, rating: Int, review: String? = null): TrackRating {
        val existingRating = trackRatingRepository.findByUserIdAndTrackId(userId, trackId)
        
        return if (existingRating.isPresent) {
            val rating = existingRating.get()
            rating.copy(
                rating = rating,
                review = review
            ).also { trackRatingRepository.save(it) }
        } else {
            TrackRating(
                user = User(id = userId),
                track = Track(id = trackId),
                rating = rating,
                review = review
            ).also { trackRatingRepository.save(it) }
        }
    }

    fun getRating(userId: Long, trackId: Long) = trackRatingRepository.findByUserIdAndTrackId(userId, trackId)
    fun getTrackRatings(trackId: Long) = trackRatingRepository.findByTrackId(trackId)
    fun getUserRatings(userId: Long) = trackRatingRepository.findByUserId(userId)
    fun getAverageRating(trackId: Long) = trackRatingRepository.findAverageRatingByTrackId(trackId)
} 