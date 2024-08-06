package com.varughese.tracktracker.controller.authentication

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class AuthController {
    @Value("\${spring.security.oauth2.client.registration.spotify.client-id}")
    private val clientId: String? = null

    @Value("\${spotify.redirect-uri}")
    private val redirectUri: String? = null

    @GetMapping("/login")
    fun login(): String {
        val scope = "user-read-private user-read-email"
        val state = generateRandomString(16)

        return "redirect:https://accounts.spotify.com/authorize?" +
                "response_type=code" +
                "&client_id=" + clientId +
                "&scope=" + scope +
                "&redirect_uri=" + redirectUri +
                "&state=" + state
    }

    private fun generateRandomString(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val sb = StringBuilder()
        for (i in 0 until length) {
            val index = (Math.random() * characters.length).toInt()
            sb.append(characters[index])
        }
        return sb.toString()
    }

    @GetMapping("/login/oauth2/code/spotify")
    fun handleOAuth2Callback(principal: OAuth2AuthenticationToken): String {
        val userDetails = principal.principal as OAuth2User
        return "redirect:/home"
    }
}