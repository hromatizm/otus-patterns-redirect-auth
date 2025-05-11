package ru.otus.auth.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity()
@Table(name = "users")
data class UserEntity(
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "full_name", nullable = false)
    var fullName: String? = null,

    @Column(name = "login", nullable = false)
    var login: String? = null,

    @Column(name = "password", nullable = false)
    var encodedPassword: String? = null,
)