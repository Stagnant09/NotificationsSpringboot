package my.app.notificationprovider.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.handler.TextWebSocketHandler
import tools.jackson.databind.ObjectMapper
import java.util.concurrent.CopyOnWriteArrayList

@Component
class MyNotificationHandler(private val objectMapper: ObjectMapper) : TextWebSocketHandler() {

    // Thread-safe list to keep track of active desktop/mobile clients
    private val sessions = CopyOnWriteArrayList<WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: org.springframework.web.socket.CloseStatus) {
        sessions.remove(session)
    }

    // NotificationService calls this method whenever a new notification is created
    fun broadcastNotification(notification: Any) {
        val json = objectMapper.writeValueAsString(notification)
        val message = TextMessage(json)

        sessions.forEach { session ->
            if (session.isOpen) {
                session.sendMessage(message)
            }
        }
    }
}

@Configuration
@EnableWebSocket
class WebSocketConfig(private val myNotificationHandler: MyNotificationHandler) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(myNotificationHandler, "/ws/raw-notifications")
            .setAllowedOrigins("*") // Allows your Desktop app to connect
    }
}