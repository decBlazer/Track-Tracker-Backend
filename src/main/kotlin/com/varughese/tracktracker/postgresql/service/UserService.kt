package com.varughese.tracktracker.postgresql.service

import com.varughese.tracktracker.postgresql.data.User
import org.springframework.stereotype.Service

import com.varughese.tracktracker.postgresql.repository.UserRepository

import org.springframework.web.client.RestTemplate

import org.springframework.http.HttpHeaders

import org.springframework.http.HttpEntity

import org.springframework.http.HttpMethod

import org.springframework.http.ResponseEntity
import java.time.LocalDateTime


@Service

class UserService(private val userRepository: UserRepository) {


    fun registerUser(accessToken: String): User {

        val spotifyUserInfo = fetchSpotifyUserInfo(accessToken)


        val existingUser = userRepository.findBySpotifyId(spotifyUserInfo.id)

        if (existingUser.isPresent) {

            return existingUser.get()

        }


        // Create a new user record

        val user = User(

            id = spotifyUserInfo.id,

            spotifyId = spotifyUserInfo.id,

            email = spotifyUserInfo.email,

            username = spotifyUserInfo.username,

            passwordHash = "",

            createdAt = LocalDateTime.now()
        )

        return userRepository.save(user)

    }


    private fun fetchSpotifyUserInfo(accessToken: String): SpotifyUserInfo {

        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        headers.set("Authorization", "Bearer $accessToken")

        val entity = HttpEntity<String>(headers)


        val response: ResponseEntity<SpotifyUserInfo> = restTemplate.exchange(

            "https://api.spotify.com/v1/me",

            HttpMethod.GET,

            entity,

            SpotifyUserInfo::class.java

        )


        return response.body ?: throw RuntimeException("Failed to fetch user info from Spotify")

    }

}


data class SpotifyUserInfo(

    val id: String,

    val email: String,

    val username: String

)