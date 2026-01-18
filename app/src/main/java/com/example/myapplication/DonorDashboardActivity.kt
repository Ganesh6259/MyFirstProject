package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DonorDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_dashboard)

        findViewById<Button>(R.id.btnDonateSmall).setOnClickListener {
            val intent = Intent(this, PostFoodActivity::class.java)
            intent.putExtra("USER_TYPE", UserType.INDIVIDUAL_DONOR)
            startActivity(intent)
        }

        setupRecyclerViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_profile) {
            startActivity(Intent(this, ViewProfileActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        val rvHistory = findViewById<RecyclerView>(R.id.rvDonorHistory)
        rvHistory.layoutManager = LinearLayoutManager(this)
        val myDonations = FoodRepository.donations.filter { it.donorType == UserType.INDIVIDUAL_DONOR }
        rvHistory.adapter = FoodAdapter(myDonations)

        val rvRequests = findViewById<RecyclerView>(R.id.rvOrphanageRequestsDonor)
        rvRequests.layoutManager = LinearLayoutManager(this)
        rvRequests.adapter = RequestAdapter(
            requests = FoodRepository.requests,
            showActions = true
        )
    }
}