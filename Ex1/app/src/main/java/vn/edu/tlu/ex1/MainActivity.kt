package vn.edu.tlu.ex1


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class   MainActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button
    private lateinit var btnClear: Button
    private lateinit var btnShow: Button
    private lateinit var tvResult: TextView

    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo views
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnSave = findViewById(R.id.btnSave)
        btnClear = findViewById(R.id.btnClear)
        btnShow = findViewById(R.id.btnShow)
        tvResult = findViewById(R.id.tvResult)

        // Khởi tạo PreferenceHelper
        preferenceHelper = PreferenceHelper(this)

        // Xử lý sự kiện khi nhấn nút Lưu
        btnSave.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            preferenceHelper.saveUserCredentials(username, password)
            Toast.makeText(this, "Đã lưu thông tin thành công", Toast.LENGTH_SHORT).show()

            // Xóa trường nhập sau khi lưu
            etUsername.setText("")
            etPassword.setText("")
        }

        // Xử lý sự kiện khi nhấn nút Xóa
        btnClear.setOnClickListener {
            preferenceHelper.clearUserCredentials()
            tvResult.text = ""
            Toast.makeText(this, "Đã xóa thông tin", Toast.LENGTH_SHORT).show()
        }

        // Xử lý sự kiện khi nhấn nút Hiển thị
        btnShow.setOnClickListener {
            val (username, password) = preferenceHelper.getUserCredentials()

            if (username != null && password != null) {
                tvResult.text = "Tên người dùng: $username\nMật khẩu: $password"
            } else {
                tvResult.text = "Không có thông tin người dùng"
            }
        }
    }
}