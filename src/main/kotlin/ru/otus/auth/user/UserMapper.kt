package ru.otus.auth.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import ru.otus.auth.registration.RegistrationDto

@Component
class UserMapper(
    private val encoder: PasswordEncoder
) {

    fun toModel(dto: RegistrationDto) =
        UserModel(
            fullName = dto.fullName,
            login = dto.login,
            encodedPassword = encoder.encode(dto.password),
        )

    fun toEntity(model: UserModel) =
        UserEntity(
            id = model.id,
            fullName = model.fullName,
            login = model.login,
            encodedPassword = model.encodedPassword,
        )

    fun toModel(entity: UserEntity) =
        UserModel(
            id = entity.id,
            fullName = entity.fullName,
            login = entity.login,
            encodedPassword = entity.encodedPassword,
        )
}
