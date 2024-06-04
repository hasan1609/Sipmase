package com.sipmase.sipmase.pelaksana.eloto

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.eloto.DetailElotoAdapter
import com.sipmase.sipmase.databinding.ActivityTambahPenormalanBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.eloto.ElotoModel
import com.sipmase.sipmase.model.eloto.GroupedDataEloto
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class TambahPenormalanActivity : AppCompatActivity(), AnkoLogger {
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
        val currentDate = LocalDate.now()
        binding.edtTgl.text = currentDate.toString()
        binding.btnSimpan.setOnClickListener {
            simpanData()
        }
    }

    private fun simpanData() {
        var pic = binding.edtPic.text.toString()
        var posisi = binding.edtPosPenormalan.text.toString()

        if (pic.isNotEmpty() && posisi.isNotEmpty() && penormalan!!.idTag.toString() != null){
            loading(true)
            api.tambahpenormalan(penormalan!!.idTag.toString() ,pic, posisi).enqueue(object :
                Callback<PostDataResponse> {
                override fun onResponse(
                    call: Call<PostDataResponse>,
                    response: Response<PostDataResponse>
                ) {
                    try {
                        if (response.body()!!.sukses == 1) {
                            loading(false)
                            toast("Tambah Data berhasil")
                            finish()
                        } else {
                            loading(false)
                            toast("Tambah Data gagal")
                        }
                    } catch (e: Exception) {
                        progressDialog.dismiss()
                        info { "dinda ${e.message}${response.code()} " }
                        toast(e.message.toString())
                    }
                }
                override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                    loading(false)
                    toast("Kesalahan Jaringan")
                }
            })
        }else{
            toast("Harap Isi Semua Kolom")
        }
    }

    fun loading(status: Boolean) {
        if (status) {
            progressDialog.setTitle("Loading...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        } else {
            progressDialog.dismiss()

        }
    }
}