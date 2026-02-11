package my.app.notificationprovider.repositories

import my.app.notificationprovider.models.Notification
import my.app.notificationprovider.models.NotificationPriority
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, String> {

    // Spring Data JPA will automatically implement these based on method names
    fun findByPriority(priority: NotificationPriority): List<Notification>

    fun findByTimeBetween(startTime: Long, endTime: Long): List<Notification>

    fun findByTitleContaining(title: String): List<Notification>

    // For sorting, we'll use the repository's built-in methods in the service
}