package ru.otus.auth

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.otus.auth.login.KeyProvider
import ru.otus.auth.login.LoginDto
import ru.otus.auth.user.IUserRepository
import ru.otus.auth.user.UserEntity
import kotlin.test.Test

private const val USER_ID = 1234567890L
private const val USER_FULL_NAME = "user_full_name"
private const val USER_LOGIN = "user_login"
private const val USER_PASSWORD = "user_password"
private const val USER_PASSWORD_ENCODED = """${'$'}2a${'$'}10${'$'}SoQL/B6LYVBfkT.Yq..n8eFiqfvuGz4CjBVc3FkKPqVLRXcrhbHLK"""
private const val BAD_CREDENTIALS_ERROR = "Incorrect login or password"

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var keyProvider: KeyProvider

    @MockitoBean
    lateinit var userRepository: IUserRepository

    private val uri = "/api/v1/login"

    @Nested
    inner class SuccessfulLoginTest {

        private val loginDto = LoginDto(
            login = USER_LOGIN,
            password = USER_PASSWORD
        )

        @BeforeEach
        fun setupMocks() {
            whenever(userRepository.findByLogin(any())).thenReturn(
                UserEntity(
                    id = USER_ID,
                    fullName = USER_FULL_NAME,
                    login = USER_LOGIN,
                    encodedPassword = USER_PASSWORD_ENCODED
                )
            )
        }

        @Test
        fun `the answer is OK`() {
            // Arrange
            val request = createLoginRequest(loginDto)

            // Act
            val resultActions: ResultActions = mockMvc.perform(request)

            // Assert
            resultActions.andExpect(status().isOk)
        }

        @Test
        fun `body with jwt`() {
            // Arrange
            val request = createLoginRequest(loginDto)

            // Act
            val jwt = mockMvc
                .perform(request)
                .andReturn().response.contentAsString

            // Assert
            assertThat(jwt).isNotBlank
            assertThat(jwt.split(".")).hasSize(3)

            val claims = Jwts.parserBuilder()
                .setSigningKey(keyProvider.publicKey)
                .build()
                .parseClaimsJws(jwt)
                .body
            assertThat(claims.subject).isEqualTo(USER_ID.toString())
            assertThat(claims["userLogin"]).isEqualTo(USER_LOGIN)
            assertThat(claims["userFullName"]).isEqualTo(USER_FULL_NAME)
        }

    }

    @Nested
    inner class WrongLoginTest {

        @BeforeEach
        fun setupMocks() {
            whenever(userRepository.findByLogin(any())).thenReturn(null)
        }

        val loginDto = LoginDto(
            login = USER_LOGIN,
            password = USER_PASSWORD
        )

        @Test
        fun `the answer is 401`() {
            // Arrange
            val request = createLoginRequest(loginDto)

            // Act
            val resultActions: ResultActions = mockMvc.perform(request)

            // Assert
            resultActions.andExpect(status().isUnauthorized)
        }

        @Test
        fun `body with error message`() {
            // Arrange
            val request = createLoginRequest(loginDto)

            // Act
            val errorMsg = mockMvc
                .perform(request)
                .andReturn().response.contentAsString

            // Assert
            assertThat(errorMsg).isEqualTo(BAD_CREDENTIALS_ERROR)
        }
    }

    @Nested
    inner class WrongPasswordTest {

        @BeforeEach
        fun setupMocks() {
            whenever(userRepository.findByLogin(any())).thenReturn(
                UserEntity(
                    id = USER_ID,
                    fullName = USER_FULL_NAME,
                    login = USER_LOGIN,
                    encodedPassword = USER_PASSWORD_ENCODED
                )
            )        }

        val loginDto = LoginDto(
            login = USER_LOGIN,
            password = "wrong_password"
        )

        @Test
        fun `the answer is 401`() {
            // Arrange
            val request = createLoginRequest(loginDto)

            // Act
            val resultActions: ResultActions = mockMvc.perform(request)

            // Assert
            resultActions.andExpect(status().isUnauthorized)
        }

        @Test
        fun `body with error message`() {
            // Arrange
            val request = createLoginRequest(loginDto)

            // Act
            val errorMsg = mockMvc
                .perform(request)
                .andReturn().response.contentAsString

            // Assert
            assertThat(errorMsg).isEqualTo(BAD_CREDENTIALS_ERROR)
        }
    }

    private fun createLoginRequest(dto: LoginDto): MockHttpServletRequestBuilder {
        val messageJson = objectMapper.writeValueAsString(dto)
        return post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(messageJson)
    }

}
