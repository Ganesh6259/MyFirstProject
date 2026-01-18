package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NeedyDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_needy_dashboard)

        findViewById<Button>(R.id.btnRequestFoodNeedy).setOnClickListener {
            val intent = Intent(this, RequestFoodActivity::class.java)
            intent.putExtra("USER_TYPE", UserType.NEEDED_INDIVIDUAL)
            startActivity(intent)
        }

        setupRecyclerViews()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        // Available Food from all donors
        val rvAvailable = findViewById<RecyclerView>(R.id.rvNeedyFoodList)
        rvAvailable.layoutManager = LinearLayoutManager(this)
        rvAvailable.adapter = FoodAdapter(
            donations = FoodRepository.donations,
            showActions = true
        )

        // My Requests (Filtered to Needy Individual)
        val rvMyRequests = findViewById<RecyclerView>(R.id.rvMyRequestsNeedy)
        rvMyRequests.layoutManager = LinearLayoutManager(this)
        val myRequests = FoodRepository.requests.filter { it.requesterType == UserType.NEEDED_INDIVIDUAL }
        rvMyRequests.adapter = RequestAdapter(myRequests)
    }
}