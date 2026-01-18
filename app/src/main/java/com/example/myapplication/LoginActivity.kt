package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.LocaleListCompat
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val ibLanguage = findViewById<ImageButton>(R.id.ibLanguage)
        ibLanguage.setOnClickListener { view ->
            showLanguageMenu(view)
        }

        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroup)
        val llEmail = findViewById<LinearLayout>(R.id.llEmailLogin)
        val llPhone = findViewById<LinearLayout>(R.id.llPhoneLogin)
        
        val tilEmail = findViewById<TextInputLayout>(R.id.tilLoginEmail)
        val etEmail = findViewById<TextInputEditText>(R.id.etLoginEmail)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilLoginPassword)
        val etPassword = findViewById<TextInputEditText>(R.id.etLoginPassword)
        
        val tilPhone = findViewById<TextInputLayout>(R.id.tilLoginPhone)
        val etPhone = findViewById<TextInputEditText>(R.id.etLoginPhone)
        val atvCountryCode = findViewById<AutoCompleteTextView>(R.id.atvCountryCode)
        
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvSignup = findViewById<View>(R.id.tvGoToSignup)

        // Setup Country Codes
        val countries = arrayOf(
            "🇮🇳 +91", "🇺🇸 +1", "🇬🇧 +44", "🇦🇺 +61", "🇨🇦 +1", 
            "🇩🇪 +49", "🇫🇷 +33", "🇯🇵 +81", "🇨🇳 +86", "🇧🇷 +55"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countries)
        atvCountryCode.setAdapter(adapter)

        toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                if (checkedId == R.id.btnEmailTab) {
                    llEmail.visibility = View.VISIBLE
                    llPhone.visibility = View.GONE
                    tilPhone.error = null
                } else {
                    llEmail.visibility = View.GONE
                    llPhone.visibility = View.VISIBLE
                    tilEmail.error = null
                    tilPassword.error = null
                }
            }
        }

        btnLogin.setOnClickListener {
            if (llEmail.visibility == View.VISIBLE) {
                val email = etEmail.text.toString().trim()
                val pass = etPassword.text.toString().trim()
                
                var isValid = true
                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tilEmail.error = "Enter a valid email address"
                    isValid = false
                } else {
                    tilEmail.error = null
                }
                
                if (pass.isEmpty()) {
                    tilPassword.error = "Enter password"
                    isValid = false
                } else {
                    tilPassword.error = null
                }

                if (isValid) {
                    val account = FoodRepository.accounts.find { it.email == email && it.password == pass }
                    if (account != null) {
                        proceedToDashboard(account)
                    } else {
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                val phoneNum = etPhone.text.toString().trim()
                
                if (phoneNum.isEmpty()) {
                    tilPhone.error = "Phone number cannot be empty"
                } else if (!phoneNum.all { it.isDigit() }) {
                    tilPhone.error = "Only digits are allowed"
                } else if (phoneNum.length != 10) {
                    tilPhone.error = "Enter a valid 10-digit phone number"
                } else {
                    val fullPhone = atvCountryCode.text.toString() + " " + phoneNum
                    val existing = FoodRepository.accounts.find { it.phoneNumber == fullPhone }
                    if (existing != null) {
                        tilPhone.error = null
                        val intent = Intent(this, OtpActivity::class.java)
                        intent.putExtra("PHONE", fullPhone)
                        intent.putExtra("IS_SIGNUP", false)
                        startActivity(intent)
                    } else {
                        tilPhone.error = "No account found with this number. Please Sign up."
                    }
                }
            }
        }

        tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun showLanguageMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menu.add(0, 1, 0, "Tamil (Default)")
        popup.menu.add(0, 2, 1, "Hindi")
        popup.menu.add(0, 3, 2, "Spanish")
        popup.menu.add(0, 4, 3, "English")
        
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> setLocale("ta")
                2 -> setLocale("hi")
                3 -> setLocale("es")
                4 -> setLocale("en")
            }
            true
        }
        popup.show()
    }

    private fun setLocale(langCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    private fun proceedToDashboard(account: UserAccount) {
        FoodRepository.currentAccount = account
        if (account.profile == null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            val intent = when (account.profile!!.role) {
                UserType.RESTAURANT -> Intent(this, RestaurantDashboardActivity::class.java)
                UserType.INDIVIDUAL_DONOR -> Intent(this, DonorDashboardActivity::class.java)
                UserType.ORPHANAGE -> Intent(this, OrphanageDashboardActivity::class.java)
                UserType.NEEDED_INDIVIDUAL -> Intent(this, NeedyDashboardActivity::class.java)
                UserType.DELIVERY_BOY -> Intent(this, DeliveryDashboardActivity::class.java)
            }
            startActivity(intent)
        }
        finish()
    }
}