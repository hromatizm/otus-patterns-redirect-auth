package ru.otus.auth.registration

data class RegistrationDto(
    val fullName: String,
    val login: String,
    val password: String
)