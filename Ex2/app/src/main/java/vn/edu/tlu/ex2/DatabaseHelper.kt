package vn.edu.tlu.ex2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ContactDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "contacts"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_NAME TEXT,
                $KEY_PHONE TEXT
            )
        """
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Thêm liên hệ
    fun addContact(name: String, phone: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, name)
            put(KEY_PHONE, phone)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    // Sửa liên hệ (dựa trên ID)
    fun updateContact(id: Long, name: String, phone: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, name)
            put(KEY_PHONE, phone)
        }
        val rowsAffected = db.update(TABLE_NAME, values, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsAffected
    }

    // Xóa liên hệ (dựa trên ID)
    fun deleteContact(id: Long): Int {
        val db = this.writableDatabase
        val rowsAffected = db.delete(TABLE_NAME, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsAffected
    }

    // Lấy tất cả liên hệ
    fun getAllContacts(): List<String> {
        val contactList = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE))
                contactList.add("ID: $id, Tên: $name, SĐT: $phone")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return contactList
    }
}