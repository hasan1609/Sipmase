package com.sipmase.sipmase.pelaksana.eloto

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityTambahLotoBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.eloto.LastTagResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class TambahLotoActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityTambahLotoBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    var tagId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tambah_loto)
        binding.lifecycleOwner = this

        progressDialog = ProgressDialog(this)
        val currentDate = LocalDate.now()
        binding.edtTgl.text = currentDate.toString()
        binding.btnSimpan.setOnClickListener {
            simpanData()
        }
    }

    private fun simpanData() {
        var wo = binding.edtWoSr.text.toString()
        var peralatan = binding.edtPeralatan.text.toString()
        var lokasi = binding.edtLokasi.text.toString()
        var posisi = binding.edtPosAwal.text.toString()
        var ket = binding.edtKet.text.toString()

        if (tagId != null && wo.isNotEmpty() && peralatan.isNotEmpty() && lokasi.isNotEmpty() && posisi.isNotEmpty()){
            loading(true)
            api.tambaheloto(tagId, wo, peralatan, lokasi, posisi, ket).enqueue(object :
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
            info(tagId)
        }
    }

    override fun onStart() {
        super.onStart()
        getTag()
    }

    fun getTag() {
        loading(true)
        api.getLastTag()
            .enqueue(object : Callback<LastTagResponse> {
                override fun onResponse(
                    call: Call<LastTagResponse>,
                    response: Response<LastTagResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            binding.edtTag.text = response.body()!!.data.toString()
                            tagId = response.body()!!.data.toString()
                        } else {
                            loading(false)
                            toast("gagal mendapatkan response")
                            info ( response )
                        }
                    } catch (e: Exception) {
                        loading(false)
                        info { "dinda ${e.message}" }
                        toast("kesalahan server")
                    }
                }

                override fun onFailure(call: Call<LastTagResponse>, t: Throwable) {
                    loading(false)
                    info { "dinda ${t.message}" }
                    toast("Periksa Koneksi Anda")
                }

            })


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