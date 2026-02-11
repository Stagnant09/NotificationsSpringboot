package my.app.notificationprovider.services

import my.app.notificationprovider.models.Notification
import my.app.notificationprovider.models.NotificationPriority
import my.app.notificationprovider.repositories.NotificationRepository
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class NotificationServiceLegacy(
    private val notificationRepository: NotificationRepository
) {

    fun findAll(): List<Notification> {
        return notificationRepository.findAll()
    }

    fun findById(id: String): Notification {
        return notificationRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Notification with id $id not found")
    }

    fun create(notification: Notification): Notification {
        return notificationRepository.save(notification)
    }

    fun update(id: String, notification: Notification): Notification {
        // Check if exists
        if (!notificationRepository.existsById(id)) {
            throw NoSuchElementException("Notification with id $id not found")
        }

        // Create updated notification with the same ID
        val updatedNotification = notification.copy(id = id)
        return notificationRepository.save(updatedNotification)
    }

    fun delete(id: String) {
        if (!notificationRepository.existsById(id)) {
            throw NoSuchElementException("Notification with id $id not found")
        }
        notificationRepository.deleteById(id)
    }

    fun findByStatus(status: NotificationPriority): List<Notification> {
        return notificationRepository.findByPriority(status)
    }

    fun findByTimeRange(startTime: Long, endTime: Long): List<Notification> {
        return notificationRepository.findByTimeBetween(startTime, endTime)
    }

    fun findByTitleContaining(title: String): List<Notification> {
        return notificationRepository.findByTitleContaining(title)
    }

    fun findAllSortedByTime(descending: Boolean = false): List<Notification> {
        val sort = if (descending) {
            Sort.by(Sort.Direction.DESC, "time")
        } else {
            Sort.by(Sort.Direction.ASC, "time")
        }
        return notificationRepository.findAll(sort)
    }

    fun findAllSortedByStatus(descending: Boolean = false): List<Notification> {
        val sort = if (descending) {
            Sort.by(Sort.Direction.DESC, "priority")
        } else {
            Sort.by(Sort.Direction.ASC, "priority")
        }
        return notificationRepository.findAll(sort)
    }
}
