package com.tukholko.assistant.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.tukholko.assistant.R
import com.tukholko.assistant.app.fragments.Left
import com.tukholko.assistant.app.fragments.Profile
import com.tukholko.assistant.app.fragments.Right
import com.tukholko.assistant.auth.LoginActivity

class AppActivity : AppCompatActivity() {
    private val leftFragment = Left()
    private val centralFragment = Profile()
    private val rightFragment = Right()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = centralFragment
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        fragmentManager.beginTransaction().apply {
            add(R.id.container, leftFragment, "LEFT FRAGMENT").hide(leftFragment)
            add(R.id.container, rightFragment, "RIGHT FRAGMENT").hide(rightFragment)
            add(R.id.container, centralFragment, "CENTRAL NAHUI")
        }.commit()

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_left -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(leftFragment).commit()
                activeFragment = leftFragment
            }
            R.id.navigation_central -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(centralFragment).commit()
                activeFragment = centralFragment
            }
            R.id.navigation_right -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(rightFragment).commit()
                activeFragment = rightFragment
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun signOut() {
        auth.signOut()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
