package my.app.notificationprovider.services

import my.app.notificationprovider.models.DeliveryStatus
import my.app.notificationprovider.models.Notification
import my.app.notificationprovider.models.NotificationPriority
import my.app.notificationprovider.repositories.NotificationRepository
import my.app.notificationprovider.websocket.MyNotificationHandler
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val repository: NotificationRepository,
    private val notificationHandler: MyNotificationHandler
) {

    fun getAllNotifications(): List<Notification> {
        return repository.findAll().sortedByDescending { it.time }
    }

    fun getNotificationById(id: String): Notification? {
        return repository.findById(id).orElse(null)
    }

    fun createNotification(title: String, message: String, priority: NotificationPriority): Notification {
        val notification = Notification(title = title, message = message, priority = priority)
        val saved = repository.save(notification)

        // This is the "Auto-Update" trigger!
        notificationHandler.broadcastNotification(saved)

        return saved
    }

    fun updateNotificationStatus(id: String, status: DeliveryStatus): Notification? {
        val notification = repository.findById(id).orElse(null) ?: return null
        val updated = notification.copy(deliveryStatus = status)
        val saved = repository.save(updated)

        // Broadcast the update
        broadcastNotification(saved)

        return saved
    }

    fun deleteNotification(id: String): Boolean {
        return if (repository.existsById(id)) {
            repository.deleteById(id)
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
}