package my.app.notificationprovider.services

import my.app.notificationprovider.models.User
import my.app.notificationprovider.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: String): User? {
        return userRepository.findById(id)?.orElse(null)
    }

    fun getUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun createUser(user: User): User {
        return userRepository.save(user)
    }
}