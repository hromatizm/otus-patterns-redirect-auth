package ru.otus.auth.registration

import org.springframework.stereotype.Service
import ru.otus.auth.user.UserMapper
import ru.otus.auth.user.IUserRepository
import ru.otus.auth.util.lazyLogger
import kotlin.getValue

@Service
class RegistrationService(
    private val IUserRepository: IUserRepository,
    private val userMapper: UserMapper,
) {

    private val logger by lazyLogger()

    fun register(dto: RegistrationDto): Long? {
        logger.info("User registration started $dto")
        val entity = userMapper.toEntity(dto)
        val savedUser = IUserRepository.save(entity)
        return savedUser.id.also {
            logger.info("User registration completed. User id: $it")
        }
    }
}
