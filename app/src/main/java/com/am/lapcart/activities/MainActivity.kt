package com.am.lapcart.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.am.lapcart.fragments.CartFragment
import com.am.lapcart.fragments.HomeFragment
import com.am.lapcart.fragments.ProfileFragment
import com.am.lapcart.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        replaceFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

            R.id.home -> {
            replaceFragment(HomeFragment())
            true
        }
            R.id.cart ->  {
            replaceFragment(CartFragment())
            true
        }
            R.id.profile -> {
            replaceFragment(ProfileFragment())
            true
        }
            else -> false

            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }
}