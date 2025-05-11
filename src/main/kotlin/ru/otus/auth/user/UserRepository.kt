package ru.otus.auth.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface IUserJpaRepository : JpaRepository<UserEntity, Long> {

    fun selectByLogin(login: String): UserEntity?
}

@Repository
class UserRepository(
    private val jpaDelegate: IUserJpaRepository,
    private val mapper: UserMapper,
) : IUserRepository {

    override fun findByLogin(login: String): UserModel? {
        val entity = jpaDelegate.selectByLogin(login)
        return entity?.let { mapper.toModel(it) }
    }

    override fun save(model: UserModel): UserModel {
        val entity = mapper.toEntity(model)
        val savedEntity = jpaDelegate.save(entity)
        return mapper.toModel(savedEntity)
    }
}