package my.app.notificationprovider.websocket

import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import tools.jackson.databind.ObjectMapper
import java.util.concurrent.ConcurrentHashMap
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Component
class MyNotificationHandler(private val objectMapper: ObjectMapper) : TextWebSocketHandler() {

    // Map of userId -> Set of WebSocket sessions for that user
    private val userSessions = ConcurrentHashMap<String, MutableSet<WebSocketSession>>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = extractUserId(session)
        if (userId != null) {
            userSessions.computeIfAbsent(userId) { ConcurrentHashMap.newKeySet() }.add(session)
            println("✅ WebSocket connected for user: $userId (Total sessions: ${userSessions[userId]?.size})")
            println("📊 Active user sessions: ${userSessions.keys}")
        } else {
            println("❌ WebSocket connected without userId - closing connection")
            session.close()
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val userId = extractUserId(session)
        if (userId != null) {
            userSessions[userId]?.remove(session)
            if (userSessions[userId]?.isEmpty() == true) {
                userSessions.remove(userId)
            }
            println("🔌 WebSocket disconnected for user: $userId")
        }
    }

    // Send notification to a specific user
    fun sendNotificationToUser(userId: String, notification: Any) {
        println("📤 sendNotificationToUser called for userId: $userId")
        println("📊 Current user sessions: ${userSessions.keys}")

        val sessions = userSessions[userId]
        if (sessions.isNullOrEmpty()) {
            println("⚠️ No active sessions for user: $userId")
            return
        }

        println("📱 Found ${sessions.size} active session(s) for user: $userId")

        try {
            val json = objectMapper.writeValueAsString(notification)
            println("📝 Serialized notification to JSON: $json")

            val message = TextMessage(json)

            sessions.forEach { session ->
                if (session.isOpen) {
                    try {
                        session.sendMessage(message)
                        println("✅ Sent notification to user $userId via WebSocket")
                    } catch (e: Exception) {
                        println("❌ Error sending message to user $userId: ${e.message}")
                        e.printStackTrace()
                    }
                } else {
                    println("⚠️ Session is closed for user $userId")
                }
            }
        } catch (e: Exception) {
            println("❌ Error serializing notification: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun extractUserId(session: WebSocketSession): String? {
        // Extract userId from URI path: /ws/notifications/{userId}
        val uri = session.uri ?: return null
        val pathSegments = uri.path.split("/")
        val userId = pathSegments.lastOrNull()
        println("🔍 Extracted userId from path: $userId (full path: ${uri.path})")
        return userId
    }
}

@Configuration
@EnableWebSocket
class WebSocketConfig(private val myNotificationHandler: MyNotificationHandler) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        // User-specific endpoint
        registry.addHandler(myNotificationHandler, "/ws/notifications/{userId}")
            .setAllowedOrigins("*")

        // Keep old endpoint for backward compatibility (optional)
        registry.addHandler(myNotificationHandler, "/ws/raw-notifications")
            .setAllowedOrigins("*")
    }
}