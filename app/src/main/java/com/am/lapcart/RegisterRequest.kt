package com.am.lapcart

data class RegisterRequest(
    val address: String,
    val city: String,
    val email: String,
    val name: String,
    val password: String,
    val phone_number: String,
    val pincode: String,
    val referral_code: String,
    val state: String
)