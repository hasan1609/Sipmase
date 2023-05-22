package com.sipmase.sipmase.pelaksana.apat

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityCekApatBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.apat.HasilApatModel
import com.sipmase.sipmase.model.postdata.UpdateScheduleApat
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

class CekApatActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekApatBinding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog
    companion object {
        var cekapat: HasilApatModel? = null
    }
    var currentDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_apat)
        binding.lifecycleOwner = this

        val gson = Gson()
        cekapat =
            gson.fromJson(intent.getStringExtra("cekapat"), HasilApatModel::class.java)

        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = currentDate

        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)

        binding.txtshift.text = sessionManager.getNama()
        val xmlArray: Array<String> = resources.getStringArray(R.array.arr1)
        if (cekapat!!.bak != null) {
            binding.txtbak.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapat!!.bak }
            ))
        }
        if (cekapat!!.pasir != null) {
            binding.txtpasir.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapat!!.pasir }
            ))
        }
        if (cekapat!!.karung != null) {
            binding.txtkarung.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapat!!.karung }
            ))
        }
        if (cekapat!!.ember != null) {
            binding.txtember.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapat!!.ember }
            ))
        }
        if (cekapat!!.sekop != null) {
            binding.txtsekop.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapat!!.sekop }
            ))
        }
        if (cekapat!!.gantungan != null) {
            binding.txtgantungan.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapat!!.gantungan }
            ))
        }
        if (cekapat!!.keterangan != null) {
            binding.txtketerangan.setText(cekapat!!.keterangan)
        }


        binding.scan.setOnClickListener {
            startActivity<QrCodeCekApatActivity>()
        }

        binding.btnDraft.setOnClickListener {
            loading(true)
            val spnbak = binding.txtbak.selectedItem
            val spnember = binding.txtember.selectedItem
            val spngantungan = binding.txtgantungan.selectedItem
            val spnkarung = binding.txtkarung.selectedItem
            val spnpasir = binding.txtpasir.selectedItem
            val spnsekop = binding.txtsekop.selectedItem
            val keterangan = binding.txtketerangan.text.toString().trim()
            val updatescheduleapat = UpdateScheduleApat(
                keterangan,
                cekapat!!.tw,
                cekapat!!.tahun,
                spnember.toString(),
                spngantungan.toString(),
                sessionManager.getNama(),
                cekapat!!.tanggalCek,
                spnbak!!.toString(),
                cekapat!!.isStatus,
                spnkarung.toString(),
                spnpasir.toString(),
                cekapat!!.id,
                spnsekop.toString(),
                null
            )

            api.update_schedule_apat(updatescheduleapat).enqueue(object :
                Callback<PostDataResponse> {
                override fun onResponse(
                    call: Call<PostDataResponse>,
                    response: Response<PostDataResponse>
                ) {
                    if (response.isSuccessful){
                        loading(false)
                        if (response.body()!!.sukses ==1){
                            finish()
                            toast("Draft telah tersimpan")

                        }else{
                            finish()
                            toast("silahkan coba lagi")
                        }
                    }else{
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
        }

        binding.btnSubmit.setOnClickListener {
            val spnbak = binding.txtbak.selectedItem
            val spnember = binding.txtember.selectedItem
            val spngantungan = binding.txtgantungan.selectedItem
            val spnkarung = binding.txtkarung.selectedItem
            val spnpasir = binding.txtpasir.selectedItem
            val spnsekop = binding.txtsekop.selectedItem
            val keterangan = binding.txtketerangan.text.toString().trim()
            if (QrCodeCekApatActivity.kodeapat != null && QrCodeCekApatActivity.no_bak != null && QrCodeCekApatActivity.lokasi != null) {
                loading(true)
                val updatescheduleapat = UpdateScheduleApat(
                    keterangan,
                    cekapat!!.tw,
                    cekapat!!.tahun,
                    spnember.toString(),
                    spngantungan.toString(),
                    sessionManager.getNama(),
                    cekapat!!.tanggalCek,
                    spnbak!!.toString(),
                    1,
                    spnkarung.toString(),
                    spnpasir.toString(),
                    cekapat!!.id,
                    spnsekop.toString(),
                    currentDate
                )

                api.update_schedule_apat(updatescheduleapat).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        if (response.isSuccessful){
                            loading(false)
                            if (response.body()!!.sukses ==1){
                                finish()
                                toast("Tunggu approve admin")
                            }else{
                                finish()
                                toast("silahkan coba lagi")
                            }
                        }else{
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
            } else {
                toast("Harap Scan Terlebih Dahulu")
            }
        }

    }

    override fun onStart() {
        super.onStart()
        //CEK APAR
        if (QrCodeCekApatActivity.kodeapat != null && QrCodeCekApatActivity.no_bak != null && QrCodeCekApatActivity.lokasi != null ) {
            binding.txtKode.text = QrCodeCekApatActivity.kodeapat.toString()
            binding.txtnobak.text = QrCodeCekApatActivity.no_bak.toString()
            binding.txtlokasi.text = QrCodeCekApatActivity.lokasi.toString()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        cekapat = null
        QrCodeCekApatActivity.kodeapat = null
        QrCodeCekApatActivity.no_bak = null
        QrCodeCekApatActivity.lokasi = null
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