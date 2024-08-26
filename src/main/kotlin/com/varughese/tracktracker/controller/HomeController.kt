import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken

import org.springframework.security.oauth2.core.user.OAuth2User

import org.springframework.stereotype.Controller

import org.springframework.web.bind.annotation.GetMapping

/**
 * Used after successful login using Spotify
 */
@Controller

class HomeController {


    @GetMapping("/home")

    fun home(principal: OAuth2AuthenticationToken): String {

        val userDetails = principal.principal as OAuth2User

        return "home"

    }

}