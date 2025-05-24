package ru.otus.auth.exception

class BadCredentialsException : RuntimeException("Incorrect login or password")

class UserAlreadyExistsException : RuntimeException("User already exists")