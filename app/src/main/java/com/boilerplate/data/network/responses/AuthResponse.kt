package com.boilerplate.data.network.responses

import com.boilerplate.data.db.entities.User

data class AuthResponse(
    val data: Any?,
    val status: String?,
    val message: String?
)