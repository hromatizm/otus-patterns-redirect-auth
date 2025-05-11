package ru.otus.auth.registration

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import ru.otus.auth.user.UserModel

@Component
class RegistrationMapper(
    private val encoder: PasswordEncoder
) {

    fun toModel(dto: RegistrationDto) =
        UserModel(
            fullName = dto.fullName,
            login = dto.login,
            encodedPassword = encoder.encode(dto.password),
        )
}
