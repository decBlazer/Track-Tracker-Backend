package com.varughese.tracktracker.controller


import lombok.RequiredArgsConstructor

import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*

import com.varughese.tracktracker.postgresql.data.User
import com.varughese.tracktracker.postgresql.service.UserService


@RequiredArgsConstructor

@CrossOrigin(origins = ["https://localhost:3000"])

@RestController

@RequestMapping("/api")

class ApiController(private val userService: UserService) {


    @PostMapping("/register")

    fun registerUser(@RequestHeader("Authorization") accessToken: String): ResponseEntity<String> {

        val token = accessToken.removePrefix("Bearer ")

        userService.registerUser(token)

        return ResponseEntity.ok("User registered successfully")

    }

}