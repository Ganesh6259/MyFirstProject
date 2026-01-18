package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

class ProfileSetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setup)

        val role = intent.getSerializableExtra("ROLE") as? UserType ?: FoodRepository.currentAccount?.profile?.role ?: UserType.RESTAURANT
        val isEditing = FoodRepository.currentAccount?.profile != null

        val tvTitle = findViewById<TextView>(R.id.tvProfileTitle)
        val etName = findViewById<EditText>(R.id.etProfileName)
        val etContact = findViewById<EditText>(R.id.etProfileContact)
        val etAddress = findViewById<EditText>(R.id.etProfileAddress)
        val etOrphanageCapacity = findViewById<EditText>(R.id.etOrphanageCapacity)
        val etRestaurantFoodType = findViewById<EditText>(R.id.etRestaurantFoodType)
        val etOrgType = findViewById<EditText>(R.id.etOrgType)
        val etNotes = findViewById<EditText>(R.id.etProfileNotes)
        val btnSave = findViewById<Button>(R.id.btnSaveProfile)

        if (isEditing) {
            tvTitle.text = "Edit Your Profile"
            btnSave.text = "Update Profile"
            val p = FoodRepository.currentAccount!!.profile!!
            etName.setText(p.name)
            etContact.setText(p.contact)
            etAddress.setText(p.address)
            etOrphanageCapacity.setText(p.orphanageCapacity)
            etRestaurantFoodType.setText(p.restaurantFoodType)
            etOrgType.setText(p.organizationType)
            etNotes.setText(p.additionalNotes)
        }

        // Show/Hide fields based on role
        when (role) {
            UserType.ORPHANAGE -> etOrphanageCapacity.visibility = View.VISIBLE
            UserType.RESTAURANT -> etRestaurantFoodType.visibility = View.VISIBLE
            else -> {}
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val contact = etContact.text.toString()
            val address = etAddress.text.toString()

            if (name.isNotEmpty() && contact.isNotEmpty() && address.isNotEmpty()) {
                val profile = UserProfile(
                    id = FoodRepository.currentAccount?.profile?.id ?: UUID.randomUUID().toString(),
                    name = name,
                    contact = contact,
                    address = address,
                    role = role,
                    orphanageCapacity = etOrphanageCapacity.text.toString(),
                    restaurantFoodType = etRestaurantFoodType.text.toString(),
                    organizationType = etOrgType.text.toString(),
                    additionalNotes = etNotes.text.toString()
                )
                FoodRepository.currentAccount?.profile = profile
                
                if (!isEditing) {
                    val intent = when (role) {
                        UserType.RESTAURANT -> Intent(this, RestaurantDashboardActivity::class.java)
                        UserType.INDIVIDUAL_DONOR -> Intent(this, DonorDashboardActivity::class.java)
                        UserType.ORPHANAGE -> Intent(this, OrphanageDashboardActivity::class.java)
                        UserType.NEEDED_INDIVIDUAL -> Intent(this, NeedyDashboardActivity::class.java)
                        UserType.DELIVERY_BOY -> Intent(this, DeliveryDashboardActivity::class.java)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}