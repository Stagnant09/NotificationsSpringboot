package my.app.notificationprovider.controllers

import my.app.notificationprovider.dtos.CreateNotificationRequest
import my.app.notificationprovider.dtos.UpdateStatusRequest
import my.app.notificationprovider.models.DeliveryStatus
import my.app.notificationprovider.models.Notification
import my.app.notificationprovider.models.NotificationPriority
import my.app.notificationprovider.services.NotificationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = ["*"]) // For development - restrict in production
class NotificationController(
    private val notificationService: NotificationService
) {

    @GetMapping
    fun getAllNotifications(): List<Notification> {
        return notificationService.getAllNotifications()
    }

    @GetMapping("/{id}")
    fun getNotificationById(@PathVariable id: String): ResponseEntity<Notification> {
        val notification = notificationService.getNotificationById(id)
        return if (notification != null) {
            ResponseEntity.ok(notification)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createNotification(@RequestBody request: CreateNotificationRequest): ResponseEntity<Notification> {
        val notification = notificationService.createNotification(
            title = request.title,
            message = request.message,
            priority = request.priority
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(notification)
    }

    @PatchMapping("/{id}/status")
    fun updateNotificationStatus(
        @PathVariable id: String,
        @RequestBody request: UpdateStatusRequest
    ): ResponseEntity<Notification> {
        val updated = notificationService.updateNotificationStatus(id, request.status)
        return if (updated != null) {
            ResponseEntity.ok(updated)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteNotification(@PathVariable id: String): ResponseEntity<Void> {
        return if (notificationService.deleteNotification(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

