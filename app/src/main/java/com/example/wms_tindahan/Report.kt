package com.example.wms_tindahan

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
//import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.drawerlayout.widget.DrawerLayout
import com.example.wms_tindahan.fragment.InventoryFragment
import com.example.wms_tindahan.fragment.ReportFragment
import com.example.wms_tindahan.fragment.UserFragment
import com.google.android.material.navigation.NavigationView

class Report : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report)

        drawerLayout = findViewById(R.id.drawer_layout)

        // TODO: Make the nav title dynamic (can add filter/add)
        // TODO: Different across fragments
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ReportFragment()).commit()
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