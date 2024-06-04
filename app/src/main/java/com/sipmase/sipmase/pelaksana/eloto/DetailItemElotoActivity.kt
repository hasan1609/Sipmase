package com.sipmase.sipmase.pelaksana.eloto

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityDetailItemElotoBinding
import com.sipmase.sipmase.model.eloto.ElotoModel
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger

class DetailItemElotoActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityDetailItemElotoBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    var eloto: ElotoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_item_eloto)
        binding.lifecycleOwner = this
        val gson = Gson()
        eloto =
            gson.fromJson(
                intent.getStringExtra("eloto"),
                ElotoModel::class.java
            )
        progressDialog = ProgressDialog(this)
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.txtTgl.text = eloto!!.createdAt.toString()
        binding.txtTag.text = eloto!!.idTag.toString()
        binding.txtWo.text = eloto!!.wo.toString()
        binding.txtPeralatan.text = eloto!!.peralatan.toString()
        binding.txtLokasi.text = eloto!!.lokasi.toString()
        binding.txtPosisi.text = eloto!!.posisiAwal.toString()
        binding.txtKet.text = eloto!!.ket.toString()

        if (eloto!!.isolasi == null){
            binding.crdIsolasi.visibility = View.GONE
        }else{
            binding.crdIsolasi.visibility = View.VISIBLE
            binding.txtTglIsolasi.text = eloto!!.isolasi!!.createdAt.toString()
            binding.txtPicIsolasi.text = eloto!!.isolasi!!.pic.toString()
            binding.txtPosisiIsolasi.text = eloto!!.isolasi!!.posisiIsolasi.toString()
            val url = "https://sipmase.com/public/storage/eviden/"
            val foto = eloto!!.isolasi!!.eviden.toString()
            Picasso.get()
                .load(url+foto)
                .into(binding.eviden)
        }

        if (eloto!!.penormalan == null){
            binding.crdPenormalan.visibility = View.GONE
        }else{
            binding.crdIsolasi.visibility = View.VISIBLE
            binding.txtTglPenormalan.text = eloto!!.penormalan!!.createdAt.toString()
            binding.txtPicPenormalan.text = eloto!!.penormalan!!.pic.toString()
            binding.txtPosisiPenormalan.text = eloto!!.penormalan!!.posisiNormal.toString()
        }


    }
}