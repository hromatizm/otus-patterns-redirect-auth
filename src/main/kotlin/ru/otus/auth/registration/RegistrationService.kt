package ru.otus.auth.registration

import org.springframework.stereotype.Service
import ru.otus.auth.user.IUserRepository
import ru.otus.auth.user.UserModel
import ru.otus.auth.util.lazyLogger

@Service
class RegistrationService(
    private val userRepository: IUserRepository,
) {

    private val logger by lazyLogger()

    fun register(model: UserModel): Long? {
        logger.info("User registration started $model")
        val savedUser = userRepository.save(model)
        return savedUser.id.also {
            logger.info("User registration completed. User id: $it")
        }
    }
}
