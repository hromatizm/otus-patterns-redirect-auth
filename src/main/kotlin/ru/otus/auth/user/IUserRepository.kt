package ru.otus.auth.user

interface IUserRepository {

    fun findByLogin(login: String): UserModel?

    fun existsByLogin(login: String?): Boolean

    fun save(model: UserModel): UserModel

}