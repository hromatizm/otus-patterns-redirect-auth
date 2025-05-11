package ru.otus.auth.user

fun UserModel.toEntity() =
    UserEntity(
        id = id,
        fullName = fullName,
        login = login,
        encodedPassword = encodedPassword,
    )

fun UserEntity.toModel() =
    UserModel(
        id = id,
        fullName = fullName,
        login = login,
        encodedPassword = encodedPassword,
    )
