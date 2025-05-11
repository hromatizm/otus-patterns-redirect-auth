package ru.otus.auth.registration

import org.springframework.stereotype.Service
import ru.otus.auth.user.IUserRepository
import ru.otus.auth.user.UserMapper
import ru.otus.auth.util.lazyLogger

@Service
class RegistrationService(
    private val userRepository: IUserRepository,
    private val userMapper: UserMapper,
) {

    private val logger by lazyLogger()

    fun register(dto: RegistrationDto): Long? {
        logger.info("User registration started $dto")
        val model = userMapper.toModel(dto)
        val savedUser = userRepository.save(model)
        return savedUser.id.also {
            logger.info("User registration completed. User id: $it")
        }
    }
}
