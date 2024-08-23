package com.varughese.tracktracker.postgresql.service

import com.varughese.tracktracker.data.User
import com.varughese.tracktracker.postgresql.repository.UserRepository
import org.springframework.stereotype.Service

@Service

class UserService(private val userRepository: UserRepository) {

    fun registerUser(username: String, email: String, password: String): User {

        val passwordHash = hashPassword(password)

        val user = User(username = username, email = email, passwordHash = passwordHash)

        return userRepository.save(user)

    }

    // Additional methods for user management

}