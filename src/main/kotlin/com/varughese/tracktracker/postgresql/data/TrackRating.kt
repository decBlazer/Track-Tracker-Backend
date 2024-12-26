package com.varughese.tracktracker.postgresql.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class TrackRating(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: String? = null,
    val name: String,
    val artist: String,
    val album: String,
    val artwork: String,
    val popularity: Double,
    val rating: Double
)