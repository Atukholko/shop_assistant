package com.tukholko.assistant.app

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tukholko.assistant.R
import com.tukholko.assistant.app.fragments.Central
import com.tukholko.assistant.app.fragments.Left
import com.tukholko.assistant.app.fragments.Right

class AppActivity : AppCompatActivity() {
    private val leftFragment = Left()
    private val centralFragment = Central()
    private val rightFragment = Right()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = centralFragment

    lateinit var toolbar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        toolbar = supportActionBar!!
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
                toolbar.title = "LEFT GAY SON"
                fragmentManager.beginTransaction().hide(activeFragment).show(leftFragment).commit()
                activeFragment = leftFragment
            }
            R.id.navigation_central -> {
                toolbar.title = "VLADIMIRSKII CENTRAL "
                fragmentManager.beginTransaction().hide(activeFragment).show(centralFragment).commit()
                activeFragment = centralFragment
            }
            R.id.navigation_right -> {
                toolbar.title = "RIGHTHTHTH"
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

}