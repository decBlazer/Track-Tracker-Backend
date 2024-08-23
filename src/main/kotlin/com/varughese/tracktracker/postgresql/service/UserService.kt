package com.varughese.tracktracker.postgresql.service

import com.varughese.tracktracker.postgresql.data.User
import com.varughese.tracktracker.postgresql.repository.UserRepository
import org.springframework.stereotype.Service

@Service

class UserService(private val userRepository: UserRepository) {


    fun registerUser(username: String, email: String, password: String) {

        val passwordHash = hashPassword(password) // Implement password hashing

        val user = User(username = username, email = email, passwordHash = passwordHash)

        userRepository.save(user)

    }


    private fun hashPassword(password: String): String {

        // Implement your password hashing logic here

        return password // Placeholder

    }

}