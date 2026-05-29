package com.am.lapcart.models


data class CategoryModel(

    val id: Int,

    val name: String,

    val status: Boolean,

    val featured: String,

    val logo_path: String,

    val banner_path: String?,

    val created_at: String,

    val updated_at: String

)

data class CategoryResponse(

    val success: Boolean,

    val data: List<CategoryModel>

)