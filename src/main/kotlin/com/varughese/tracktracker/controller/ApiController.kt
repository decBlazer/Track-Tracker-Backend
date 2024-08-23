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


    @GetMapping("/login")

    fun getLoginData(): ResponseEntity<String> {

        return ResponseEntity.ok("Login data")

    }


    @PostMapping("/register")

    fun registerUser(@RequestBody user: User): ResponseEntity<String> {

        userService.registerUser(user.username, user.email, user.passwordHash)

        return ResponseEntity.ok("User registered successfully")

    }

}