package com.varughese.tracktracker.repository

import com.varughese.tracktracker.model.TrackRating
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TrackRatingRepository : JpaRepository<TrackRating, Long> {
    fun findByUserIdAndTrackId(userId: Long, trackId: Long): Optional<TrackRating>
    fun findByTrackId(trackId: Long): List<TrackRating>
    fun findByUserId(userId: Long): List<TrackRating>
    fun findAverageRatingByTrackId(trackId: Long): Double
} 