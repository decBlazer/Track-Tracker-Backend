package com.varughese.tracktracker.data

data class Track(
    val name: String,
    val artists: List<Artist>
)

data class Artist(
    val name: String
)

data class PlaylistResponse(
    val items: List<TrackWrapper>
)

data class TrackWrapper(
    val track: Track
)