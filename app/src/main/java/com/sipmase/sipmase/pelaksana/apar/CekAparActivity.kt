package com.sipmase.sipmase.pelaksana.apar

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityCekAparBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.ScheduleAparPelaksanaModel
import com.sipmase.sipmase.model.postdata.PostScheduleApar
import com.sipmase.sipmase.pelaksana.apar.QrCoderCekAparActivity.Companion.jenis
import com.sipmase.sipmase.pelaksana.apar.QrCoderCekAparActivity.Companion.kadaluarsa
import com.sipmase.sipmase.pelaksana.apar.QrCoderCekAparActivity.Companion.kodeapar
import com.sipmase.sipmase.pelaksana.apar.QrCoderCekAparActivity.Companion.lokasi
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

class CekAparActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekAparBinding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog
    companion object {
        var cekapar: ScheduleAparPelaksanaModel? = null
    }
    var currentDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_apar)
        binding.lifecycleOwner = this

        val gson = Gson()
        cekapar =
            gson.fromJson(intent.getStringExtra("cekapar"), ScheduleAparPelaksanaModel::class.java)

        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = currentDate
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        binding.txtshift.text = sessionManager.getNama()

        val xmlArray: Array<String> = resources.getStringArray(R.array.arr1)
        if (cekapar!!.tabung != null) {
            binding.txttabung.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.tabung }
            ))
        }
        if (cekapar!!.pin != null) {
            binding.txtpin.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.pin }
            ))
        }
        if (cekapar!!.segel != null) {
            binding.txtsegel.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.segel }
            ))
        }
        if (cekapar!!.handle != null) {
            binding.txthandle.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.handle }
            ))
        }
        if (cekapar!!.pressure != null) {
            binding.txtpressureIndikator.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.pressure }
            ))
        }
        if (cekapar!!.tuas != null) {
            binding.txttuas.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.tuas }
            ))
        }
        if (cekapar!!.selang != null) {
            binding.txtselang.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.selang }
            ))
        }
        if (cekapar!!.nozzle != null) {
            binding.txtnozzle.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.nozzle }
            ))
        }
        if (cekapar!!.rambu != null) {
            binding.txtrambuSegitiga.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.rambu }
            ))
        }
        if (cekapar!!.gantungan != null) {
            binding.txtgantungan.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.gantungan }
            ))
        }
        if (cekapar!!.houskeeping != null) {
            binding.txthouseKeeping.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekapar!!.houskeeping }
            ))
        }
        if (cekapar!!.kapasitas != null) {
            binding.txtkapasitas.setText(cekapar!!.kapasitas.toString())
        }
        if (cekapar!!.keteranganTambahan != null) {
            binding.txtketerangan.setText(cekapar!!.keteranganTambahan.toString())
        }
        binding.scan.setOnClickListener {
            startActivity<QrCoderCekAparActivity>()
        }

        binding.btnDraft.setOnClickListener {
            loading(true)
            val keterangan = binding.txtketerangan.text.toString().trim()
            val kapasitas = binding.txtkapasitas.text.toString().trim()
            val spntabung = binding.txttabung.selectedItem
            val spnpin = binding.txtpin.selectedItem
            val spnsegel = binding.txtsegel.selectedItem
            val spnhandle = binding.txthandle.selectedItem
            val spnpressure = binding.txtpressureIndikator.selectedItem
            val spnselang = binding.txtselang.selectedItem
            val spnnoozle = binding.txtnozzle.selectedItem
            val spnrambusegitiga = binding.txtrambuSegitiga.selectedItem
            val spngantungan = binding.txtgantungan.selectedItem
            val spnhousekeeping = binding.txthouseKeeping.selectedItem
            val spntuas = binding.txttuas.selectedItem
            val updatescheduleapar =
                PostScheduleApar(
                    cekapar!!.tw,
                    cekapar!!.tahun,
                    spntuas.toString(),
                    spnrambusegitiga.toString(),
                    spngantungan.toString(),
                    currentDate,
                    sessionManager.getNama(),
                    cekapar!!.tanggalCek,
                    spnpressure.toString(),
                    spntabung.toString(),
                    spnnoozle.toString(),
                    cekapar!!.isStatus,
                    spnpin.toString(),
                    keterangan,
                    spnsegel.toString(),
                    cekapar!!.id,
                    spnhousekeeping.toString(),
                    spnselang.toString(),
                    kapasitas.toInt(),
                    spnhandle.toString()
                )

            api.update_schedule_apar(cekapar!!.id, updatescheduleapar).enqueue(object :
                Callback<PostDataResponse> {
                override fun onResponse(
                    call: Call<PostDataResponse>,
                    response: Response<PostDataResponse>
                ) {
                    if (response.isSuccessful){
                        loading(false)
                        if (response.body()!!.sukses ==1){
                            finish()
                            toast("Draft Berhasil disimpan")
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
            val keterangan = binding.txtketerangan.text.toString().trim()
            val kapasitas = binding.txtkapasitas.text.toString().trim()
            val spntabung = binding.txttabung.selectedItem
            val spnpin = binding.txtpin.selectedItem
            val spnsegel = binding.txtsegel.selectedItem
            val spnhandle = binding.txthandle.selectedItem
            val spnpressure = binding.txtpressureIndikator.selectedItem
            val spnselang = binding.txtselang.selectedItem
            val spnnoozle = binding.txtnozzle.selectedItem
            val spnrambusegitiga = binding.txtrambuSegitiga.selectedItem
            val spngantungan = binding.txtgantungan.selectedItem
            val spnhousekeeping = binding.txthouseKeeping.selectedItem
            val spntuas = binding.txttuas.selectedItem
            if (kodeapar != null && jenis != null && lokasi != null && kadaluarsa != null) {
                loading(true)
                val updatescheduleapar =
                    PostScheduleApar(
                        cekapar!!.tw,
                        cekapar!!.tahun,
                        spntuas.toString(),
                        spnrambusegitiga.toString(),
                        spngantungan.toString(),
                        currentDate,
                        sessionManager.getNama(),
                        cekapar!!.tanggalCek,
                        spnpressure.toString(),
                        spntabung.toString(),
                        spnnoozle.toString(),
                        1,
                        spnpin.toString(),
                        keterangan,
                        spnsegel.toString(),
                        cekapar!!.id,
                        spnhousekeeping.toString(),
                        spnselang.toString(),
                        kapasitas.toInt(),
                        spnhandle.toString()
                    )

                api.update_schedule_apar(cekapar!!.id, updatescheduleapar).enqueue(object :
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
            }else{
                toast("Harap scan APAT terlebih dahulu")
            }

        }
    }

    override fun onStart() {
        super.onStart()
        //CEK APAR
        if (kodeapar != null && jenis != null && lokasi != null && kadaluarsa != null) {
            binding.txtKode.text = kodeapar.toString()
            binding.txtjenis.text = jenis.toString()
            binding.txtlokasi.text = lokasi.toString()
            binding.txttglKadaluarsa.text = kadaluarsa.toString()
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

    override fun onBackPressed() {
        super.onBackPressed()
        kodeapar = null
        jenis = null
        lokasi = null
        kadaluarsa = null
    }

}