package com.am.lapcart.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.am.lapcart.models.CartModel

class DBHelper(context: Context) : SQLiteOpenHelper(context, "cart.db", null, 1) {

    companion object {
        const val TABLE_CART = "cart"
        const val COLUMN_ID = "id"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_IMG_URL = "img_url"
        const val COLUMN_TITLE = "title"
        const val COLUMN_PRICE = "price"
        const val COLUMN_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_CART (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_PRODUCT_ID INTEGER," +
                    "$COLUMN_IMG_URL TEXT," +
                    "$COLUMN_TITLE TEXT," +
                    "$COLUMN_PRICE TEXT," +
                    "$COLUMN_QUANTITY INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    fun insertProduct(model: CartModel) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PRODUCT_ID, model.product_id)
            put(COLUMN_IMG_URL, model.img_url)
            put(COLUMN_TITLE, model.title)
            put(COLUMN_PRICE, model.price)
            put(COLUMN_QUANTITY, model.quantity)
        }
        db.insert(TABLE_CART, null, values)
        db.close()
    }

    fun getProducts(): ArrayList<CartModel> {
        val list = ArrayList<CartModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CART", null)

        if (cursor.moveToFirst()) {
            do {
                val model = CartModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    product_id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                    img_url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMG_URL)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
                )
                list.add(model)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun deleteProduct(productId: Int) {
        val db = writableDatabase
        db.delete(TABLE_CART, "$COLUMN_PRODUCT_ID=?", arrayOf(productId.toString()))
        db.close()
    }

    fun updateQuantity(productId: Int, qty: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUANTITY, qty)
        }
        db.update(TABLE_CART, values, "$COLUMN_PRODUCT_ID=?", arrayOf(productId.toString()))
        db.close()
    }

    fun isProductInCart(productId: Int): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_CART WHERE $COLUMN_PRODUCT_ID = ?",
            arrayOf(productId.toString())
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun increaseQuantity(productId: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_CART SET $COLUMN_QUANTITY = $COLUMN_QUANTITY + 1 WHERE $COLUMN_PRODUCT_ID = ?", arrayOf(productId.toString()))
        db.close()
    }
}
