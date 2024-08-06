package com.varughese.tracktracker.controller.authentication

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import org.springframework.web.util.UriComponentsBuilder
import java.util.*


@Controller
class SpotifyCallbackController {
    @Value("\${spring.security.oauth2.client.registration.spotify.client-id}")
    private val clientId: String? = null

    @Value("\${spring.security.oauth2.client.registration.spotify.client-secret}")
    private val clientSecret: String? = null

    @Value("\${spotify.redirect-uri}")
    private val redirectUri: String? = null

    @GetMapping("/callback")
    fun callback(@RequestParam("code") code: String?, @RequestParam("state") state: String?): String {
        val tokenEndpoint = "https://accounts.spotify.com/api/token"

        val credentials = "$clientId:$clientSecret"
        val encodedCredentials = Base64.getEncoder().encodeToString(credentials.toByteArray())

        val headers = HttpHeaders()
        headers.add("Authorization", "Basic $encodedCredentials")
        headers.add("Content-Type", "application/x-www-form-urlencoded")

        val body = UriComponentsBuilder.fromHttpUrl(tokenEndpoint)
            .queryParam("grant_type", "authorization_code")
            .queryParam("code", code)
            .queryParam("redirect_uri", redirectUri)
            .build()
            .toUriString()

        val request = HttpEntity(body, headers)

        val restTemplate = RestTemplate()
        val response: ResponseEntity<Map<*, *>> = restTemplate.postForEntity<Map<*, *>>(
            tokenEndpoint, request,
            MutableMap::class.java
        )

        val responseBody: Map<*, *>? = response.body

        if (responseBody != null) {
            val accessToken = responseBody["access_token"] as String?
        }

        return "redirect:/"
    }
}