package my.app.notifications.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    val id: String? = null,
    val username: String,
    val email: String,
    val password: String,
    val role: UserRole = UserRole.USER
)

enum class UserRole {
    USER, ADMIN
}