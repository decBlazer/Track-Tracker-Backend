package com.varughese.tracktracker.config

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyClientCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpotifyConfig {
    @Value("\${SPOTIFY_CLIENT_ID}")
    private lateinit var clientId: String

    @Value("\${SPOTIFY_CLIENT_SECRET}")
    private lateinit var clientSecret: String

    @Bean
    fun spotifyClientApi(): SpotifyClientApi {
        val credentials = SpotifyClientCredentials(
            clientId = clientId,
            clientSecret = clientSecret
        )
        return SpotifyClientApi(credentials)
    }
} 