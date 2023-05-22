package com.sipmase.sipmase.admin.apar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.apar.fragment.HasilAparFragment
import com.sipmase.sipmase.admin.apar.fragment.ItemAparFragment
import com.sipmase.sipmase.admin.apar.fragment.ScheduleAparFragment
import com.sipmase.sipmase.session.SessionManager

class AparActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_item -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminapar,
                        ItemAparFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_schedule -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminapar,
                        ScheduleAparFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_hasil -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameadminapar,
                        HasilAparFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apar)
//        sessionManager = SessionManager(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_viewaparadmin)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(ItemAparFragment())

    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frameadminapar, fragment)
        fragmentTrans.commit()
    }
}