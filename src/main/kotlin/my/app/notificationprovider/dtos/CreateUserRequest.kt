package my.app.notificationprovider.dtos

import my.app.notificationprovider.models.UserRole

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: UserRole
)