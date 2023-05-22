package com.sipmase.sipmase.admin.ambulance.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.ambulance.fragment.BottomSheetExportAmbulanceFragment
import com.sipmase.sipmase.databinding.ActivityDetailCekAmbulanceBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.ambulance.AmbulanceModel
import com.sipmase.sipmase.webservice.ApiClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class DetailCekAmbulanceActivity : AppCompatActivity(), AnkoLogger {

    lateinit var cekambulance: AmbulanceModel
    lateinit var binding: ActivityDetailCekAmbulanceBinding
    val sheet = BottomSheetExportAmbulanceFragment()

    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_ambulance)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekambulance =
            gson.fromJson(intent.getStringExtra("ambulance"), AmbulanceModel::class.java)

        if (cekambulance.isStatus ==1){
            binding.lyBtn.visibility = View.VISIBLE
            binding.btnExport.visibility = View.GONE
        }

        if (cekambulance.isStatus ==3 || cekambulance.isStatus == 0){
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.GONE
        }

        if (cekambulance.isStatus ==2 ){
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.VISIBLE

        }
        binding.edttabungoksigen.text = cekambulance.tabungOksigen.toString()
        binding.edttekananoksigen.text = cekambulance.toTekanan.toString()
        binding.edttapar.text = cekambulance.apar.toString()
        binding.edttekananapar.text = cekambulance.aTekanan.toString()
        binding.edtfisikapar.text = cekambulance.kondisiFisik.toString()
        binding.edtcairanInfus.text = cekambulance.cairanInfus.toString()
        binding.edtperlakAlas.text = cekambulance.perlak.toString()
        binding.edttanduDorong.text = cekambulance.tanduDorong.toString()
        binding.edtkebersihan.text = cekambulance.kebersihan.toString()
        binding.edtroda.text = cekambulance.roda.toString()
        binding.edtsponsKasur.text = cekambulance.sponsKasur.toString()
        binding.tanduLipat.text = cekambulance.tanduLipat.toString()
        binding.airWastafel.text = cekambulance.airWastafel.toString()
        binding.antiSepticGel.text = cekambulance.antisepticGel.toString()
        binding.tisu.text = cekambulance.tisuKering.toString()
        binding.oxycan.text = cekambulance.oxycan.toString()
        binding.tasP3k.text = cekambulance.tasP3k.toString()
        binding.txtketerangan.text = cekambulance.catatan.toString()


        binding.txttgl.text = cekambulance.tanggalCek.toString()
        binding.txtshift.text = cekambulance.shift.toString()

        binding.btnReturn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Mobil Ambulance")
            builder.setMessage("Return Mobil ambulance ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_ambulance(cekambulance.id!!).enqueue(object :
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
                                Snackbar.make(it, "return   gagal", Snackbar.LENGTH_SHORT)
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

        binding.btnApprove.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Mobil Ambulance")
            builder.setMessage("ACC Mobil Ambulance ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_ambulance(cekambulance.id!!).enqueue(object :
                    Callback<PostDataResponse> {
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

        binding.btnExport.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", cekambulance.id.toString())
            sheet.arguments = bundle
            sheet.show(supportFragmentManager,"BottomSheetExportAmbulanceFragment")
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