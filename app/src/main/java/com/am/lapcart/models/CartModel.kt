package com.am.lapcart.models

data class CartModel(

    var id:Int = 0,
    var product_id:Int = 0,
    var img_url:String = "",
    var title:String = "",
    var price:String = "",
    var quantity:Int = 1

)