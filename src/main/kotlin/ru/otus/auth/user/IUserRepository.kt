package ru.otus.auth.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IUserRepository : JpaRepository<UserEntity, Long> {

    fun findByLogin(login: String): UserEntity?
}