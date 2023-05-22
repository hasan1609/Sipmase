package com.sipmase.sipmase.admin.pencahayaan.activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityDetailCekKebisinganBinding
import com.sipmase.sipmase.databinding.ActivityDetailCekPencahayaanBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.kebisingan.ScheduleKebisinganModel
import com.sipmase.sipmase.model.pencahayaan.SchedulePencahayaanModel
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCekPencahayaanActivity : AppCompatActivity(), AnkoLogger {

    lateinit var cekpencahayaan: SchedulePencahayaanModel
    lateinit var binding: ActivityDetailCekPencahayaanBinding
    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_pencahayaan)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekpencahayaan =
            gson.fromJson(intent.getStringExtra("cekpencahayaan"), SchedulePencahayaanModel::class.java)

        if (cekpencahayaan.isStatus == 0 || cekpencahayaan.isStatus == 2 || cekpencahayaan.isStatus == 3){
            binding.lyBtn.visibility = View.GONE
        }
        if (cekpencahayaan.isStatus ==1){
            binding.lyBtn.visibility = View.VISIBLE
        }

        binding.txtKode.text = cekpencahayaan.kodePencahayaan.toString()
        binding.txtshift.text = cekpencahayaan.shift.toString()
        binding.txttgl.text = cekpencahayaan.tanggalCek.toString()
        binding.txtlokasi.text = cekpencahayaan.pencahayaan!!.lokasi.toString()

        binding.txtlux1.text = cekpencahayaan.lux1.toString()
        binding.txtlux2.text = cekpencahayaan.lux2.toString()
        binding.txtlux3.text = cekpencahayaan.lux3.toString()
        binding.txtrataRata.text = cekpencahayaan.luxrata2.toString()
        binding.txtsumberCahaya.text = cekpencahayaan.sumberPencahayaan.toString()

        binding.txtketerangan.text = cekpencahayaan.keterangan.toString()

        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pencahayaan")
            builder.setMessage("Return Pencahayaan ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_pencahayaan(cekpencahayaan.id!!).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.body()!!.sukses == 1) {
                                loading(false)
                                toast("return schedule berhasil")
                                finish()
                            } else {
                                loading(false)
                                Snackbar.make(it, "Coba Lagi", Snackbar.LENGTH_SHORT)
                                    .show()

                            }


                        } catch (e: Exception) {
                            progressDialog.dismiss()
                            info { "dinda ${e.message}${response.code()} " }
                        }

                    }

                    override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                        loading(false)
                        Snackbar.make(it, "Kesalahan jaringan", Snackbar.LENGTH_SHORT).show()

                    }

                })
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()
        }

        binding.btnTerima.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pencahayaan")
            builder.setMessage("Acc Pencahayaan ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_pencahayaan(cekpencahayaan.id!!).enqueue(object : Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.body()!!.sukses == 1) {
                                loading(false)
                                toast("acc schedule berhasil")
                                finish()
                            } else {
                                loading(false)
                                Snackbar.make(it, "acc  gagal", Snackbar.LENGTH_SHORT)
                                    .show()

                            }


                        } catch (e: Exception) {
                            progressDialog.dismiss()
                            info { "dinda ${e.message}${response.code()} " }
                        }

                    }

                    override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                        loading(false)
                        Snackbar.make(it, "Kesalahan jaringan", Snackbar.LENGTH_SHORT).show()

                    }

                })
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()

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