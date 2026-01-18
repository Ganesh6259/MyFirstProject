package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class PersonalDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_details)

        val etDob = findViewById<TextInputEditText>(R.id.etDob)
        val atvGender = findViewById<AutoCompleteTextView>(R.id.atvGender)
        val tvWhyInfo = findViewById<TextView>(R.id.tvWhyInfo)
        val btnNext = findViewById<Button>(R.id.btnContinueDetails)

        // Date Picker Setup
        etDob.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                etDob.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            }, year, month, day).show()
        }

        // Gender Dropdown Setup
        val genders = arrayOf("Male", "Female", "Other", "Prefer not to say")
        atvGender.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, genders))

        // Info Click Logic
        tvWhyInfo.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Why we ask for this info")
                .setMessage("Sharing your birthday is required. We uses you birthday to make sure you meet age requiredments for certain services and provide a more personalized experience.\n\nSharing your gender is optional. We only uses your gender to provide a more personalized experience.")
                .setPositiveButton("OK", null)
                .show()
        }

        btnNext.setOnClickListener {
            val dob = etDob.text.toString().trim()
            val gender = atvGender.text.toString().trim()

            if (dob.isNotEmpty()) {
                val account = FoodRepository.currentAccount
                account?.dob = dob
                account?.gender = gender

                // Final step of account creation -> Role Selection
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Date of birth is required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}