package ru.otus.auth.user

data class UserModel(
    val id: Long? = null,
    val fullName: String? = null,
    val login: String? = null,
    val encodedPassword: String? = null,
)