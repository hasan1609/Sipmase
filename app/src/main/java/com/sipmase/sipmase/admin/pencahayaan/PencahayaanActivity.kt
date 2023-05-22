package com.sipmase.sipmase.admin.pencahayaan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.pencahayaan.fragment.HasilPencahayaanFragment
import com.sipmase.sipmase.admin.pencahayaan.fragment.ItemPencahayaanFragment
import com.sipmase.sipmase.admin.pencahayaan.fragment.SchedulePencahayaanFragment
import com.sipmase.sipmase.session.SessionManager

class PencahayaanActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_item -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminpencahayaan,
                        ItemPencahayaanFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_schedule -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminpencahayaan,
                        SchedulePencahayaanFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_hasil -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminpencahayaan,
                        HasilPencahayaanFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pencahayaan)
//        sessionManager = SessionManager(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_viewpencahayaanadmin)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(ItemPencahayaanFragment())

    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frameadminpencahayaan, fragment)
        fragmentTrans.commit()
    }
}