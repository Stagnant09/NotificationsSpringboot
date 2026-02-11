package my.app.notifications.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

actual fun formatTimestamp(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(millis))
}