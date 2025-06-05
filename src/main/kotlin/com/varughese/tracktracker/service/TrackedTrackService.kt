package com.varughese.tracktracker.service

import com.varughese.tracktracker.model.TrackedTrack
import com.varughese.tracktracker.repository.TrackedTrackRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TrackedTrackService(private val trackedTrackRepository: TrackedTrackRepository) {
    @Transactional
    fun trackTrack(userId: Long, trackId: Long, notes: String? = null): TrackedTrack {
        val existingTrack = trackedTrackRepository.findByUserIdAndTrackId(userId, trackId)
        
        return if (existingTrack.isPresent) {
            existingTrack.get()
        } else {
            TrackedTrack(
                user = User(id = userId),
                track = Track(id = trackId),
                notes = notes
            ).also { trackedTrackRepository.save(it) }
        }
    }

    @Transactional
    fun incrementPlayCount(userId: Long, trackId: Long) {
        trackedTrackRepository.findByUserIdAndTrackId(userId, trackId).ifPresent { trackedTrack ->
            trackedTrack.playCount++
            trackedTrackRepository.save(trackedTrack)
        }
    }

    fun getTrackedTrack(userId: Long, trackId: Long) = trackedTrackRepository.findByUserIdAndTrackId(userId, trackId)
    fun getUserTrackedTracks(userId: Long) = trackedTrackRepository.findByUserId(userId)
    fun getRecentTrackedTracks(userId: Long) = trackedTrackRepository.findTopByUserIdOrderByTrackedAtDesc(userId)
} 