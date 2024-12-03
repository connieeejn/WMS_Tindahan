package com.example.wms_tindahan

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.wms_tindahan.fragment.InventoryFragment
import com.example.wms_tindahan.fragment.ReportFragment
import com.example.wms_tindahan.fragment.UserFragment
import com.google.android.material.navigation.NavigationView

class Inventory : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventory)

        drawerLayout = findViewById(R.id.drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val userName = intent.getStringExtra("USER_NAME") ?: "Default Name"
        val email = intent.getStringExtra("USER_EMAIL") ?: "Default Email"

        val headerView = navigationView.getHeaderView(0)
        val headerName = headerView.findViewById<TextView>(R.id.username)
        val headerEmail = headerView.findViewById<TextView>(R.id.email) // Replace 'header_email' with the ID of another view

        headerName.text = userName.capitalize()
        headerEmail.text = email

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InventoryFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_inventory)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_inventory -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InventoryFragment()).commit()
            R.id.nav_report -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ReportFragment()).commit()
            R.id.nav_user -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UserFragment()).commit()
            R.id.nav_logout -> {
                // Show logout toast
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()

                // Navigate to MainActivity (or wherever you want)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                // Close the current activity (if you want to exit after logout)
                finish()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}