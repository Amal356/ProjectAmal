package com.example.myamal.tp5

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_IMAGE TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_DESCRIPTION, product.description)
            put(COLUMN_IMAGE, product.image)
        }
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun deleteDB(productId: String): Boolean {
        val db = this.writableDatabase
        val selection = "${COLUMN_ID} = ?"
        val selectionArgs = arrayOf(productId)
        val deletedRows = db.delete(TABLE_NAME, selection, selectionArgs)

        println("Deleted rows: $deletedRows")

        return deletedRows > 0

    }

    fun updateDB(newValue: String, productId: String) : Boolean {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, newValue)
        }

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(productId)
        val count = db.update(
            TABLE_NAME,
            values,
            selection,
            selectionArgs
        )

        return count > 0

    }

    @SuppressLint("Range")
    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                val image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                productList.add(Product(id, name, description, image))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productList
    }

    companion object {
        private const val DATABASE_NAME = "productDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE = "image"
    }
}
