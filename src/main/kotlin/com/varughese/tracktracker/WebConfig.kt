package com.varughese.tracktracker

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@EnableWebSecurity
class WebConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors ->
                cors.configurationSource {
                    val source = CorsConfiguration()
                    source.allowedOrigins = listOf("http://localhost:3000") // React app url
                    source.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
                    source.allowedHeaders = listOf("*")
                    source
                }
            }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/", "/home", "/search", "/loginpage", "/index.html", "/static/**", "/favicon.ico", "/manifest.json", "/logo192.png").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/loginpage")
                    .defaultSuccessUrl("/home", true)
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .permitAll()
            }
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint { request, response, authException ->
                        response.sendRedirect("/loginpage")
                    }
            }

        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user =
            User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build()

        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry
                    .addMapping("/**")
                    .allowedMethods(CorsConfiguration.ALL)
                    .allowedHeaders(CorsConfiguration.ALL)
                    .allowedOriginPatterns(CorsConfiguration.ALL)
            }
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = mutableListOf("*")
        configuration.allowedMethods = mutableListOf("*")
        configuration.allowedHeaders = mutableListOf("*")
        val source: UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}