package my.app.notificationprovider.repositories

import my.app.notificationprovider.models.Notification
import my.app.notificationprovider.models.NotificationPriority
import my.app.notificationprovider.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, String> {

    fun findByPriority(priority: NotificationPriority): List<Notification>

    fun findByTimeBetween(startTime: Long, endTime: Long): List<Notification>

    fun findByTitleContaining(title: String): List<Notification>

    fun findNotificationsByUser(user: User): MutableList<Notification>

}