package com.sipmase.sipmase.admin.edgblok3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.edgblok3.fragment.HasilEdgBlok3Fragment
import com.sipmase.sipmase.admin.edgblok3.fragment.ScheduleEdgBlok3Fragment
import com.sipmase.sipmase.session.SessionManager

class EdgBlok3Activity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_schedule -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminedgblok3,
                        ScheduleEdgBlok3Fragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_hasil -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminedgblok3,
                        HasilEdgBlok3Fragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edg_blok3)
//        sessionManager = SessionManager(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_viewedgblok3admin)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(ScheduleEdgBlok3Fragment())

    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frameadminedgblok3, fragment)
        fragmentTrans.commit()
    }

}