package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OrphanageDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orphanage_dashboard)

        findViewById<Button>(R.id.btnRequestFood).setOnClickListener {
            val intent = Intent(this, RequestFoodActivity::class.java)
            intent.putExtra("USER_TYPE", UserType.ORPHANAGE)
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
        val rvAvailable = findViewById<RecyclerView>(R.id.rvAvailableFood)
        rvAvailable.layoutManager = LinearLayoutManager(this)
        rvAvailable.adapter = FoodAdapter(
            donations = FoodRepository.donations,
            showActions = true
        )

        val rvMyRequests = findViewById<RecyclerView>(R.id.rvMyRequests)
        rvMyRequests.layoutManager = LinearLayoutManager(this)
        val myRequests = FoodRepository.requests.filter { it.requesterType == UserType.ORPHANAGE }
        rvMyRequests.adapter = RequestAdapter(myRequests)
    }
}