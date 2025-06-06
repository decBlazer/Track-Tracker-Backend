package com.varughese.tracktracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class TrackTrackerApplication

fun main(args: Array<String>) {
    runApplication<TrackTrackerApplication>(*args)
}

// Testing