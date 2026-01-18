package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.LocaleListCompat
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignupActivity : AppCompatActivity() {
    private lateinit var etEmail: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etPass: TextInputEditText
    private lateinit var tvStrength: TextView
    private lateinit var tvReqUpper: TextView
    private lateinit var tvReqLower: TextView
    private lateinit var tvReqDigit: TextView
    private lateinit var tvReqSpecial: TextView
    private lateinit var tvReqLength: TextView
    
    private lateinit var llEmail: LinearLayout
    private lateinit var llPhone: LinearLayout
    private lateinit var etPhone: TextInputEditText
    private lateinit var tilPhone: TextInputLayout
    private lateinit var atvCountryCode: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val ibLanguage = findViewById<ImageButton>(R.id.ibLanguageSignup)
        ibLanguage.setOnClickListener { view ->
            showLanguageMenu(view)
        }

        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupSignup)
        llEmail = findViewById(R.id.llEmailSignup)
        llPhone = findViewById(R.id.llPhoneSignup)
        
        etEmail = findViewById(R.id.etSignupEmail)
        tilEmail = findViewById(R.id.tilSignupEmail)
        etPass = findViewById(R.id.etSignupPassword)
        tvStrength = findViewById(R.id.tvStrengthValue)
        tvReqUpper = findViewById(R.id.tvReqUpper)
        tvReqLower = findViewById(R.id.tvReqLower)
        tvReqDigit = findViewById(R.id.tvReqDigit)
        tvReqSpecial = findViewById(R.id.tvReqSpecial)
        tvReqLength = findViewById(R.id.tvReqLength)
        
        etPhone = findViewById(R.id.etSignupPhone)
        tilPhone = findViewById(R.id.tilSignupPhone)
        atvCountryCode = findViewById(R.id.atvSignupCountryCode)

        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val tvLogin = findViewById<TextView>(R.id.tvGoToLogin)

        // Setup Country Codes
        val countries = arrayOf("🇮🇳 +91", "🇺🇸 +1", "🇬🇧 +44", "🇦🇺 +61", "🇨🇦 +1")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countries)
        atvCountryCode.setAdapter(adapter)

        toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                if (checkedId == R.id.btnEmailSignupTab) {
                    llEmail.visibility = View.VISIBLE
                    llPhone.visibility = View.GONE
                } else {
                    llEmail.visibility = View.GONE
                    llPhone.visibility = View.VISIBLE
                }
            }
        }

        etPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnSignup.setOnClickListener {
            if (llEmail.visibility == View.VISIBLE) {
                handleEmailSignup()
            } else {
                handlePhoneSignup()
            }
        }

        tvLogin.setOnClickListener { finish() }
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

    private fun handleEmailSignup() {
        val email = etEmail.text.toString().trim()
        val pass = etPass.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Invalid email format"
            return
        } else tilEmail.error = null

        if (isAllSatisfied(pass)) {
            val existing = FoodRepository.accounts.find { it.email == email }
            if (existing != null) {
                Toast.makeText(this, "Account already exists", Toast.LENGTH_SHORT).show()
            } else {
                val newAccount = UserAccount(email = email, password = pass)
                FoodRepository.accounts.add(newAccount)
                FoodRepository.currentAccount = newAccount
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        } else {
            Toast.makeText(this, "Please satisfy all password requirements", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handlePhoneSignup() {
        val phone = etPhone.text.toString().trim()
        if (phone.length == 10) {
            val fullPhone = atvCountryCode.text.toString() + " " + phone
            
            // Check if phone number already exists
            val existing = FoodRepository.accounts.find { it.phoneNumber == fullPhone }
            if (existing != null) {
                tilPhone.error = "Account already exists with this phone number"
                return
            }
            
            val intent = Intent(this, OtpActivity::class.java)
            intent.putExtra("PHONE", fullPhone)
            intent.putExtra("IS_SIGNUP", true)
            startActivity(intent)
        } else {
            tilPhone.error = "Enter a valid 10-digit number"
        }
    }

    private fun validatePassword(password: String) {
        val hasUpper = password.any { it.isUpperCase() }
        val hasLower = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        val hasLength = password.length >= 8

        updateRequirement(tvReqUpper, hasUpper, "At least one uppercase letter")
        updateRequirement(tvReqLower, hasLower, "At least one lowercase letter")
        updateRequirement(tvReqDigit, hasDigit, "At least one digit")
        updateRequirement(tvReqSpecial, hasSpecial, "At least one special character")
        updateRequirement(tvReqLength, hasLength, "At least 8 characters")

        var score = 0
        if (hasUpper) score++
        if (hasLower) score++
        if (hasDigit) score++
        if (hasSpecial) score++
        if (hasLength) score++

        when {
            score == 0 -> { tvStrength.text = "None"; tvStrength.setTextColor(Color.GRAY) }
            score < 3 -> { tvStrength.text = "Weak"; tvStrength.setTextColor(Color.RED) }
            score < 5 -> { tvStrength.text = "Medium"; tvStrength.setTextColor(Color.parseColor("#FFA500")) }
            else -> { tvStrength.text = "Strong"; tvStrength.setTextColor(Color.parseColor("#388E3C")) }
        }
    }

    private fun updateRequirement(textView: TextView, satisfied: Boolean, text: String) {
        if (satisfied) {
            textView.text = "✔ $text"
            textView.setTextColor(Color.parseColor("#388E3C"))
        } else {
            textView.text = "✘ $text"
            textView.setTextColor(Color.RED)
        }
    }

    private fun isAllSatisfied(password: String): Boolean {
        return password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() } &&
                password.any { it.isDigit() } &&
                password.any { !it.isLetterOrDigit() } &&
                password.length >= 8
    }
}