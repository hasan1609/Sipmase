package com.sipmase.sipmase.admin.edgblok3.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sipmase.sipmase.R
import com.sipmase.sipmase.constant.Constant
import com.sipmase.sipmase.databinding.ActivityDetailCekEdgBlok3Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.edgblok3.EdgBlok3Model
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCekEdgBlok3Activity : AppCompatActivity(), AnkoLogger {
    lateinit var cekedgblok3: EdgBlok3Model
    lateinit var binding: ActivityDetailCekEdgBlok3Binding

    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_edg_blok3)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekedgblok3 =
            gson.fromJson(intent.getStringExtra("cekedgblok3"), EdgBlok3Model::class.java)

        if (cekedgblok3.isStatus == 1) {
            binding.lyBtn.visibility = View.VISIBLE
            binding.btnExport.visibility = View.GONE
        }

        if (cekedgblok3.isStatus == 3 || cekedgblok3.isStatus == 0) {
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.GONE
        }

        if (cekedgblok3.isStatus == 2) {
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.VISIBLE

        }

        binding.opsb.text = cekedgblok3.oilPressSebelumStart
        binding.opss.text = cekedgblok3.oilPressSesudahStart

        binding.otsb.text = cekedgblok3.oilTempSebelumStart
        binding.otss.text = cekedgblok3.oilTempSesudahStart

        binding.ctsb.text = cekedgblok3.waterCoolTempSebelumStart
        binding.ctss.text = cekedgblok3.waterCoolTempSesudahStart

        binding.ssb.text = cekedgblok3.speedSebelumStart
        binding.sss.text = cekedgblok3.speedSesudahStart

        binding.hmsb.text = cekedgblok3.hourMeterSebelumStart
        binding.hmss.text = cekedgblok3.hourMeterSesudahStart

        binding.mlvsb.text = cekedgblok3.mlVoltageSebelumStart
        binding.mlvss.text = cekedgblok3.mlVoltageSesudahStart

        binding.mlfsb.text = cekedgblok3.mlFreqSebelumStart
        binding.mlfss.text = cekedgblok3.mlFreqSesudahStart

        binding.gbsb.text = cekedgblok3.genBreakSebelumStart
        binding.gbss.text = cekedgblok3.genBreakSesudahStart

        binding.gvsb.text = cekedgblok3.genVolSebelumStart
        binding.gvss.text = cekedgblok3.genVolSesudahStart

        binding.gfsb.text = cekedgblok3.genFreqSebelumStart
        binding.gfss.text = cekedgblok3.genFreqSesudahStart

        binding.lcsb.text = cekedgblok3.loadCurSebelumStart
        binding.lcss.text = cekedgblok3.loadCurSesudahStart

        binding.dsb.text = cekedgblok3.dayaSebelumStart
        binding.dss.text = cekedgblok3.dayaSesudahStart

        binding.csb.text = cekedgblok3.cosSebelumStart
        binding.css.text = cekedgblok3.cosSesudahStart

        binding.bcvsb.text = cekedgblok3.batChargeVolSebelumStart
        binding.bcvss.text = cekedgblok3.batChargeVolSesudahStart

        binding.hsb.text = cekedgblok3.hourSebelumStart
        binding.hss.text = cekedgblok3.hourSesudahStart

        binding.lolsb.text = cekedgblok3.lubeOilSebelumStart
        binding.lolss.text = cekedgblok3.lubeOilSesudahStart

        binding.alsb.text = cekedgblok3.accuSebelumStart
        binding.alss.text = cekedgblok3.accuSesudahStart

        binding.rlsb.text = cekedgblok3.radiatorSebelumStart
        binding.rlss.text = cekedgblok3.radiatorSesudahStart

        binding.folsb.text = cekedgblok3.fuelOilSebelumStart
        binding.folss.text = cekedgblok3.fuelOilSesudahStart

        binding.tbgsb.text = cekedgblok3.tempBearingGenSebelumStart
        binding.tbgss.text = cekedgblok3.tempBearingGenSesudahStart

        binding.twusb.text = cekedgblok3.tampWindingUSebelumStart
        binding.twuss.text = cekedgblok3.tampWindingUSesudahStart

        binding.twvsb.text = cekedgblok3.tempWindingVSebelumStart
        binding.twvss.text = cekedgblok3.tempWindingVSesudahStart

        binding.twwsb.text = cekedgblok3.tempWindingWSebelumStart
        binding.twwss.text = cekedgblok3.tempWindingWSesudahStart

        binding.brfsb.text = cekedgblok3.beltRadiatorFanSebelumStart
        binding.brfss.text = cekedgblok3.beltRadiatorFanSesudahStart


        binding.ket.text = cekedgblok3.catatan

        binding.txtshift.text = cekedgblok3.shift
        binding.txttgl.text = cekedgblok3.tanggalCek

        binding.namak3.text = cekedgblok3.k3Nama
        binding.namaoperator.text = cekedgblok3.operatorNama
        binding.namaspv.text = cekedgblok3.supervisorNama
        Picasso.get().load("${Constant.foto_url+"foto-edgblok3/"}${cekedgblok3.operatorTtd}").into(binding.ttdoperator)
        Picasso.get().load("${Constant.foto_url+"foto-edgblok3/"}${cekedgblok3.k3Ttd}").into(binding.ttdk3)
        Picasso.get().load("${Constant.foto_url+"foto-edgblok3/"}${cekedgblok3.supervisorTtd}").into(binding.ttdspv)
        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Return Edg Blok 3")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_edgblok3(cekedgblok3.id!!).enqueue(object :
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
                            loading(false)
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
            builder.setTitle("Acc Edg Blok 3")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_edgblok3(cekedgblok3.id!!).enqueue(object :
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
                            loading(false)
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
            cetakpdf()
        }

    }

    fun cetakpdf(){
        loading(true)
        api.edgblok3_pdf(cekedgblok3.id!!).enqueue(object :
            Callback<PostDataResponse> {
            override fun onResponse(
                call: Call<PostDataResponse>,
                response: Response<PostDataResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        loading(false)
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(response.body()!!.data.toString()))
                        startActivity(browserIntent)


                    }else{
                        val gson = Gson()
                        val type = object : TypeToken<PostDataResponse>() {}.type
                        var errorResponse: PostDataResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                        info { "dinda ${errorResponse}" }

                        loading(false)
                        toast("kesalahan response")
                    }

                }catch (e :Exception){
                    progressDialog.dismiss()
                    info { "dinda ${e.message}${response.code()} " }
                    loading(false)
                    toast(e.message.toString())
                }
            }

            override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                info { "dinda failure ${t.message}" }
                loading(false)
            }

        })
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