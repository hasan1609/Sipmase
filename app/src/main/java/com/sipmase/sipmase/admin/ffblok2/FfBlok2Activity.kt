package com.sipmase.sipmase.admin.ffblok2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.ffblok1.fragment.HasilFfBlok1Fragment
import com.sipmase.sipmase.admin.ffblok1.fragment.ScheduleFfBlok1Fragment
import com.sipmase.sipmase.admin.ffblok2.fragment.HasilFfBlok2Fragment
import com.sipmase.sipmase.admin.ffblok2.fragment.ScheduleFfBlok2Fragment
import com.sipmase.sipmase.session.SessionManager

class FfBlok2Activity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_schedule -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminffblok2,
                        ScheduleFfBlok2Fragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_hasil -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminffblok2,
                        HasilFfBlok2Fragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ff_blok2)
//        sessionManager = SessionManager(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_viewffblok2admin)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(ScheduleFfBlok2Fragment())

    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frameadminffblok2, fragment)
        fragmentTrans.commit()
    }
}