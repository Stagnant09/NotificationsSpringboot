package my.app.notificationprovider.repositories

import my.app.notificationprovider.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String> {

    fun findByUsername(username: String) : User?

}
