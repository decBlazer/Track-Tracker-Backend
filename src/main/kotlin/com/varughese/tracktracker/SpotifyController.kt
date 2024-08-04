package com.varughese.tracktracker

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class SpotifyController {

    @Value("\${spotify.api-base-url}")
    private lateinit var spotifyApiUrl: String

    @GetMapping("/random-songs")
    fun getRandomSongs(@AuthenticationPrincipal principal: OidcUser): ResponseEntity<String> {
        val accessToken = principal.idToken.tokenValue
        val headers = HttpHeaders().apply {
            setBearerAuth(accessToken)
        }

        val entity = HttpEntity<String>(headers)
        val restTemplate = RestTemplate()
        val response = restTemplate.exchange(
            "$spotifyApiUrl/v1/playlists/69fEt9DN5r4JQATi52sRtq/tracks", // The Sound of Everything (Spotify Playlist)
            HttpMethod.GET,
            entity,
            String::class.java
        )

        return ResponseEntity.ok(response.body)
    }
}