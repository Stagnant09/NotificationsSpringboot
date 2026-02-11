package my.app.notifications.models

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String,
    val title: String = "",
    val message: String = "",
    val time: Long,
    val priority: NotificationPriority = NotificationPriority.NORMAL,
    var deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING
)

@Serializable
enum class NotificationPriority {
    NORMAL,
    URGENT,
    CRITICAL
}

@Serializable
enum class DeliveryStatus {
    PENDING,
    SENT,
    FAILED,
    READ
}

fun getPlaceholderNotification() = Notification(
    id = "0",
    title = "Placeholder Notification",
    message = "This is a placeholder notification",
    priority = NotificationPriority.NORMAL,
    deliveryStatus = DeliveryStatus.PENDING,
    time = 0L
)