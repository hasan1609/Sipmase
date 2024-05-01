package com.sipmase.sipmase.pelaksana.hydrant

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityCekhydrantBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.hydrant.ScheduleHydrantPelaksanaModel
import com.sipmase.sipmase.model.postdata.UpdateScheduleHydrant
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

class CekhydrantActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekhydrantBinding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog

    companion object {
        var cekhydrant: ScheduleHydrantPelaksanaModel? = null

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cekhydrant)
        binding.lifecycleOwner = this

        val gson = Gson()
        cekhydrant =
            gson.fromJson(intent.getStringExtra("cekhydrant"), ScheduleHydrantPelaksanaModel::class.java)

        binding.txttgl.text = cekhydrant!!.tanggalCek


        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)

        binding.txtshift.text = sessionManager.getNama()

        val xmlArray: Array<String> = resources.getStringArray(R.array.arr1)
        val yesno: Array<String> = resources.getStringArray(R.array.yatidak)
        val openclose: Array<String> = resources.getStringArray(R.array.opncls)

        if (cekhydrant!!.flushing != null) {
            binding.txtflashing.setSelection(yesno.indexOf(
                yesno.first { elem -> elem == cekhydrant!!.flushing }
            ))
        }
        if (cekhydrant!!.mainValve != null) {
            binding.txtmainValve.setSelection(openclose.indexOf(
                openclose.first { elem -> elem == cekhydrant!!.mainValve }
            ))
        }
        if (cekhydrant!!.discharge != null) {
            binding.txtdichargeValve.setSelection(openclose.indexOf(
                openclose.first { elem -> elem == cekhydrant!!.discharge }
            ))
        }
        if (cekhydrant!!.kondisiBox != null) {
            binding.txtkondisiBox.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekhydrant!!.kondisiBox }
            ))
        }
        if (cekhydrant!!.kunciBox != null) {
            binding.txtkunciBox.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekhydrant!!.kunciBox }
            ))
        }
        if (cekhydrant!!.kunciF != null) {
            binding.txtkunciF.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekhydrant!!.kunciF }
            ))
        }
        if (cekhydrant!!.selang != null) {
            binding.txtselang.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekhydrant!!.selang }
            ))
        }
        if (cekhydrant!!.noozle != null) {
            binding.txtnozzle.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekhydrant!!.noozle }
            ))
        }
        if (cekhydrant!!.houseKeeping != null) {
            binding.txthouseKeeping.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == cekhydrant!!.houseKeeping }
            ))
        }
        if (cekhydrant!!.keterangan != null) {
            binding.txtketerangan.setText(cekhydrant!!.keterangan.toString())
        }

        binding.scan.setOnClickListener {
            startActivity<QrCodeCekHydrantActivity>()
        }

        binding.btnDraft.setOnClickListener {
            if (QrCodeCekHydrantActivity.kodehydrant != null && QrCodeCekHydrantActivity.no != null && QrCodeCekHydrantActivity.lokasi != null) {
                loading(true)
                val keterangan = binding.txtketerangan.text.toString().trim()
                val spnflashing = binding.txtflashing.selectedItem
                val spnmainvalve = binding.txtmainValve.selectedItem
                val spndischarge = binding.txtdichargeValve.selectedItem
                val spnkondisibox = binding.txtkondisiBox.selectedItem
                val spnkuncibox = binding.txtkunciBox.selectedItem
                val spnkuncif = binding.txtkunciF.selectedItem
                val spnselang = binding.txtselang.selectedItem
                val spnnozzle = binding.txtnozzle.selectedItem
                val spnhousekeeping = binding.txthouseKeeping.selectedItem

                val updateschedulehydrant = UpdateScheduleHydrant(
                    spnnozzle.toString(),
                    keterangan,
                    cekhydrant!!.tw,
                    spnkuncibox.toString(),
                    cekhydrant!!.tahun,
                    sessionManager.getNama().toString(),
                    cekhydrant!!.tanggalCek,
                    spnmainvalve.toString(),
                    spnkuncif.toString(),
                    cekhydrant!!.isStatus,
                    spnhousekeeping.toString(),
                    spndischarge.toString(),
                    spnkondisibox.toString(),
                    cekhydrant!!.id,
                    spnflashing.toString(),
                    spnselang.toString(),
                    cekhydrant!!.tanggalCek
                )

                api.update_schedule_hydrant(updateschedulehydrant).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        if (response.isSuccessful){
                            loading(false)
                            if (response.body()!!.sukses ==1){
                                finish()
                                toast("Draft berhasil disimpan")
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

        }

        binding.btnSubmit.setOnClickListener {
            if (QrCodeCekHydrantActivity.kodehydrant != null && QrCodeCekHydrantActivity.no != null && QrCodeCekHydrantActivity.lokasi != null) {
                loading(true)
                val keterangan = binding.txtketerangan.text.toString().trim()
                val spnflashing = binding.txtflashing.selectedItem
                val spnmainvalve = binding.txtmainValve.selectedItem
                val spndischarge = binding.txtdichargeValve.selectedItem
                val spnkondisibox = binding.txtkondisiBox.selectedItem
                val spnkuncibox = binding.txtkunciBox.selectedItem
                val spnkuncif = binding.txtkunciF.selectedItem
                val spnselang = binding.txtselang.selectedItem
                val spnnozzle = binding.txtnozzle.selectedItem
                val spnhousekeeping = binding.txthouseKeeping.selectedItem

                val updateschedulehydrant = UpdateScheduleHydrant(
                    spnnozzle.toString(),
                    keterangan,
                    cekhydrant!!.tw,
                    spnkuncibox.toString(),
                    cekhydrant!!.tahun,
                    sessionManager.getNama().toString(),
                    cekhydrant!!.tanggalCek,
                    spnmainvalve.toString(),
                    spnkuncif.toString(),
                    1,
                    spnhousekeeping.toString(),
                    spndischarge.toString(),
                    spnkondisibox.toString(),
                    cekhydrant!!.id,
                    spnflashing.toString(),
                    spnselang.toString(),
                    cekhydrant!!.tanggalCek
                )

                api.update_schedule_hydrant(updateschedulehydrant).enqueue(object :
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


            }

        }

    }

    override fun onStart() {
        super.onStart()
        //CEK Hydrant
        if (QrCodeCekHydrantActivity.kodehydrant != null && QrCodeCekHydrantActivity.no != null && QrCodeCekHydrantActivity.lokasi != null ) {
            binding.txtKode.text = QrCodeCekHydrantActivity.kodehydrant.toString()
            binding.txtnobox.text = QrCodeCekHydrantActivity.no.toString()
            binding.txtlokasi.text = QrCodeCekHydrantActivity.lokasi.toString()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        cekhydrant = null
        QrCodeCekHydrantActivity.kodehydrant = null
        QrCodeCekHydrantActivity.no = null
        QrCodeCekHydrantActivity.lokasi = null
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