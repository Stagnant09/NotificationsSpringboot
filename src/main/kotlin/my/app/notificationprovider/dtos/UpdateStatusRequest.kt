package my.app.notificationprovider.dtos

import my.app.notificationprovider.models.DeliveryStatus

data class UpdateStatusRequest(
    val status: DeliveryStatus
)