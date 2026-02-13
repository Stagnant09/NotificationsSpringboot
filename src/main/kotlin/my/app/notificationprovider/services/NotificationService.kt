package my.app.notificationprovider.services

import my.app.notificationprovider.models.DeliveryStatus
import my.app.notificationprovider.models.Notification
import my.app.notificationprovider.models.NotificationPriority
import my.app.notificationprovider.models.User
import my.app.notificationprovider.repositories.NotificationRepository
import my.app.notificationprovider.repositories.UserRepository
import my.app.notificationprovider.websocket.MyNotificationHandler
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    private val notificationHandler: MyNotificationHandler
) {

    fun getAllNotifications(): List<Notification> {
        return notificationRepository.findAll().sortedByDescending { it.time }
    }

    fun getNotificationById(id: String): Notification? {
        return notificationRepository.findById(id).orElse(null)
    }

    fun createNotification(
        title: String,
        message: String,
        priority: NotificationPriority,
        userId: String
    ): Notification {
        // Find the user by ID
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found with id: $userId") }

        // Create notification
        val notification = Notification(
            title = title,
            message = message,
            priority = priority,
            user = user
        )

        return notificationRepository.save(notification)
    }

    fun updateNotificationStatus(id: String, status: DeliveryStatus): Notification? {
        val notification = notificationRepository.findById(id).orElse(null) ?: return null
        val updated = notification.copy(deliveryStatus = status)
        val saved = notificationRepository.save(updated)

        // Broadcast the update
        broadcastNotification(saved)

        return saved
    }

    fun deleteNotification(id: String): Boolean {
        return if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id)
            // Broadcast deletion
            //messagingTemplate.convertAndSend("/topic/notifications/deleted", id)
            true
        } else {
            false
        }
    }

    private fun broadcastNotification(notification: Notification) {
        // Send notification to all subscribers of /topic/notifications
        //messagingTemplate.convertAndSend("/topic/notifications", notification)
    }

    fun getNotificationsByUser(user: User) : List<Notification> {
        return notificationRepository.findNotificationsByUser(user)
    }
}