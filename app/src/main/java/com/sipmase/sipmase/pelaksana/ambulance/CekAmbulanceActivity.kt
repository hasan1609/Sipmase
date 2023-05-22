package com.sipmase.sipmase.pelaksana.ambulance

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityCekAmbulanceBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.ambulance.AmbulanceModel
import com.sipmase.sipmase.model.postdata.UpdateAmbulance
import com.sipmase.sipmase.pelaksana.damkar.CekDamkarActivity
import com.sipmase.sipmase.session.SessionManager
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CekAmbulanceActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekAmbulanceBinding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog

    companion object {
        var cekambulance: AmbulanceModel? = null
    }

    var currentDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_ambulance)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekambulance =
            gson.fromJson(
                intent.getStringExtra("cekambulance"),
                AmbulanceModel::class.java
            )

        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = currentDate
        if(cekambulance!!.isStatus == 1){
            binding.btnDraft.visibility = View.GONE
            binding.btnSubmit.visibility = View.GONE
        }else if(cekambulance!!.isStatus == 3){
            binding.btnDraft.visibility = View.GONE
        }
        if(cekambulance!!.shift != null){
            binding.txtshift.text = cekambulance!!.shift
        }else{
            binding.txtshift.text = sessionManager.getNama()
        }

        binding.edttabungoksigen.setText(cekambulance!!.tabungOksigen)
         binding.edttekananoksigen.setText(cekambulance!!.toTekanan)
         binding.edttapar.setText(cekambulance!!.apar)
         binding.edttekananapar.setText(cekambulance!!.aTekanan)
         binding.edtfisikapar.setText(cekambulance!!.kondisiFisik)
         binding.edtcairanInfus.setText(cekambulance!!.cairanInfus)
         binding.edtperlakAlas.setText(cekambulance!!.perlak)
         binding.edttanduDorong.setText(cekambulance!!.tanduDorong)
         binding.edtkebersihan.setText(cekambulance!!.kebersihan)
        binding.edtroda.setText(cekambulance!!.roda)
         binding.edtsponsKasur.setText(cekambulance!!.sponsKasur)
         binding.edttanduLipat.setText(cekambulance!!.tanduLipat)
         binding.airWastafel.setText(cekambulance!!.airWastafel)
         binding.antiSepticGel.setText(cekambulance!!.antisepticGel)
         binding.tisu.setText(cekambulance!!.tisuKering)
         binding.oxycan.setText(cekambulance!!.oxycan)
         binding.tasP3k.setText(cekambulance!!.tasP3k)
         binding.txtketerangan.setText(cekambulance!!.catatan)


        binding.btnSubmit.setOnClickListener {
            val txtto = binding.edttabungoksigen.text.toString()
            val txttoT = binding.edttekananoksigen.text.toString()
            val txta = binding.edttapar.text.toString()
            val txtaT = binding.edttekananapar.text.toString()
            val txtaKs = binding.edtfisikapar.text.toString()
            val txtci = binding.edtcairanInfus.text.toString()
            val txtpa = binding.edtperlakAlas.text.toString()
            val txttd = binding.edttanduDorong.text.toString()
            val txttdK = binding.edtkebersihan.text.toString()
            val txttdR = binding.edtroda.text.toString()
            val txttdSk = binding.edtsponsKasur.text.toString()
            val txttl = binding.edttanduLipat.text.toString()
            val txtaw = binding.airWastafel.text.toString()
            val txtag = binding.antiSepticGel.text.toString()
            val txttk = binding.tisu.text.toString()
            val txto = binding.oxycan.text.toString()
            val txttp = binding.tasP3k.text.toString()
            val txtc = binding.txtketerangan.text.toString()

            if (
                txtto.isNotEmpty() &&
                txttoT.isNotEmpty() &&
                txta.isNotEmpty() &&
                txtaT.isNotEmpty() &&
                txtaKs.isNotEmpty() &&
                txtci.isNotEmpty() &&
                txtpa.isNotEmpty() &&
                txttd.isNotEmpty() &&
                txttdK.isNotEmpty() &&
                txttdR.isNotEmpty() &&
                txttdSk.isNotEmpty() &&
                txttl.isNotEmpty() &&
                txtaw.isNotEmpty() &&
                txtag.isNotEmpty() &&
                txttk.isNotEmpty() &&
                txto.isNotEmpty() &&
                txttp.isNotEmpty() &&
                txtc.isNotEmpty()) {
                loading(true)
                val updateAmbulance = UpdateAmbulance(
                    txttk,
                    currentDate,
                    sessionManager.getNama(),
                    txtaKs,
                    txta,
                    txttdR,
                    1,
                    txttdSk,
                    txtto,
                    cekambulance!!.id,
                    txto,
                    txtaw,
                    txttdK,
                    txtag,
                    txtc,
                    txtaT,
                    txttd,
                    txttoT,
                    txttl,
                    txttp,
                    txtpa,
                    txtci
                )
                api.update_ambulanceku(updateAmbulance).enqueue(object :
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

            } else {
                toast("Jangan kosongi kolom")
            }

        }
        binding.btnDraft.setOnClickListener {
            val txtto = binding.edttabungoksigen.text.toString()
            val txttoT = binding.edttekananoksigen.text.toString()
            val txta = binding.edttapar.text.toString()
            val txtaT = binding.edttekananapar.text.toString()
            val txtaKs = binding.edtfisikapar.text.toString()
            val txtci = binding.edtcairanInfus.text.toString()
            val txtpa = binding.edtperlakAlas.text.toString()
            val txttd = binding.edttanduDorong.text.toString()
            val txttdK = binding.edtkebersihan.text.toString()
            val txttdR = binding.edtroda.text.toString()
            val txttdSk = binding.edtsponsKasur.text.toString()
            val txttl = binding.edttanduLipat.text.toString()
            val txtaw = binding.airWastafel.text.toString()
            val txtag = binding.antiSepticGel.text.toString()
            val txttk = binding.tisu.text.toString()
            val txto = binding.oxycan.text.toString()
            val txttp = binding.tasP3k.text.toString()
            val txtc = binding.txtketerangan.text.toString()

            loading(true)
            val updateAmbulance = UpdateAmbulance(
                txttk,
                currentDate,
                sessionManager.getNama(),
                txtaKs,
                txta,
                txttdR,
                cekambulance!!.isStatus,
                txttdSk,
                txtto,
                cekambulance!!.id,
                txto,
                txtaw,
                txttdK,
                txtag,
                txtc,
                txtaT,
                txttd,
                txttoT,
                txttl,
                txttp,
                txtpa,
                txtci
            )
            api.update_ambulanceku(updateAmbulance).enqueue(object :
                Callback<PostDataResponse> {
                override fun onResponse(
                    call: Call<PostDataResponse>,
                    response: Response<PostDataResponse>
                ) {
                    if (response.isSuccessful) {
                        loading(false)
                        if (response.body()!!.sukses == 1) {
                            finish()
                            toast("Draft telah sisimpan")
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


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        cekambulance = null
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