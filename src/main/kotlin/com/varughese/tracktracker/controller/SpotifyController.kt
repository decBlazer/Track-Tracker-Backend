package com.varughese.tracktracker.controller


import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.web.client.HttpClientErrorException

@RestController
class SpotifyController(
    @Value("\${spotify.api-base-url}") private val spotifyApiUrl: String,
    private val objectMapper: ObjectMapper // Inject ObjectMapper bean
) {

    @GetMapping("/random-songs")
    fun getRandomSongs(@AuthenticationPrincipal principal: OidcUser): ResponseEntity<String> {
        val accessToken = principal.idToken.tokenValue
        val headers = HttpHeaders().apply {
            setBearerAuth(accessToken)
            set("Accept", "application/json")
        }

        val entity = HttpEntity<String>(headers)
        val restTemplate = RestTemplate()

        return try {
            val response = restTemplate.exchange(
                "$spotifyApiUrl/v1/playlists/69fEt9DN5r4JQATi52sRtq/tracks",
                HttpMethod.GET,
                entity,
                String::class.java
            )

            val tracks = parseTracks(response.body)
            ResponseEntity.ok(tracks.joinToString(", "))
        } catch (e: HttpClientErrorException) {
            ResponseEntity.status(e.statusCode).body(e.message)
        }
    }

    private fun parseTracks(json: String?): List<String> {
        val tracks = mutableListOf<String>()
        try {
            val jsonNode: JsonNode = objectMapper.readTree(json)
            val trackWrappers = jsonNode.path("items")

            for (item in trackWrappers) {
                val trackNode = item.path("track")
                val trackName = trackNode.path("name").asText()
                val artistNames = trackNode.path("artists").map { it.path("name").asText() }
                val trackDescription = "$trackName by ${artistNames.joinToString(", ")}"
                tracks.add(trackDescription)
            }
        } catch (e: Exception) {
            // Handle parsing errors
            e.printStackTrace()
        }
        return tracks
    }
}