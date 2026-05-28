package com.am.lapcart

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "cart.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS cart (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "product_id INTEGER," +
                    "img_url TEXT," +
                    "title TEXT," +
                    "price TEXT," +
                    "quantity INTEGER)"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

    }

    fun insertProduct(model: CartModel) {

        val db = writableDatabase

        val values = ContentValues()

        values.put("product_id", model.product_id)
        values.put("img_url", model.img_url)
        values.put("title", model.title)
        values.put("price", model.price)
        values.put("quantity", model.quantity)

        db.insert("cart", null, values)

    }

    fun getProducts(): ArrayList<CartModel> {
        val list = ArrayList<CartModel>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM cart",
            null
        )

        while (cursor.moveToNext()) {
            val model = CartModel(
                id = cursor.getInt(0),
                product_id = cursor.getInt(1),
                img_url = cursor.getString(2),
                title = cursor.getString(3),
                price = cursor.getString(4),
                quantity = cursor.getInt(5)
            )
            list.add(model)
        }
        cursor.close()
        return list
    }

    fun deleteProduct(id: Int){

        val db = writableDatabase
        db.delete("cart", "product_id=?", arrayOf(id.toString()))
    }

    fun updateQuantity(productId: Int , qty: Int){

        val db = writableDatabase
        val values = ContentValues()
        values.put("quantity", qty)
        db.update("cart", values, "product_id=?", arrayOf(productId.toString()))

    }

    fun isProductInCart(productId: Int): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM cart WHERE product_id = ?",
            arrayOf(productId.toString())
        )
        val isProductInCart = cursor.count > 0
        cursor.close()
        return isProductInCart


}
    fun increaseQuantity(productId: Int) {
        val db = writableDatabase

        db.execSQL(
            "UPDATE cart SET quantity = quantity + 1 WHERE product_id = $productId")
    }
}