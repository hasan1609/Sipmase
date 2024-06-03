package com.sipmase.sipmase.pelaksana.eloto

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.eloto.DetailElotoAdapter
import com.sipmase.sipmase.databinding.ActivityTambahPenormalanBinding
import com.sipmase.sipmase.model.eloto.ElotoModel
import com.sipmase.sipmase.model.eloto.GroupedDataEloto
import com.sipmase.sipmase.webservice.ApiClient

class TambahPenormalanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahPenormalanBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    var penormalan: ElotoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tambah_penormalan)
        binding.lifecycleOwner = this
        val gson = Gson()
        penormalan =
            gson.fromJson(
                intent.getStringExtra("penormalan"),
                ElotoModel::class.java
            )

        progressDialog = ProgressDialog(this)


    }
}