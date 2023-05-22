package com.sipmase.sipmase.admin.damkar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.damkar.fragment.HasilDamkarFragment
import com.sipmase.sipmase.admin.damkar.fragment.ScheduleDamkarFragment
import com.sipmase.sipmase.session.SessionManager

class DamkarActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_schedule -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadmindamkar,
                        ScheduleDamkarFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_hasil -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadmindamkar,
                        HasilDamkarFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_damkar)
//        sessionManager = SessionManager(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_viewdamkaradmin)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(ScheduleDamkarFragment())

    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frameadmindamkar, fragment)
        fragmentTrans.commit()
    }
}