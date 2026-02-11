package my.app.notificationprovider.controllers

import my.app.notificationprovider.dtos.CreateNotificationRequest
import my.app.notificationprovider.models.Notification
import my.app.notificationprovider.models.NotificationPriority
import my.app.notificationprovider.models.User
import my.app.notificationprovider.services.NotificationServiceLegacy
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*



@RestController
@RequestMapping("/api/notifications")
@Profile("legacy")
class NotificationControllerLegacy(
    private val notificationServiceLegacy: NotificationServiceLegacy
) {

    @GetMapping("/")
    fun getAllNotifications(): ResponseEntity<List<Notification>> {
        return ResponseEntity.ok(notificationServiceLegacy.findAll())
    }

    @GetMapping("/{id}")
    fun getNotificationById(@PathVariable id: String): ResponseEntity<Notification> {
        return ResponseEntity.ok(notificationServiceLegacy.findById(id))
    }

    @PostMapping
    fun createNotification(
        @RequestBody request: CreateNotificationRequest
    ): ResponseEntity<Notification> {
        val notification = Notification(
            title = request.title,
            message = request.message,
            priority = request.priority
        )
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(notificationServiceLegacy.create(notification))
    }

    @PatchMapping("/{id}")
    fun updateNotification(
        @PathVariable id: String,
        @RequestBody notification: Notification
    ): ResponseEntity<Notification> {
        return ResponseEntity.ok(notificationServiceLegacy.update(id, notification))
    }

    @DeleteMapping("/{id}")
    fun deleteNotification(@PathVariable id: String): ResponseEntity<Void> {
        notificationServiceLegacy.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/status/{status}")
    fun getNotificationsByStatus(
        @PathVariable status: NotificationPriority
    ): ResponseEntity<List<Notification>> {
        return ResponseEntity.ok(notificationServiceLegacy.findByStatus(status))
    }

    @GetMapping("/search")
    fun getNotificationsByTimeFrame(
        @RequestParam startTime: Long,
        @RequestParam endTime: Long
    ): ResponseEntity<List<Notification>> {
        return ResponseEntity.ok(notificationServiceLegacy.findByTimeRange(startTime, endTime))
    }

    @GetMapping("/search/title")
    fun searchByTitle(@RequestParam title: String): ResponseEntity<List<Notification>> {
        return ResponseEntity.ok(notificationServiceLegacy.findByTitleContaining(title))
    }

    @GetMapping("/sorted/time")
    fun getSortedByTime(
        @RequestParam(defaultValue = "false") descending: Boolean
    ): ResponseEntity<List<Notification>> {
        return ResponseEntity.ok(notificationServiceLegacy.findAllSortedByTime(descending))
    }

    @GetMapping("/sorted/status")
    fun getSortedByStatus(
        @RequestParam(defaultValue = "false") descending: Boolean
    ): ResponseEntity<List<Notification>> {
        return ResponseEntity.ok(notificationServiceLegacy.findAllSortedByStatus(descending))
    }

}