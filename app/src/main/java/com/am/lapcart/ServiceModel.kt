package com.am.lapcart

data class ServiceResponse(

    val success: Boolean,

    val data: List<ServiceModel>

)



data class ServiceModel(

    val id: Int,

    val subcategory_id: Int,

    val title: String,

    val description: String,

    val status: Int,

    val price: String,

    val duration_minutes: Int,

    val img_url: String,

    val is_recommended: Boolean,

    val is_best: Boolean,

    val subcategory: Subcategory

)



data class Subcategory(

    val id: Int,

    val category_id: Int,

    val name: String,

    val logo_path: String?,

    val banner_path: String?,

    val description: String?,

    val status: Boolean

)