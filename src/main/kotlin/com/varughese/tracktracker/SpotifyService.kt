package com.varughese.tracktracker

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.util.*


@Service
class SpotifyService(
    @Value("\${spring.security.oauth2.client.registration.spotify.client-id}") private val clientId: String,
    @Value("\${spring.security.oauth2.client.registration.spotify.client-secret}") private val clientSecret: String,
    @Value("\${spotify.token-uri}") private val tokenUri: String,
    @Value("\${spotify.api-base-url}") private val apiBaseUrl: String
) {

    private var accessToken: String? = null
    private val restTemplate = RestTemplate()

    private fun fetchAccessToken() {
        val authHeader = "Basic " + Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())
        val headers = HttpHeaders().apply {
            set("Authorization", authHeader)
            set("Content-Type", "application/x-www-form-urlencoded")
        }
        val request = HttpEntity("grant_type=client_credentials", headers)
        val response = restTemplate.exchange<Map<String, Any>>(tokenUri, HttpMethod.POST, request)
        accessToken = response.body?.get("access_token") as String
    }

    fun getTrack(trackId: String): Map<String, Any>? {
        if (accessToken == null) {
            fetchAccessToken()
        }

        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $accessToken")
        }
        val entity = HttpEntity<String>(headers)

        val response = restTemplate.exchange<Map<String, Any>>("$apiBaseUrl/tracks/$trackId", HttpMethod.GET, entity)
        return response.body
    }

    fun getUserData(accessToken: String): String? {
        val url = "https://api.spotify.com/v1/me"

        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $accessToken")

        val request = HttpEntity<String>(headers)

        val restTemplate = RestTemplate()
        val response = restTemplate.getForEntity(
            url,
            String::class.java, request
        )

        return response.body
    }
}