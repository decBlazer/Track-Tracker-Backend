package com.varughese.tracktracker.controller

import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequiredArgsConstructor
@CrossOrigin(origins = ["https://localhost:3000"])
@RestController
@RequestMapping("/api")
class ApiController {

    @GetMapping("/login")
    fun getLoginData(): ResponseEntity<String> {
        return ResponseEntity.ok("Login data")
    }
}