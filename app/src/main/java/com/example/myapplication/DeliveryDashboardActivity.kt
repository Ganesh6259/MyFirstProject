package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DeliveryDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_dashboard)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rvTasks = findViewById<RecyclerView>(R.id.rvDeliveryTasks)
        rvTasks.layoutManager = LinearLayoutManager(this)

        // Combine accepted donations and accepted requests for delivery
        val acceptedDonations = FoodRepository.donations.filter { it.status == "Accepted" || it.status == "Picked up" }
        val acceptedRequests = FoodRepository.requests.filter { it.status == "Accepted" || it.status == "Picked up" }
        
        val allTasks = mutableListOf<Any>()
        allTasks.addAll(acceptedDonations)
        allTasks.addAll(acceptedRequests)

        rvTasks.adapter = DeliveryAdapter(allTasks) { task ->
            if (task is FoodDonation) {
                if (task.status == "Accepted") {
                    task.status = "Picked up"
                    Toast.makeText(this, "Food Picked up!", Toast.LENGTH_SHORT).show()
                } else if (task.status == "Picked up") {
                    task.status = "Delivered"
                    Toast.makeText(this, "Food Delivered!", Toast.LENGTH_SHORT).show()
                }
            } else if (task is FoodRequest) {
                if (task.status == "Accepted") {
                    task.status = "Picked up"
                    Toast.makeText(this, "Request Picked up!", Toast.LENGTH_SHORT).show()
                } else if (task.status == "Picked up") {
                    task.status = "Delivered"
                    Toast.makeText(this, "Request Delivered!", Toast.LENGTH_SHORT).show()
                }
            }
            setupRecyclerView() // Refresh list
        }
    }
}