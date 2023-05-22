package com.sipmase.sipmase.pelaksana.apar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.apar.fragment.HasilAparFragment
import com.sipmase.sipmase.admin.apar.fragment.ItemAparFragment
import com.sipmase.sipmase.admin.apar.fragment.ScheduleAparFragment
import com.sipmase.sipmase.pelaksana.apar.fragment.AparKadaluarsaFragment
import com.sipmase.sipmase.pelaksana.apar.fragment.ScheduleAparPelaksanaFragment
import com.sipmase.sipmase.session.SessionManager

class AparPelaksanaActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_apar -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameapar,
                        ScheduleAparPelaksanaFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_kadaluarsa -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameapar,
                        AparKadaluarsaFragment()
                    ).commit()
                    return@OnNavigationItemSelectedListener true

                }
            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apar_pelaksana)
//        sessionManager = SessionManager(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_viewapar)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(ScheduleAparPelaksanaFragment())

    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frameapar, fragment)
        fragmentTrans.commit()
    }
}