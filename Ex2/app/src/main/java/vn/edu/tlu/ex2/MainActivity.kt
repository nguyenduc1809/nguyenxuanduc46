package vn.edu.tlu.ex2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etId: EditText
    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnShow: Button
    private lateinit var tvResult: TextView

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        etId = findViewById(R.id.etId)
        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        btnShow = findViewById(R.id.btnShow)
        tvResult = findViewById(R.id.tvResult)


        dbHelper = DatabaseHelper(this)


        btnAdd.setOnClickListener {
            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = dbHelper.addContact(name, phone)
            if (id > 0) {
                Toast.makeText(this, "Đã thêm liên hệ", Toast.LENGTH_SHORT).show()
                clearInputs()
            }
        }


        btnUpdate.setOnClickListener {
            val idText = etId.text.toString().trim()
            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (idText.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập ID và đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = idText.toLongOrNull()
            if (id == null) {
                Toast.makeText(this, "ID không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rowsAffected = dbHelper.updateContact(id, name, phone)
            if (rowsAffected > 0) {
                Toast.makeText(this, "Đã cập nhật liên hệ", Toast.LENGTH_SHORT).show()
                clearInputs()
            } else {
                Toast.makeText(this, "Không tìm thấy liên hệ với ID: $id", Toast.LENGTH_SHORT).show()
            }
        }


        btnDelete.setOnClickListener {
            val idText = etId.text.toString().trim()

            if (idText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập ID để xóa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = idText.toLongOrNull()
            if (id == null) {
                Toast.makeText(this, "ID không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rowsAffected = dbHelper.deleteContact(id)
            if (rowsAffected > 0) {
                Toast.makeText(this, "Đã xóa liên hệ", Toast.LENGTH_SHORT).show()
                clearInputs()
            } else {
                Toast.makeText(this, "Không tìm thấy liên hệ với ID: $id", Toast.LENGTH_SHORT).show()
            }
        }


        btnShow.setOnClickListener {
            val contacts = dbHelper.getAllContacts()
            if (contacts.isEmpty()) {
                tvResult.text = "Không có liên hệ nào"
            } else {
                tvResult.text = contacts.joinToString("\n")
            }
        }
    }

    private fun clearInputs() {
        etId.setText("")
        etName.setText("")
        etPhone.setText("")
    }
}