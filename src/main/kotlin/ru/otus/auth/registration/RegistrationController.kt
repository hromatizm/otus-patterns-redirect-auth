package ru.otus.auth.registration

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.auth.util.lazyLogger

@RestController
@RequestMapping("/api/v1")
class RegistrationController(
    private val mapper: RegistrationMapper,
    private val registrationService: RegistrationService,
) {

    private val logger by lazyLogger()

    @PostMapping("/registration")
    fun register(@RequestBody dto: RegistrationDto): ResponseEntity<Long> {
        logger.info("Registration request received: $dto")
        val model = mapper.toModel(dto)
        val userId = registrationService.register(model)
        return ResponseEntity.ok().body(userId)
    }
}
