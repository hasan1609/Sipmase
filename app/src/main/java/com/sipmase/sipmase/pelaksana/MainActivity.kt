package com.sipmase.sipmase.pelaksana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.sipmase.sipmase.LoginActivity
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.apar.AparActivity
import com.sipmase.sipmase.admin.apat.ApatActivity
import com.sipmase.sipmase.admin.hydrant.HydrantActivity
import com.sipmase.sipmase.admin.kebisingan.KebisinganActivity
import com.sipmase.sipmase.admin.pelaksana.PelaksanaActivity
import com.sipmase.sipmase.admin.pencahayaan.PencahayaanActivity
import com.sipmase.sipmase.databinding.ActivityMainBinding
import com.sipmase.sipmase.pelaksana.ambulance.ScheduleAmbulanceActivity
import com.sipmase.sipmase.pelaksana.apar.AparPelaksanaActivity
import com.sipmase.sipmase.pelaksana.apat.ScheduleApatActivity
import com.sipmase.sipmase.pelaksana.damkar.ScheduleDamkarActivity
import com.sipmase.sipmase.pelaksana.edgblok1.ScheduleEdgBlok1Activity
import com.sipmase.sipmase.pelaksana.edgblok2.ScheduleEdgBlok2Activity
import com.sipmase.sipmase.pelaksana.edgblok3.ScheduleEdgBlok3Activity
import com.sipmase.sipmase.pelaksana.ffblok1.ScheduleFfBlok1Activity
import com.sipmase.sipmase.pelaksana.ffblok2.ScheduleFfBlok2Activity
import com.sipmase.sipmase.pelaksana.hydrant.ScheduleHydrantActivity
import com.sipmase.sipmase.pelaksana.kebisingan.ScheduleKebisinganActivity
import com.sipmase.sipmase.pelaksana.pencahyaan.SchedulePencahayaanActivity
import com.sipmase.sipmase.pelaksana.seawater.ScheduleSeaWaterActivity
import com.sipmase.sipmase.session.SessionManager
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var sessionManager : SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)

        binding.username.text = sessionManager.getUsername().toString().toUpperCase()
        binding.nama.text = sessionManager.getNama().toString().toUpperCase()
        binding.txtapar.setOnClickListener {
            startActivity<AparPelaksanaActivity>()
        }
        binding.txtapat.setOnClickListener {
            startActivity<ScheduleApatActivity>()
        }
        binding.txthydrant.setOnClickListener {
            startActivity<ScheduleHydrantActivity>()
        }
        binding.txtkebisingan.setOnClickListener {
            startActivity<ScheduleKebisinganActivity>()
        }
        binding.txtpencahayaan.setOnClickListener {
            startActivity<SchedulePencahayaanActivity>()
        }
        binding.txtseawater.setOnClickListener {
            startActivity<ScheduleSeaWaterActivity>()
        }
        binding.txtffblok1.setOnClickListener {
            startActivity<ScheduleFfBlok1Activity>()
        }
        binding.txtfirefightingblok2.setOnClickListener {
            startActivity<ScheduleFfBlok2Activity>()
        }
        binding.txtedgblok1.setOnClickListener {
            startActivity<ScheduleEdgBlok1Activity>()
        }
        binding.txtedgblok2.setOnClickListener {
            startActivity<ScheduleEdgBlok2Activity>()
        }
        binding.txtedgblok3.setOnClickListener {
            startActivity<ScheduleEdgBlok3Activity>()
        }
        binding.txtmobildamkar.setOnClickListener {
            startActivity<ScheduleDamkarActivity>()
        }
        binding.txtmobilambulance.setOnClickListener {
            startActivity<ScheduleAmbulanceActivity>()
        }


        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Logout ? ")
            builder.setPositiveButton("Ok") { dialog, which ->
                sessionManager.setLoginadmin(false)
                sessionManager.setLogin(false)
                sessionManager.setToken("")
                sessionManager.setNama("")
                startActivity<LoginActivity>()
                toast("Berhasil Logout")
                finish()
            }

            builder.setNegativeButton("Cancel ?") { dialog, which ->
                dialog.dismiss()
            }

            builder.show()
        }

    }
}