package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

class PostFoodActivity : AppCompatActivity() {
    private val addedItems = mutableListOf<FoodItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_food)

        val userType = intent.getSerializableExtra("USER_TYPE") as? UserType ?: UserType.RESTAURANT
        
        val tvTitle = findViewById<TextView>(R.id.tvPostTitle)
        val etDonorName = findViewById<EditText>(R.id.etDonorName)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etItemName = findViewById<EditText>(R.id.etItemName)
        val etItemQty = findViewById<EditText>(R.id.etItemQty)
        val btnAddItem = findViewById<Button>(R.id.btnAddItem)
        val tvAddedItems = findViewById<TextView>(R.id.tvAddedItems)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitPost)

        if (userType == UserType.INDIVIDUAL_DONOR) {
            tvTitle.text = "Donor: Post Food"
            etDonorName.hint = "Your Name"
        } else {
            tvTitle.text = "Restaurant: Post Food"
            etDonorName.hint = "Restaurant Name"
        }

        btnAddItem.setOnClickListener {
            val name = etItemName.text.toString()
            val qty = etItemQty.text.toString()

            if (name.isNotEmpty() && qty.isNotEmpty()) {
                addedItems.add(FoodItem(name, qty))
                etItemName.text.clear()
                etItemQty.text.clear()
                updateItemsDisplay(tvAddedItems)
            } else {
                Toast.makeText(this, "Enter item and quantity", Toast.LENGTH_SHORT).show()
            }
        }

        btnSubmit.setOnClickListener {
            val donor = etDonorName.text.toString()
            val addr = etAddress.text.toString()

            if (donor.isNotEmpty() && addr.isNotEmpty() && addedItems.isNotEmpty()) {
                val donation = FoodDonation(
                    id = UUID.randomUUID().toString(),
                    items = addedItems.toList(),
                    address = addr,
                    donorId = FoodRepository.currentAccount?.profile?.id ?: "",
                    donorName = donor,
                    donorType = userType
                )
                FoodRepository.donations.add(donation)
                Toast.makeText(this, "Food Posted Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all details and add items", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateItemsDisplay(textView: TextView) {
        val display = addedItems.joinToString("\n") { "${it.name} - ${it.quantity}" }
        textView.text = if (display.isEmpty()) "No items added yet" else display
    }
}