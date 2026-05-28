package com.am.lapcart

data class LoginResponse(

    val success:Boolean,
    val message:String,
    val data: Data

){

    data class Data(

        val customer: Customer,
        val token:String,
        val token_type:String

    )

    data class Customer(

        val name:String,
        val email:String,
        val phone_number:String,
        val profile_photo:String?,
        val status:String,
        val address: Address,
        val referral_code:String

    )

    data class Address(

        val address:String,
        val city:String,
        val state:String,
        val pincode:String,
        val is_primary:String

    )

}