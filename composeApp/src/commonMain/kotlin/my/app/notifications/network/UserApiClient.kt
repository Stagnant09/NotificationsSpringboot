package my.app.notifications.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import my.app.notifications.models.User

class UserApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
    }

    private val baseUrl = "http://127.0.0.1:8080/users"

    suspend fun registerUser(username: String, email: String, password: String): User? {
        println("=== Starting registration ===")
        println("URL: $baseUrl/register")

        return try {
            val requestBody = mapOf(
                "username" to username,
                "email" to email,
                "password" to password,
                "role" to "USER"  // Send as String, not enum
            )
            println("Request body: $requestBody")

            val response = client.post("$baseUrl/register") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            println("Response status: ${response.status}")
            val responseText = response.body<String>()
            println("Response body: $responseText")

            if (response.status.isSuccess()) {
                // Try to parse the response
                val user: User = Json.decodeFromString(responseText)
                println("User registered successfully: $user")
                user
            } else {
                println("Registration failed with status: ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("=== Registration error ===")
            println("Error type: ${e::class.simpleName}")
            println("Error message: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun loginUser(username: String, password: String): User? {
        return try {
            val response: HttpResponse = client.post("$baseUrl/login") {
                contentType(ContentType.Application.Json)
                setBody("""
                    {
                        "username": "$username",
                        "password": "$password"
                    }
                """.trimIndent())
            }

            println("Login Status: ${response.status}")

            if (response.status == HttpStatusCode.OK) {
                response.body<User>()
            } else {
                null
            }
        } catch (e: Exception) {
            println("Login Error: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}