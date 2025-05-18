package ru.otus.auth.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface IUserJpaRepository : JpaRepository<UserEntity, Long> {

    fun findByLogin(login: String): UserEntity?
}

@Repository
class UserRepository(
    private val jpaDelegate: IUserJpaRepository,
) : IUserRepository {

    override fun findByLogin(login: String): UserModel? {
        val entity = jpaDelegate.findByLogin(login)
        return entity?.toModel()
    }

    override fun save(model: UserModel): UserModel {
        val savedEntity = jpaDelegate.save(model.toEntity())
        return savedEntity.toModel()
    }

}
