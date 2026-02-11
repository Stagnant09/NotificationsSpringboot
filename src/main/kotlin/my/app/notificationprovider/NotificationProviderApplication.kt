package my.app.notificationprovider

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NotificationProviderApplication

fun main(args: Array<String>) {
    runApplication<NotificationProviderApplication>(*args)
}
