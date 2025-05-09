package ru.otus.auth.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import ru.otus.auth.registration.RegistrationDto

@Component
class UserMapper(
    private val encoder: PasswordEncoder
) {

    fun toEntity(dto: RegistrationDto) =
        UserEntity(
            fullName = dto.fullName,
            login = dto.login,
            encodedPassword = encoder.encode(dto.password),
        )
}
