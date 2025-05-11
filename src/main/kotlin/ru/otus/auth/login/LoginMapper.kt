package ru.otus.auth.login

fun LoginDto.toModel() =
    LoginModel(
        login = login,
        password = password,
    )
