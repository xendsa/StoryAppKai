package com.kai.intermediatestatus.data.local

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)