package my.app.notificationprovider.models

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "app_user")
data class User(
    @Id val id: String = UUID.randomUUID().toString(),
    val username: String = "",
    val email: String = "",
    val password: String = "", // Will be encrypted
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val notifications: MutableList<Notification> = mutableListOf()
)

enum class UserRole {
    USER, ADMIN
}