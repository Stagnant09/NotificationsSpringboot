package my.app.notificationprovider.models

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "app_user")
data class User(
    @Id val id: String = UUID.randomUUID().toString(),
    val username: String,
    val email: String,
    val password: String, // Will be encrypted
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER
) {
    constructor() : this(
        id = UUID.randomUUID().toString(),
        username = "",
        email = "",
        password = "",
        role = UserRole.USER
    )
}

enum class UserRole {
    USER, ADMIN
}