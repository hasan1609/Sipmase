package com.sipmase.sipmase.pelaksana.pencahyaan

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityCekPencahayaanBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.pencahayaan.SchedulePencahayaanModel
import com.sipmase.sipmase.model.pencahayaan.UpdateSchedulePencahayaan
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

class CekPencahayaanActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekPencahayaanBinding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog
    companion object {
        var cekpencahayaan: SchedulePencahayaanModel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_pencahayaan)
        binding.lifecycleOwner = this
        val gson = Gson()
        cekpencahayaan =
            gson.fromJson(
                intent.getStringExtra("cekpencahayaan"),
                SchedulePencahayaanModel::class.java
            )

        val sdf = SimpleDateFormat("yyyy-M-dd")
        binding.txttgl.text = cekpencahayaan!!.tanggalCek


        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)

        binding.txtshift.text = sessionManager.getNama()

        val xmlArray: Array<String> = resources.getStringArray(R.array.sumber)
        if (cekpencahayaan!!.sumberPencahayaan != null) {
            binding.txtsumberCahaya.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekpencahayaan!!.sumberPencahayaan }
            ))
        }
        if (cekpencahayaan!!.lux1 != null) {
            binding.txtlux1.setText(cekpencahayaan!!.lux1.toString())
        }
        if (cekpencahayaan!!.lux2 != null) {
            binding.txtlux2.setText(cekpencahayaan!!.lux2.toString())
        }
        if (cekpencahayaan!!.lux3 != null) {
            binding.txtlux3.setText(cekpencahayaan!!.lux3.toString())
        }
        if (cekpencahayaan!!.keterangan != null) {
            binding.txtketerangan.setText(cekpencahayaan!!.keterangan.toString())
        }
        binding.scan.setOnClickListener {
            startActivity<QrCodeCekPencahayaanActivity>()
        }

        binding.btnDraft.setOnClickListener {
            val keterangan = binding.txtketerangan.text.toString().trim()
            val lux1 = binding.txtlux1.text.toString().trim()
            val lux2 = binding.txtlux2.text.toString().trim()
            val lux3 = binding.txtlux3.text.toString().trim()
//            val luxrata2 = binding.txtluxrata2.text.toString().trim()
            val sumber_pencahayaan = binding.txtsumberCahaya.selectedItem.toString()

            if (keterangan.isNotEmpty() && lux1.isNotEmpty()
                && lux2.isNotEmpty() && lux3.isNotEmpty() && QrCodeCekPencahayaanActivity . kodepencahayaan != null && QrCodeCekPencahayaanActivity.lokasi != null) {
                loading(true)

                val updateschedulepencahayaan = UpdateSchedulePencahayaan(
                    keterangan,
                    cekpencahayaan!!.tw,
                    cekpencahayaan!!.tahun,
                    sessionManager.getNama().toString(),
                    cekpencahayaan!!.tanggalCek,
                    lux1.toInt(),
                    cekpencahayaan!!.isStatus,
                    lux2.toInt(),
                    lux3.toInt(),
//                    luxrata2.toFloat(),
                    sumber_pencahayaan,
                    cekpencahayaan!!.id,
                    cekpencahayaan!!.tanggalCek
                )


                api.update_schedule_pencahayaan(updateschedulepencahayaan).enqueue(object :
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
            val lux1 = binding.txtlux1.text.toString().trim()
            val lux2 = binding.txtlux2.text.toString().trim()
            val lux3 = binding.txtlux3.text.toString().trim()
//            val luxrata2 = binding.txtluxrata2.text.toString().trim()
            val sumber_pencahayaan = binding.txtsumberCahaya.selectedItem.toString()

            if (keterangan.isNotEmpty() && lux1.isNotEmpty()
                && lux2.isNotEmpty() && lux3.isNotEmpty() && QrCodeCekPencahayaanActivity . kodepencahayaan != null && QrCodeCekPencahayaanActivity.lokasi != null) {
                loading(true)

                val updateschedulepencahayaan = UpdateSchedulePencahayaan(
                    keterangan,
                    cekpencahayaan!!.tw,
                    cekpencahayaan!!.tahun,
                    sessionManager.getNama().toString(),
                    cekpencahayaan!!.tanggalCek,
                    lux1.toInt(),
                    1,
                    lux2.toInt(),
                    lux3.toInt(),
//                    luxrata2.toFloat(),
                    sumber_pencahayaan,
                    cekpencahayaan!!.id,
                    cekpencahayaan!!.tanggalCek
                )


                api.update_schedule_pencahayaan(updateschedulepencahayaan).enqueue(object :
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
        //CEK pencahayaan
        if (QrCodeCekPencahayaanActivity.kodepencahayaan != null && QrCodeCekPencahayaanActivity.lokasi != null) {
            binding.txtKode.text =
                QrCodeCekPencahayaanActivity.kodepencahayaan.toString()
            binding.txtlokasi.text =
                QrCodeCekPencahayaanActivity.lokasi.toString()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        cekpencahayaan = null
        QrCodeCekPencahayaanActivity.kodepencahayaan = null
        QrCodeCekPencahayaanActivity.lokasi = null
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