package my.app.notificationprovider.models

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "notifications")
data class Notification(
    @Id
    val id: String = UUID.randomUUID().toString(),

    val title: String = "",
    val message: String = "",
    val time: Long = System.currentTimeMillis(),

    @Enumerated(EnumType.STRING)
    val priority: NotificationPriority = NotificationPriority.NORMAL,

    @Enumerated(EnumType.STRING)
    var deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null
)

enum class NotificationPriority {
    NORMAL,
    URGENT,
    CRITICAL
}

enum class DeliveryStatus {
    PENDING,
    SENT,
    FAILED,
    READ
}

fun getPlaceholderNotification() = Notification(
    title = "Placeholder Notification",
    message = "This is a placeholder notification",
    priority = NotificationPriority.NORMAL,
    deliveryStatus = DeliveryStatus.PENDING
)