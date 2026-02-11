package my.app.notifications

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform