package com.robosh.distancebetween.saveuser.repository

import com.robosh.distancebetween.model.User

interface SaveUserRepository {
    fun saveUser(user: User): User
}