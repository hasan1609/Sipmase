package com.sipmase.sipmase.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.sipmase.sipmase.LoginActivity
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.ambulance.AmbulanceActivity
import com.sipmase.sipmase.admin.apar.AparActivity
import com.sipmase.sipmase.admin.apat.ApatActivity
import com.sipmase.sipmase.admin.damkar.DamkarActivity
import com.sipmase.sipmase.admin.edgblok1.EdgBlok1Activity
import com.sipmase.sipmase.admin.edgblok2.EdgBlok2Activity
import com.sipmase.sipmase.admin.edgblok3.EdgBlok3Activity
import com.sipmase.sipmase.admin.ffblok1.FfBlok1Activity
import com.sipmase.sipmase.admin.ffblok2.FfBlok2Activity
import com.sipmase.sipmase.admin.hydrant.HydrantActivity
import com.sipmase.sipmase.admin.kebisingan.KebisinganActivity
import com.sipmase.sipmase.admin.pelaksana.PelaksanaActivity
import com.sipmase.sipmase.admin.pencahayaan.PencahayaanActivity
import com.sipmase.sipmase.admin.seawater.SeawaterActivity
import com.sipmase.sipmase.databinding.*
import com.sipmase.sipmase.session.SessionManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(), AnkoLogger {

    lateinit var binding: ActivityHomeBinding
    lateinit var sessionManager : SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)

        binding.username.text = sessionManager.getUsername().toString().toUpperCase()
        binding.nama.text = sessionManager.getNama().toString().toUpperCase()
        binding.txtapar.setOnClickListener {
            startActivity<AparActivity>()
        }
        binding.txtapat.setOnClickListener {
            startActivity<ApatActivity>()
        }
        binding.txthydrant.setOnClickListener {
            startActivity<HydrantActivity>()
        }
        binding.txtkebisingan.setOnClickListener {
            startActivity<KebisinganActivity>()
        }
        binding.txtpencahayaan.setOnClickListener {
            startActivity<PencahayaanActivity>()
        }
        binding.txtseawater.setOnClickListener {
            startActivity<SeawaterActivity>()
        }
        binding.txtffblok1.setOnClickListener {
            startActivity<FfBlok1Activity>()
        }
        binding.txtfirefightingblok2.setOnClickListener {
            startActivity<FfBlok2Activity>()
        }
        binding.txtedgblok1.setOnClickListener {
            startActivity<EdgBlok1Activity>()
        }
        binding.txtedgblok2.setOnClickListener {
            startActivity<EdgBlok2Activity>()
        }
        binding.txtedgblok3.setOnClickListener {
            startActivity<EdgBlok3Activity>()
        }
        binding.txtmobildamkar.setOnClickListener {
            startActivity<DamkarActivity>()
        }
        binding.txtmobilambulance.setOnClickListener {
            startActivity<AmbulanceActivity>()
        }
        binding.txtpelaksana.setOnClickListener {
            startActivity<PelaksanaActivity>()
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