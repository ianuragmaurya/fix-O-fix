package com.am.lapcart

data class RegisterResponse(
    val errors: Errors,
    val message: String,
    val success: Boolean
)