package com.varughese.tracktracker.postgresql.repository

import com.varughese.tracktracker.data.User
import org.springframework.data.jpa.repository.JpaRepository

import java.util.Optional


interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): Optional<User>

}