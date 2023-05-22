package com.sipmase.sipmase.admin.damkar.activity

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
import com.sipmase.sipmase.admin.damkar.fragment.BottomSheetExportDamkarFragment
import com.sipmase.sipmase.databinding.ActivityDetailCekDamkarBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.damkar.DamkarModel
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

class DetailCekDamkarActivity : AppCompatActivity(), AnkoLogger {
    lateinit var cekdamkar: DamkarModel
    lateinit var binding: ActivityDetailCekDamkarBinding

    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()
    val sheet = BottomSheetExportDamkarFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_damkar)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekdamkar =
            gson.fromJson(intent.getStringExtra("damkar"), DamkarModel::class.java)

        if (cekdamkar.isStatus ==1){
            binding.lyBtn.visibility = View.VISIBLE
            binding.btnExport.visibility = View.GONE
        }

        if (cekdamkar.isStatus ==3 || cekdamkar.isStatus == 0){
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.GONE
        }

        if (cekdamkar.isStatus ==2 ){
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.VISIBLE

        }
        //================Motor Driven==============
        binding.star.text = cekdamkar.start.toString()
        binding.stop.text = cekdamkar.stop.toString()
        binding.airaccu.text = cekdamkar.airAccu.toString()
        binding.lvlAirRadiator.text = cekdamkar.levelAirRadiator.toString()
        binding.tempMesin.text = cekdamkar.tempraturMesin.toString()
        binding.lvlOil.text = cekdamkar.levelOil.toString()
        binding.filterSolar.text = cekdamkar.filterSolar.toString()
        binding.lvlMinyakRem.text = cekdamkar.levelMinyakRem.toString()
        binding.suaraMesin.text = cekdamkar.suaraMesin.toString()
        binding.lampuDepan.text = cekdamkar.lampuDepan.toString()
        binding.lampuBelakang.text = cekdamkar.lampuBelakang.toString()
        binding.lampuRem.text = cekdamkar.lampuRem.toString()
        binding.kananDpn.text = cekdamkar.lampuSeinKananDepan.toString()
        binding.kiriDepan.text = cekdamkar.lampuSeinKiriDepan.toString()
        binding.kananBelakang.text = cekdamkar.lampuSeinKananBelakang.toString()
        binding.kiriBelakang.text = cekdamkar.lampuSeinKiriBelakang.toString()
        binding.hazard.text = cekdamkar.lampuHazard.toString()
        binding.sorot.text = cekdamkar.lampuSorot.toString()
        binding.dalamDepan.text = cekdamkar.lampuDalamDepan.toString()
        binding.dalamTengah.text = cekdamkar.lampuDalamTengah.toString()
        binding.dalamBelakang.text = cekdamkar.lampuDalamBelakang.toString()
        binding.wiper.text = cekdamkar.wiper.toString()
        binding.spion.text = cekdamkar.spion.toString()
        binding.sirine.text = cekdamkar.sirine.toString()
        binding.txtketerangan.text = cekdamkar.catatan.toString()



        binding.txttgl.text = cekdamkar.tanggalCek.toString()
        binding.txtshift.text = cekdamkar.shift.toString()

        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Mobil Damkar")
            builder.setMessage("Return Mobil Damkar ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_damkar(cekdamkar.id!!).enqueue(object :
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
                            toast(e.message.toString())
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
            builder.setTitle("Mobil Damkar")
            builder.setMessage("ACC Mobil Damkar ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_damkar(cekdamkar.id!!).enqueue(object :
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
                            toast(e.message.toString())
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
            bundle.putString("id", cekdamkar.id.toString())
            sheet.arguments = bundle
            sheet.show(supportFragmentManager,"BottomSheetExportDamkarFragment")
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