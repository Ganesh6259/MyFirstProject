package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class NameSetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_setup)

        val etFirstName = findViewById<TextInputEditText>(R.id.etFirstName)
        val etLastName = findViewById<TextInputEditText>(R.id.etLastName)
        val btnNext = findViewById<Button>(R.id.btnNextName)

        btnNext.setOnClickListener {
            val fName = etFirstName.text.toString().trim()
            val lName = etLastName.text.toString().trim()

            if (fName.isNotEmpty()) {
                val account = FoodRepository.currentAccount
                account?.firstName = fName
                account?.lastName = lName

                startActivity(Intent(this, PersonalDetailsActivity::class.java))
            } else {
                Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}