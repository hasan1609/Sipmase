package com.sipmase.sipmase.pelaksana.kebisingan

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityCekKebisinganBinding
import com.sipmase.sipmase.databinding.ActivityDetailCekKebisinganBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.kebisingan.ScheduleKebisinganModel
import com.sipmase.sipmase.model.kebisingan.UpdateScheduleKebisingan
import com.sipmase.sipmase.session.SessionManager
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CekKebisinganActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekKebisinganBinding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog

    companion object {
        var cekkebisingan: ScheduleKebisinganModel? = null

    }

    var currentDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_kebisingan)
        binding.lifecycleOwner = this

        val gson = Gson()
        cekkebisingan =
            gson.fromJson(
                intent.getStringExtra("cekkebisingan"),
                ScheduleKebisinganModel::class.java
            )

        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = currentDate


        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)

        binding.txtshift.text = sessionManager.getNama()
        val xmlArray: Array<String> = resources.getStringArray(R.array.nab)
        if (cekkebisingan!!.status != null) {
            binding.txtstatus.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekkebisingan!!.status }
            ))
        }
        if (cekkebisingan!!.dbx1 != null) {
            binding.txtdb1.setText(cekkebisingan!!.dbx1.toString())
        }
        if (cekkebisingan!!.dbx2 != null) {
            binding.txtdb2.setText(cekkebisingan!!.dbx2.toString())
        }
        if (cekkebisingan!!.dbx3 != null) {
            binding.txtdb3.setText(cekkebisingan!!.dbx3.toString())
        }
        if (cekkebisingan!!.keterangan != null) {
            binding.txtketerangan.setText(cekkebisingan!!.dbx1.toString())
        }
        binding.scan.setOnClickListener {
            startActivity<QrCodeCekKebisinganActivity>()
        }

        binding.btnDraft.setOnClickListener {
            val keterangan = binding.txtketerangan.text.toString().trim()
            val dbx1 = binding.txtdb1.text.toString().trim()
            val dbx2 = binding.txtdb2.text.toString().trim()
            val dbx3 = binding.txtdb3.text.toString().trim()
//            val dbrata2 = binding.txtdbrata2.text.toString().trim()
            val status = binding.txtstatus.selectedItem.toString()
            if (keterangan.isNotEmpty() && dbx1.isNotEmpty()
                && dbx2.isNotEmpty() && dbx3.isNotEmpty() && status.isNotEmpty() && QrCodeCekKebisinganActivity . kodekebisingan != null && QrCodeCekKebisinganActivity.lokasi != null) {
                loading(true)

                val updateschedulekebisingan = UpdateScheduleKebisingan(
                    keterangan,
                    cekkebisingan!!.tw,
                    cekkebisingan!!.tahun,
//                dbrata2,
                    sessionManager.getNama().toString(),
                    cekkebisingan!!.tanggalCek,
                    currentDate,
                    cekkebisingan!!.isStatus,
                    dbx2,
                    dbx3,
                    dbx1,
                    cekkebisingan!!.id,
                    status
                )

                api.update_schedule_kebisingan(updateschedulekebisingan).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.sukses == 1) {
                                finish()
                                toast("Draft berhasil disimpan")
                            } else {
                                finish()
                                toast("silahkan coba lagi")
                            }
                        } else {
                            loading(false)
                            toast("Response gagal")
                        }
                    }

                    override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                        loading(false)
                        toast("Jaringan error")
                        info { "dinda errror ${t.message}" }
                    }

                })


            }else{
                toast("Jangan kosongi kolom")
            }

        }

        binding.btnSubmit.setOnClickListener {
            val keterangan = binding.txtketerangan.text.toString().trim()
            val dbx1 = binding.txtdb1.text.toString().trim()
            val dbx2 = binding.txtdb2.text.toString().trim()
            val dbx3 = binding.txtdb3.text.toString().trim()
            val status = binding.txtstatus.selectedItem.toString()
            if (keterangan.isNotEmpty() && dbx1.isNotEmpty()
                && dbx2.isNotEmpty() && dbx3.isNotEmpty() && status.isNotEmpty() && QrCodeCekKebisinganActivity . kodekebisingan != null && QrCodeCekKebisinganActivity.lokasi != null) {
                loading(true)

                val updateschedulekebisingan = UpdateScheduleKebisingan(
                    keterangan,
                    cekkebisingan!!.tw,
                    cekkebisingan!!.tahun,
//                dbrata2,
                    sessionManager.getNama().toString(),
                    cekkebisingan!!.tanggalCek,
                    currentDate,
                    1,
                    dbx2,
                    dbx3,
                    dbx1,
                    cekkebisingan!!.id,
                    status
                )

                api.update_schedule_kebisingan(updateschedulekebisingan).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.sukses == 1) {
                                finish()
                                toast("Tunggu approve admin")
                            } else {
                                finish()
                                toast("silahkan coba lagi")
                            }
                        } else {
                            loading(false)
                            toast("Response gagal")
                        }
                    }

                    override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                        loading(false)
                        toast("Jaringan error")
                        info { "dinda errror ${t.message}" }
                    }

                })


            }else{
                toast("Jangan kosongi kolom")
            }

        }

    }

    override fun onStart() {
        super.onStart()
        //CEK kebisingan
        if (QrCodeCekKebisinganActivity.kodekebisingan != null && QrCodeCekKebisinganActivity.lokasi != null) {
            binding.txtKode.text =
                QrCodeCekKebisinganActivity.kodekebisingan.toString()
            binding.txtlokasi.text =
                QrCodeCekKebisinganActivity.lokasi.toString()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        cekkebisingan = null
        QrCodeCekKebisinganActivity.kodekebisingan = null
        QrCodeCekKebisinganActivity.lokasi = null
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