package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

class RequestFoodActivity : AppCompatActivity() {
    private val requestedItems = mutableListOf<FoodItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_food)

        val userType = intent.getSerializableExtra("USER_TYPE") as? UserType ?: UserType.ORPHANAGE

        val tvTitle = findViewById<TextView>(R.id.tvRequestTitle)
        val etRequesterName = findViewById<EditText>(R.id.etRequesterName)
        val etLocation = findViewById<EditText>(R.id.etLocation)
        val etItemName = findViewById<EditText>(R.id.etReqItemName)
        val etItemQty = findViewById<EditText>(R.id.etReqItemQty)
        val btnAddItem = findViewById<Button>(R.id.btnAddReqItem)
        val tvAddedItems = findViewById<TextView>(R.id.tvAddedReqItems)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitRequest)

        if (userType == UserType.NEEDED_INDIVIDUAL) {
            tvTitle.text = "Individual: Request Food"
            etRequesterName.hint = "Your Name"
        } else {
            tvTitle.text = "Orphanage: Request Food"
            etRequesterName.hint = "Orphanage Name"
        }

        btnAddItem.setOnClickListener {
            val name = etItemName.text.toString()
            val qty = etItemQty.text.toString()

            if (name.isNotEmpty() && qty.isNotEmpty()) {
                requestedItems.add(FoodItem(name, qty))
                etItemName.text.clear()
                etItemQty.text.clear()
                updateItemsDisplay(tvAddedItems)
            } else {
                Toast.makeText(this, "Enter item and quantity", Toast.LENGTH_SHORT).show()
            }
        }

        btnSubmit.setOnClickListener {
            val requester = etRequesterName.text.toString()
            val loc = etLocation.text.toString()

            if (requester.isNotEmpty() && loc.isNotEmpty() && requestedItems.isNotEmpty()) {
                val request = FoodRequest(
                    id = UUID.randomUUID().toString(),
                    items = requestedItems.toList(),
                    location = loc,
                    requesterId = FoodRepository.currentAccount?.profile?.id ?: "",
                    requesterName = requester,
                    requesterType = userType
                )
                FoodRepository.requests.add(request)
                Toast.makeText(this, "Request Submitted Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all details and add items", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateItemsDisplay(textView: TextView) {
        val display = requestedItems.joinToString("\n") { "${it.name} - ${it.quantity}" }
        textView.text = if (display.isEmpty()) "No items added yet" else display
    }
}