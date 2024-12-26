package com.varughese.tracktracker.postgresql.repository

import com.varughese.tracktracker.postgresql.data.TrackRating
import org.springframework.data.jpa.repository.JpaRepository

interface TrackRatingRepository : JpaRepository<TrackRating, String>