package com.sipmase.sipmase.admin.kebisingan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.kebisingan.fragment.HasilKebisinganFragment
import com.sipmase.sipmase.admin.kebisingan.fragment.ItemKebisinganFragment
import com.sipmase.sipmase.admin.kebisingan.fragment.ScheduleKebisinganFragment
import com.sipmase.sipmase.session.SessionManager

class KebisinganActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_item -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminkebisingan,
                        ItemKebisinganFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_schedule -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminkebisingan,
                        ScheduleKebisinganFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_hasil -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminkebisingan,
                        HasilKebisinganFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kebisingan)
//        sessionManager = SessionManager(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_viewkebisinganadmin)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(ItemKebisinganFragment())

    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frameadminkebisingan, fragment)
        fragmentTrans.commit()
    }
}
