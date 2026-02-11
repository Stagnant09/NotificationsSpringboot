package my.app.notifications.dtos

import my.app.notifications.models.DeliveryStatus

data class UpdateStatusRequest(
    val status: DeliveryStatus
)