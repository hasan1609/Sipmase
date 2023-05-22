package com.sipmase.sipmase.admin.apar.activity

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityDetailCekAparBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.ScheduleAparPelaksanaModel
import com.sipmase.sipmase.model.ScheduleModel
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class DetailCekAparActivity : AppCompatActivity(), AnkoLogger {

    lateinit var binding: ActivityDetailCekAparBinding
    lateinit var cekapar: ScheduleAparPelaksanaModel
    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_apar)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekapar =
            gson.fromJson(intent.getStringExtra("cekapar"), ScheduleAparPelaksanaModel::class.java)

        if (cekapar.isStatus == 0 || cekapar.isStatus ==2 || cekapar.isStatus ==3){
            binding.lyBtn.visibility = View.GONE
        }
        if (cekapar.isStatus ==1){
            binding.lyBtn.visibility = View.VISIBLE
        }

        binding.txtKode.text = cekapar.kodeApar.toString()
        binding.txtshift.text = cekapar.shift.toString()
        binding.txttgl.text = cekapar.tanggalCek.toString()
        binding.txtjenis.text = cekapar.apar!!.jenis.toString()
        binding.txtlokasi.text = cekapar.apar!!.lokasi.toString()
        binding.txttglKadaluarsa.text = cekapar.apar!!.tglKadaluarsa.toString()

        binding.txtkapasitas.text = cekapar.kapasitas.toString()
        binding.txttabung.text = cekapar.tabung.toString()
        binding.txtpin.text = cekapar.pin.toString()
        binding.txtsegel.text = cekapar.segel.toString()
        binding.txthandle.text = cekapar.handle.toString()
        binding.txtpressureIndikator.text = cekapar.pressure.toString()
        binding.txttuas.text = cekapar.tuas.toString()
        binding.txtselang.text = cekapar.selang.toString()
        binding.txtnozzle.text = cekapar.nozzle.toString()
        binding.txtrambuSegitiga.text = cekapar.rambu.toString()
        binding.txtgantungan.text = cekapar.gantungan.toString()
        binding.txthouseKeeping.text = cekapar.houskeeping.toString()

        binding.txtketerangan.text = cekapar.keteranganTambahan.toString()

        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("APAR")
            builder.setMessage("Return APAR ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_apar(cekapar.id!!).enqueue(object : Callback<PostDataResponse> {
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
            builder.setTitle("APAT")
            builder.setMessage("Acc APAT ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_apar(cekapar.id!!).enqueue(object : Callback<PostDataResponse> {
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