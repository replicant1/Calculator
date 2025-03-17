package com.example.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface UserRepository {
    fun getUser(userId: Int) : String
}

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val userName = MutableLiveData<String>()

    fun loadUser(userId: Int) {
        val name = userRepository.getUser(userId)
        userName.value = name
    }
}