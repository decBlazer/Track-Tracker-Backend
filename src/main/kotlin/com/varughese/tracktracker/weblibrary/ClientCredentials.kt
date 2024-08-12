package com.varughese.tracktracker.weblibrary

import org.apache.hc.core5.http.ParseException
import org.springframework.beans.factory.annotation.Value
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest
import java.io.IOException
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletionException


class ClientCredentials(@Value("\${spring.security.oauth2.client.registration.spotify.client-id}") private val clientId: String,
                        @Value("\${spring.security.oauth2.client.registration.spotify.client-secret}") private val clientSecret: String,
                        @Value("\${spotify.token-uri}") private val tokenUri: String,
                        @Value("\${spotify.api-base-url}") private val apiBaseUrl: String) {
    private val spotifyApi: SpotifyApi = SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .build()
    private val clientCredentialsRequest: ClientCredentialsRequest = spotifyApi.clientCredentials()
        .build()

    fun clientCredentials_Sync() {
        try {
            val clientCredentials = clientCredentialsRequest.execute()

            // Set access token for further "spotifyApi" object usage
            spotifyApi.accessToken = clientCredentials.accessToken

            println("Expires in: " + clientCredentials.expiresIn)
        } catch (e: IOException) {
            println("Error: " + e.message)
        } catch (e: SpotifyWebApiException) {
            println("Error: " + e.message)
        } catch (e: ParseException) {
            println("Error: " + e.message)
        }
    }

    fun clientCredentials_Async() {
        try {
            val clientCredentialsFuture = clientCredentialsRequest.executeAsync()

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            val clientCredentials = clientCredentialsFuture.join()

            // Set access token for further "spotifyApi" object usage
            spotifyApi.accessToken = clientCredentials.accessToken

            println("Expires in: " + clientCredentials.expiresIn)
        } catch (e: CompletionException) {
            println("Error: " + e.cause!!.message)
        } catch (e: CancellationException) {
            println("Async operation cancelled.")
        }
    }
}