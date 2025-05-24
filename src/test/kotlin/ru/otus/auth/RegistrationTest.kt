package ru.otus.auth

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.otus.auth.registration.RegistrationDto
import ru.otus.auth.user.IUserJpaRepository
import ru.otus.auth.user.UserEntity
import kotlin.test.Test

private const val USER_ID = 1234567890L
private const val USER_FULL_NAME = "user_full_name"
private const val USER_LOGIN = "user_login"
private const val USER_PASSWORD = "user_password"
private const val USER_PASSWORD_ENCODED =
    """${'$'}2a${'$'}10${'$'}SoQL/B6LYVBfkT.Yq..n8eFiqfvuGz4CjBVc3FkKPqVLRXcrhbHLK"""

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var userJpaRepository: IUserJpaRepository

    private val uri = "/api/v1/registration"
    private val registrationDto = RegistrationDto(
        fullName = USER_FULL_NAME,
        login = USER_LOGIN,
        password = USER_PASSWORD
    )

    @Nested
    inner class SuccessRegistrationTest {

        @BeforeEach
        fun setupMocks() {
            whenever(userJpaRepository.existsByLogin(USER_LOGIN)).thenReturn(false)
            whenever(userJpaRepository.save(any())).thenReturn(
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
            val request = createRegistrationRequest(registrationDto)

            // Act
            val resultActions: ResultActions = mockMvc.perform(request)

            // Assert
            resultActions.andExpect(status().isOk)
        }

        @Test
        fun `body with user id`() {
            // Arrange
            val request = createRegistrationRequest(registrationDto)

            // Act
            val responseContent = mockMvc
                .perform(request)
                .andReturn().response.contentAsString

            // Assert
            assertThat(responseContent).isEqualTo(USER_ID.toString())
        }
    }

    @Nested
    inner class FailRegistrationTest {

        @BeforeEach
        fun setupMocks() {
            whenever(userJpaRepository.existsByLogin(USER_LOGIN)).thenReturn(true)
        }

        @Test
        fun `the answer is 409`() {
            // Arrange
            val request = createRegistrationRequest(registrationDto)

            // Act
            val resultActions: ResultActions = mockMvc.perform(request)

            // Assert
            resultActions.andExpect(status().isConflict)
        }

        @Test
        fun `body with error message`() {
            // Arrange
            val request = createRegistrationRequest(registrationDto)

            // Act
            val responseContent = mockMvc
                .perform(request)
                .andReturn().response.contentAsString

            // Assert
            assertThat(responseContent).isEqualTo("User already exists")
        }
    }

    private fun createRegistrationRequest(dto: RegistrationDto): MockHttpServletRequestBuilder {
        val messageJson = objectMapper.writeValueAsString(dto)
        return post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(messageJson)
    }

}
