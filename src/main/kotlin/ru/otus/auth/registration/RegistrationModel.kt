package ru.otus.auth.registration

data class RegistrationModel(
    val fullName: String,
    val login: String,
    val password: String
)