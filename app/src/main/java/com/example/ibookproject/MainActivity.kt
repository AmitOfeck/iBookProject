package com.example.ibookproject

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ibookproject.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.findNavController
import android.view.Menu
import android.view.MenuItem
import android.util.Log


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavView: BottomNavigationView = binding.bottomNavigationView
        bottomNavView.setupWithNavController(navController)

        setupActionBarWithNavController(navController)

        checkIfUserLoggedIn()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registrationFragment, R.id.profileCreationFragment -> bottomNavView.visibility = View.GONE
                else -> bottomNavView.visibility = View.VISIBLE
            }
        }

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    if (navController.currentDestination?.id != R.id.userProfileFragment) {
                        navController.navigate(R.id.userProfileFragment)
                    }
                    true
                }
                R.id.nav_create_book -> {
                    if (navController.currentDestination?.id != R.id.addBookFragment) {
                        navController.navigate(R.id.addBookFragment)
                    }
                    true
                }
                R.id.nav_search -> {
                    if (navController.currentDestination?.id != R.id.searchBookFragment) {
                        navController.navigate(R.id.searchBookFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun checkIfUserLoggedIn() {
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("user_id", null)

        if (userId != null) {
            // If there is a user_id, user is logged in. Navigate to dashboard
            navController.navigate(R.id.dashboardFragment)
        } else {
            // If there is no user_id, user is not logged in. Navigate to login screen
            navController.navigate(R.id.loginFragment)
        }
    }

    // Logout the user and navigate back to login screen
    private fun logout() {
        // Clear the user_id from SharedPreferences
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("user_id")
            apply()
        }

        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut()

        invalidateOptionsMenu()

        // Navigate back to the login screen
        navController.navigate(R.id.loginFragment)
    }

    // Add the logout button to the ActionBar menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_menu_item -> {
                logout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu) // Inflate the menu
        // קריאה לפונקציה שתעדכן את כפתור ההתנתקות
        updateLogoutMenu(menu)
        return true
    }

    // בדיקה אם המשתמש מחובר ומעדכנים את התפריט
    private fun updateLogoutMenu(menu: Menu?) {
        // בדוק אם יש user_id ב-SharedPreferences
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("user_id", null)

        // הצג את כפתור ה-logout רק אם יש user_id
        menu?.findItem(R.id.logout_menu_item)?.isVisible = userId != null
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu() // עדכון התפריט בכל טעינה מחדש של האפליקציה
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

