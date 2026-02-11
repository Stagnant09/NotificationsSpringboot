package my.app.notifications.dtos

import my.app.notifications.models.UserRole

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: UserRole
)