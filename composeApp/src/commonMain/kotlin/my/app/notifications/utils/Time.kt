package my.app.notifications.utils

import kotlinx.datetime.*
import kotlinx.datetime.format.*

fun String.convertToDate() = formatTimestamp(this.toLong())

expect fun formatTimestamp(millis: Long): String