package com.sipmase.sipmase.admin.kebisingan.activity

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
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.hydrant.HasilHydrantModel
import com.sipmase.sipmase.model.kebisingan.ScheduleKebisinganModel
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCekKebisinganActivity : AppCompatActivity(), AnkoLogger {

    lateinit var cekkbisingan: ScheduleKebisinganModel
    lateinit var binding: ActivityDetailCekKebisinganBinding
    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_kebisingan)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekkbisingan =
            gson.fromJson(intent.getStringExtra("cekkebisingan"), ScheduleKebisinganModel::class.java)

        if (cekkbisingan.isStatus == 0 || cekkbisingan.isStatus == 2 || cekkbisingan.isStatus == 3){
            binding.lyBtn.visibility = View.GONE
        }
        if (cekkbisingan.isStatus ==1){
            binding.lyBtn.visibility = View.VISIBLE
        }

        binding.txtKode.text = cekkbisingan.kodeKebisingan.toString()
        binding.txtshift.text = cekkbisingan.shift.toString()
        binding.txttgl.text = cekkbisingan.tanggalCek.toString()
        binding.txtlokasi.text = cekkbisingan.kebisingan!!.lokasi.toString()

        binding.txtdb1.text = cekkbisingan.dbx1.toString()
        binding.txtdb2.text = cekkbisingan.dbx2.toString()
        binding.txtdb3.text = cekkbisingan.dbx3.toString()
        binding.txtrataRata.text = cekkbisingan.dbrata2.toString()
        binding.txtstatus.text = cekkbisingan.status.toString()

        binding.txtketerangan.text = cekkbisingan.keterangan.toString()

        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Kebisingan")
            builder.setMessage("Return Kebisingan ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_kebisingan(cekkbisingan.id!!).enqueue(object : Callback<PostDataResponse> {
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
            builder.setTitle("Kebisingan")
            builder.setMessage("Acc Kebisingan ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_kebisingan(cekkbisingan.id!!).enqueue(object : Callback<PostDataResponse> {
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