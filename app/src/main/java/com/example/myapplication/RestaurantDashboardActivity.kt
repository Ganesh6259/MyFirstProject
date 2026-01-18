package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.os.LocaleListCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RestaurantDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_dashboard)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.dashboard_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                handleMenuAction(item)
                true
            }
            popup.show()
        }

        findViewById<Button>(R.id.btnDonateFood).setOnClickListener {
            val intent = Intent(this, PostFoodActivity::class.java)
            intent.putExtra("USER_TYPE", UserType.RESTAURANT)
            startActivity(intent)
        }

        setupRecyclerViews()
    }

    private fun handleMenuAction(item: MenuItem) {
        when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, ViewProfileActivity::class.java))
            }
            R.id.action_change_language -> {
                showLanguageDialog()
            }
            R.id.action_logout -> {
                FoodRepository.currentAccount = null
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "Hindi", "Tamil", "Spanish", "French")
        val langCodes = arrayOf("en", "hi", "ta", "es", "fr")
        
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Language")
        builder.setItems(languages) { _, which ->
            setLocale(langCodes[which])
        }
        builder.show()
    }

    private fun setLocale(langCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
        Toast.makeText(this, "Language updated", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        val rvDonations = findViewById<RecyclerView>(R.id.rvRestaurantDonations)
        rvDonations.layoutManager = LinearLayoutManager(this)
        val myDonations = FoodRepository.donations.filter { it.donorType == UserType.RESTAURANT }
        rvDonations.adapter = FoodAdapter(myDonations)

        val rvRequests = findViewById<RecyclerView>(R.id.rvOrphanageRequests)
        rvRequests.layoutManager = LinearLayoutManager(this)
        rvRequests.adapter = RequestAdapter(
            requests = FoodRepository.requests,
            showActions = true
        )
    }
}