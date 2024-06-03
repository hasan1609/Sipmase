package com.sipmase.sipmase.pelaksana.eloto

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityTambahIsolasiBinding
import com.sipmase.sipmase.model.eloto.ElotoModel
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger

class TambahIsolasiActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var binding: ActivityTambahIsolasiBinding
    var isolasi: ElotoModel? = null
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tambah_isolasi)
        binding.lifecycleOwner = this
        val gson = Gson()
        isolasi =
            gson.fromJson(
                intent.getStringExtra("isolasi"),
                ElotoModel::class.java
            )

        progressDialog = ProgressDialog(this)


    }
}