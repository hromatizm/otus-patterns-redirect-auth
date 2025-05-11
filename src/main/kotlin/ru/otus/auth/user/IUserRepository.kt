package ru.otus.auth.user

interface IUserRepository {

    fun findByLogin(login: String): UserModel?

    fun save(model: UserModel): UserModel

}