package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        loadProfileData()

        findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
            val intent = Intent(this, ProfileSetupActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnCloseProfile).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfileData()
    }

    private fun loadProfileData() {
        val profile = FoodRepository.currentAccount?.profile
        if (profile == null) {
            finish()
            return
        }

        findViewById<TextView>(R.id.tvDisplayName).text = profile.name
        findViewById<TextView>(R.id.tvDisplayRole).text = "Role: ${profile.role}"
        findViewById<TextView>(R.id.tvDisplayContact).text = "Contact: ${profile.contact}"
        findViewById<TextView>(R.id.tvDisplayAddress).text = "Address: ${profile.address}"
        findViewById<TextView>(R.id.tvDisplayOrg).text = "Org Type: ${profile.organizationType ?: "N/A"}"
        findViewById<TextView>(R.id.tvDisplayNotes).text = "Notes: ${profile.additionalNotes ?: "N/A"}"

        val tvExtra = findViewById<TextView>(R.id.tvDisplayExtra)
        when (profile.role) {
            UserType.ORPHANAGE -> {
                tvExtra.visibility = View.VISIBLE
                tvExtra.text = "Members: ${profile.orphanageCapacity ?: "N/A"}"
            }
            UserType.RESTAURANT -> {
                tvExtra.visibility = View.VISIBLE
                tvExtra.text = "Food Type: ${profile.restaurantFoodType ?: "N/A"}"
            }
            else -> {
                tvExtra.visibility = View.GONE
            }
        }
    }
}