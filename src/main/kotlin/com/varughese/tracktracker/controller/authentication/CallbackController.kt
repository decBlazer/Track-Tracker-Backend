import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpMethod
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

        // Encode client ID and client secret
        val credentials = "$clientId:$clientSecret"
        val encodedCredentials = Base64.getEncoder().encodeToString(credentials.toByteArray())

        // Set up the headers
        val headers = HttpHeaders().apply {
            set("Authorization", "Basic $encodedCredentials")
            set("Content-Type", "application/x-www-form-urlencoded")
        }

        // Set up the body
        val body = UriComponentsBuilder.fromHttpUrl(tokenEndpoint)
            .queryParam("grant_type", "authorization_code")
            .queryParam("code", code)
            .queryParam("redirect_uri", redirectUri)
            .build()
            .toUriString()

        // Create HTTP entity
        val request = HttpEntity(body, headers)

        // Create RestTemplate and make the request
        val restTemplate = RestTemplate()
        val response: ResponseEntity<Map<*, *>> = try {
            restTemplate.exchange(tokenEndpoint, HttpMethod.POST, request, Map::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            return "redirect:/error"
        }

        // Extract access token from the response
        val accessToken = response.body?.get("access_token") as String?
        println("Access Token: $accessToken")

        // Store the access token in the session (if needed)
        // session.setAttribute("accessToken", accessToken)

        return "redirect:/"
    }
}
