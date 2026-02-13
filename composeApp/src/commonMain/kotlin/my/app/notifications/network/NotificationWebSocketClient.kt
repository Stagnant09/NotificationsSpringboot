package my.app.notifications.network

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import my.app.notifications.models.Notification


class NotificationWebSocketClient {
    private val client = HttpClient {
        install(WebSockets)
    }

    private val _notifications = MutableSharedFlow<Notification?>(replay = 0)
    val notifications = _notifications.asSharedFlow()

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState = _connectionState.asStateFlow()

    private val _deletedNotificationId = MutableSharedFlow<String?>(replay = 0)
    val deletedNotificationId = _deletedNotificationId.asSharedFlow()

    private var session: DefaultClientWebSocketSession? = null

    enum class ConnectionState { CONNECTED, DISCONNECTED, CONNECTING }

    suspend fun connect(scope: CoroutineScope, userId: String) {
        if (_connectionState.value == ConnectionState.CONNECTED) {
            println("Already connected to WebSocket")
            return
        }

        _connectionState.value = ConnectionState.CONNECTING
        println("Attempting to connect to WebSocket for user: $userId")

        scope.launch {
            try {
                // Connect to user-specific WebSocket endpoint
                client.webSocket(
                    method = HttpMethod.Get,
                    host = "127.0.0.1",
                    port = 8080,
                    path = "/ws/notifications/$userId"
                ) {
                    session = this
                    _connectionState.value = ConnectionState.CONNECTED
                    println("✅ WebSocket CONNECTED for user: $userId")

                    try {
                        for (frame in incoming) {
                            if (frame is io.ktor.websocket.Frame.Text) {
                                val text = frame.readText()
                                println("📩 Received WebSocket message: $text")

                                try {
                                    val notification = Json.decodeFromString<Notification>(text)
                                    println("✅ Parsed notification: ${notification.title}")
                                    _notifications.emit(notification)
                                    println("✅ Emitted notification to flow")
                                } catch (e: Exception) {
                                    println("❌ Error parsing WS message: ${e.message}")
                                    e.printStackTrace()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        println("❌ Error reading WebSocket frames: ${e.message}")
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                println("❌ WebSocket Error: ${e.message}")
                e.printStackTrace()
            } finally {
                _connectionState.value = ConnectionState.DISCONNECTED
                session = null
                println("WebSocket disconnected")
            }
        }
    }

    suspend fun disconnect() {
        session?.close()
        _connectionState.value = ConnectionState.DISCONNECTED
        println("WebSocket manually disconnected")
    }
}