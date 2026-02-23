package my.app.notificationprovider.controllers

import my.app.notificationprovider.dtos.CreateUserRequest
import my.app.notificationprovider.dtos.LoginRequest
import my.app.notificationprovider.models.User
import my.app.notificationprovider.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("")
@CrossOrigin(origins = ["*"])
class UserController(
    private val userService: UserService
) {

    @GetMapping("/users/get")
    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }

    @GetMapping("/users/get/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<User> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @PostMapping("/users/register")
    fun createUser(@RequestBody createUserRequest: CreateUserRequest): ResponseEntity<User> {
        val user = User(
            username = createUserRequest.username,
            email = createUserRequest.email,
            password = createUserRequest.password,
            role = createUserRequest.role
        )
        return ResponseEntity.ok(userService.createUser(user))
    }

    @PostMapping("/users/login")
    fun loginUser(@RequestBody loginRequest: LoginRequest): ResponseEntity<User> {
        val user = userService.getUserByUsername(loginRequest.username)
            ?: return ResponseEntity.status(401).build()

        if (user.password == loginRequest.password) {
            return ResponseEntity.ok(user)
        }

        return ResponseEntity.status(401).build()
    }
}
