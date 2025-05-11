package ru.otus.auth.login

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.otus.auth.exception.BadCredentialsException
import ru.otus.auth.user.IUserRepository
import ru.otus.auth.user.UserModel
import ru.otus.auth.util.lazyLogger
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class LoginService(
    private val encoder: PasswordEncoder,
    private val keyProvider: KeyProvider,
    private val userRepository: IUserRepository,
) {

    private val logger by lazyLogger()

    fun login(dto: LoginDto): String {
        logger.info("User login started $dto")
        val user = findUserOrElseThrow(dto.login)
        checkPassword(rawPass = dto.password, encodedPass = user.encodedPassword)
        return buildJwt(user).also {
            logger.info("User login finished. User id: ${user.id}")
        }
    }

    private fun checkPassword(rawPass: String, encodedPass: String?) {
        val passwordIsCorrect = encoder.matches(rawPass, encodedPass)
        logger.info("Correct password: $passwordIsCorrect")
        if (!passwordIsCorrect) throw BadCredentialsException()
    }

    private fun findUserOrElseThrow(login: String): UserModel {
        return userRepository.findByLogin(login)
            ?: throw BadCredentialsException()
    }

    private fun buildJwt(user: UserModel): String {
        val now = Instant.now()
        val expiry = now.plus(30, ChronoUnit.DAYS)
        return Jwts.builder()
            .setSubject(user.id.toString())
            .claim("userLogin", user.login)
            .claim("userFullName", user.fullName)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .signWith(keyProvider.privateKey, SignatureAlgorithm.RS256)
            .compact()
            .also {
                logger.info("Jwt token created. User id: ${user.id}")
            }
    }
}
