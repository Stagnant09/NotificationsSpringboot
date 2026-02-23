# Notification System

A full-stack notification system composed of two complementary projects: a backend service that manages and distributes notifications, and a multiplatform client application that displays them in real time.

---

## NotificationProvider

A RESTful backend built with Spring Boot and Kotlin. It manages users and notifications stored in an H2 database via JPA, and exposes a standard REST API for creating, retrieving, updating, and deleting notifications. Each notification carries a title, message, timestamp, priority level (Normal, Urgent, or Critical), and a delivery status (Pending, Sent, Failed, or Read).

Beyond the HTTP API, the service includes a WebSocket server that pushes notifications to connected clients in real time. Sessions are tracked per user, so messages are routed to only the relevant recipient. A legacy endpoint is also maintained for backward compatibility.

**Tech stack:** Kotlin, Spring Boot, Spring Data JPA, Spring WebSocket, H2 Database, Gradle.

---

## NotificationsDisplay

A Kotlin Multiplatform client application targeting Android and desktop (JVM), with groundwork in place for future web targets. The UI is built entirely with Compose Multiplatform and follows a standard MVVM architecture with Jetpack Navigation.

The application flow begins with a login or registration screen, then routes the authenticated user to a main notification list. From there, individual notifications can be opened in a detail view. The client communicates with the backend via Ktor, using both HTTP requests for CRUD operations and a WebSocket connection for live updates.

**Tech stack:** Kotlin Multiplatform, Compose Multiplatform, Ktor (HTTP + WebSocket), kotlinx.serialization, AndroidX Navigation, Gradle.
