package my.app.notificationprovider.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import my.app.notificationprovider.models.NotificationPriority

data class CreateNotificationRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 1, max = 100)
    val title: String,

    @field:NotBlank(message = "Message is required")
    @field:Size(min = 1, max = 1000)
    val message: String,

    val priority: NotificationPriority = NotificationPriority.NORMAL
)