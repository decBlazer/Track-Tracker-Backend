package com.varughese.tracktracker.repository

import com.varughese.tracktracker.model.TrackedTrack
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TrackedTrackRepository : JpaRepository<TrackedTrack, Long> {
    fun findByUserIdAndTrackId(userId: Long, trackId: Long): Optional<TrackedTrack>
    fun findByUserId(userId: Long): List<TrackedTrack>
    fun findByTrackId(trackId: Long): List<TrackedTrack>
    fun findTopByUserIdOrderByTrackedAtDesc(userId: Long): List<TrackedTrack>
} 