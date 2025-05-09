package ru.otus.auth.login

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.auth.util.lazyLogger

@RestController
@RequestMapping("/api/v1")
class LoginController(
    private val loginService: LoginService
) {

    private val logger by lazyLogger()

    @PostMapping("/login")
    fun register(@RequestBody dto: LoginDto): ResponseEntity<String> {
        logger.info("Login request received: $dto")
        val jwt = loginService.login(dto)
        return ResponseEntity.ok().body(jwt)
    }
}