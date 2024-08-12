package com.varughese.tracktracker

import com.adamratzman.spotify.*
import com.adamratzman.spotify.models.Token
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.reactive.function.client.WebClient
import kotlinx.coroutines.runBlocking
import java.util.*


@Service
class SpotifyService(
    @Value("\${spring.security.oauth2.client.registration.spotify.client-id}") private val clientId: String,
    @Value("\${spring.security.oauth2.client.registration.spotify.client-secret}") private val clientSecret: String,
    @Value("\${spotify.redirect-uri}") private val redirectUri: String,
    @Value("\${spotify.token-uri}") private val tokenUri: String,
    @Value("\${spotify.api-base-url}") private val apiBaseUrl: String
) {

    lateinit var spotifyApi: SpotifyAppApi
    lateinit var token: Token

    @PostConstruct
    fun init() = runBlocking {
        val credentials = SpotifyCredentials().apply {
            this.clientId = this@SpotifyService.clientId
            this.clientSecret = this@SpotifyService.clientSecret
            this.redirectUri = this@SpotifyService.redirectUri
        }

        spotifyApi = SpotifyAppApiBuilder(credentials).build()
        token = spotifyApi.token
    }

    @GetMapping("/search")
    suspend fun searchTracks(query: String) = spotifyApi.search.searchTrack(query)
    @GetMapping("/track/{id}")
    suspend fun getTrack(id: String) = spotifyApi.tracks.getTrack(id)
    @GetMapping("/playlist/{id}")
    suspend fun getPlaylist(id: String) = spotifyApi.playlists.getPlaylist(id)
}
