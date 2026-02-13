package my.app.notifications.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import my.app.notifications.models.DeliveryStatus
import my.app.notifications.models.Notification
import my.app.notifications.models.NotificationPriority
import my.app.notifications.models.User

class NotificationApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val baseUrl = "http://127.0.0.1:8080/api/notifications"

    suspend fun getAllNotifications(): List<Notification> {
        return try {
            client.get(baseUrl).body<List<Notification>>()
        } catch (e: Exception) {
            println("Error fetching notifications: ${e.message}")
            emptyList()
        }
    }

    suspend fun getNotificationById(id: String): Notification? {
        return try {
            client.get("$baseUrl/$id").body()
        } catch (e: Exception) {
            println("Error fetching notification: ${e.message}")
            null
        }
    }

    suspend fun getNotificationsByUser(user: User): List<Notification>? {
        return try {
            println("📥 Fetching notifications for user: ${user.id}")

            val response: HttpResponse = client.get("$baseUrl/user/${user.id}")

            println("📡 Response status: ${response.status}")
            val responseText = response.bodyAsText()
            println("📄 Response body: ${responseText.take(200)}...")

            if (response.status == HttpStatusCode.OK) {
                Json.decodeFromString<List<Notification>>(responseText)
            } else {
                println("❌ Error response: $responseText")
                null
            }
        } catch (e: Exception) {
            println("❌ Error fetching notifications: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun createNotification(
        title: String,
        message: String,
        priority: NotificationPriority = NotificationPriority.NORMAL
    ): Notification? {
        return try {
            client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "title" to title,
                    "message" to message,
                    "priority" to priority.name
                ))
            }.body()
        } catch (e: Exception) {
            println("Error creating notification: ${e.message}")
            null
        }
    }

    suspend fun updateNotificationStatus(
        id: String,
        status: DeliveryStatus
    ): Notification?{
        return try {
            client.patch("$baseUrl/${id}/status") {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "status" to status.name
                    )
                )
            }.body()
        }
        catch (e: Exception) {
            println("Error updating notification: ${e.message}")
            null
        }
    }
}