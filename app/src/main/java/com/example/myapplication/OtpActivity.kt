package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OtpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        val phone = intent.getStringExtra("PHONE") ?: ""
        val isSignup = intent.getBooleanExtra("IS_SIGNUP", false)
        
        findViewById<TextView>(R.id.tvPhoneDisplay).text = phone

        val et1 = findViewById<EditText>(R.id.etOtp1)
        val et2 = findViewById<EditText>(R.id.etOtp2)
        val et3 = findViewById<EditText>(R.id.etOtp3)
        val et4 = findViewById<EditText>(R.id.etOtp4)
        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val tvEditPhone = findViewById<TextView>(R.id.tvEditPhone)

        setupOtpAutoAdvance(et1, et2, et3, et4)

        tvEditPhone.setOnClickListener { finish() }

        btnVerify.setOnClickListener {
            val otp = et1.text.toString() + et2.text.toString() + et3.text.toString() + et4.text.toString()
            
            if (otp.length == 4) {
                Toast.makeText(this, "OTP Verification Successful!", Toast.LENGTH_SHORT).show()
                
                var account = FoodRepository.accounts.find { it.phoneNumber == phone }
                if (account == null) {
                    account = UserAccount(phoneNumber = phone)
                    FoodRepository.accounts.add(account)
                }
                
                FoodRepository.currentAccount = account
                
                if (isSignup || account.profile == null) {
                    // New sign up flow - start with Name Setup
                    startActivity(Intent(this, NameSetupActivity::class.java))
                } else {
                    // Existing user - go to dashboard
                    val intent = when (account.profile!!.role) {
                        UserType.RESTAURANT -> Intent(this, RestaurantDashboardActivity::class.java)
                        UserType.INDIVIDUAL_DONOR -> Intent(this, DonorDashboardActivity::class.java)
                        UserType.ORPHANAGE -> Intent(this, OrphanageDashboardActivity::class.java)
                        UserType.NEEDED_INDIVIDUAL -> Intent(this, NeedyDashboardActivity::class.java)
                        UserType.DELIVERY_BOY -> Intent(this, DeliveryDashboardActivity::class.java)
                    }
                    startActivity(intent)
                }
                finishAffinity()
            } else {
                Toast.makeText(this, "Please enter 4-digit OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupOtpAutoAdvance(et1: EditText, et2: EditText, et3: EditText, et4: EditText) {
        et1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { if (s?.length == 1) et2.requestFocus() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        et2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { 
                if (s?.length == 1) et3.requestFocus()
                else if (s?.length == 0) et1.requestFocus()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        et3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { 
                if (s?.length == 1) et4.requestFocus()
                else if (s?.length == 0) et2.requestFocus()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        et4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { 
                if (s?.length == 0) et3.requestFocus()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}