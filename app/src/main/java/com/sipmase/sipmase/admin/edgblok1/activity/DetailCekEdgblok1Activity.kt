package com.sipmase.sipmase.admin.edgblok1.activity

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
import com.sipmase.sipmase.databinding.ActivityDetailCekEdgblok1Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.edgblok1.EdgBlokModel
import com.sipmase.sipmase.pelaksana.edgblok1.CekEdgBlok1Activity
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCekEdgblok1Activity : AppCompatActivity(), AnkoLogger {
    lateinit var cekedgblok1: EdgBlokModel
    lateinit var binding: ActivityDetailCekEdgblok1Binding

    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_edgblok1)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekedgblok1 =
            gson.fromJson(intent.getStringExtra("cekedgblok1"), EdgBlokModel::class.java)

        if (cekedgblok1.isStatus ==1){
            binding.lyBtn.visibility = View.VISIBLE
            binding.btnExport.visibility = View.GONE
        }

        if (cekedgblok1.isStatus ==3 || cekedgblok1.isStatus == 0){
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.GONE
        }

        if (cekedgblok1.isStatus ==2 ) {
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.VISIBLE

        }

        binding.wpsb.text = cekedgblok1.pWaktuPencatatanSebelumStart
        binding.wpss.text = cekedgblok1.pWaktuPencatatanSesudahStart
        //oil preasure
        binding.opsb.text = cekedgblok1.pOilPressureSebelumStart
        binding.opss.text = cekedgblok1.pOilPressureSesudahStart
        //Oil temperature
        binding.otsb.text = cekedgblok1.pOilTempratureSebelumStart
        binding.otss.text = cekedgblok1.pOilTempratureSesudahStart
//            water temp
        binding.wtsb.text = cekedgblok1.pWaterTempratureSebelumStart
        binding.wtss.text = cekedgblok1.pWaterTempratureSesudahStart
//            speed
        binding.ssb.text = cekedgblok1.pSpeedSebelumStart
        binding.sss.text = cekedgblok1.pSpeedSesudahStart
//            hor meter
        binding.hm1sb.text = cekedgblok1.pHourMeter1SebelumStart
        binding.hm1ss.text = cekedgblok1.pHourMeter1SesudahStart
//            hour meter 2
        binding.hm2sb.text = cekedgblok1.pHourMeter2SebelumStart
        binding.hm2ss.text = cekedgblok1.pHourMeter2SesudahStart
//            main line vol
        binding.mlvsb.text = cekedgblok1.pMainLineVoltageSebelumStart
        binding.mlvss.text = cekedgblok1.pMainLineVoltageSesudahStart
//            main line freq
        binding.mlfsb.text = cekedgblok1.pMainLineFrequencySebelumStart
        binding.mlfss.text = cekedgblok1.pMainLineFrequencySesudahStart
//            gen break
        binding.gbsb.text = cekedgblok1.pGeneratorBreakerSebelumStart
        binding.gbss.text =  cekedgblok1.pGeneratorBreakerSebelumStart
//            gen vol
        binding.gvsb.text = cekedgblok1.pGeneratorVoltageSebelumStart
        binding.gvss.text = cekedgblok1.pGeneratorVoltageSesudahStart
//            gen freq
        binding.gfsb.text = cekedgblok1.pGeneratorFrequencySebelumStart
        binding.gfss.text = cekedgblok1.pGeneratorFrequencySesudahStart
//            load cur
        binding.lcsb.text = cekedgblok1.pLoadCurrentSebelumStart
        binding.lcss.text = cekedgblok1.pLoadCurrentSesudahStart
//            daya
        binding.dsb.text = cekedgblok1.pDayaSebelumStart
        binding.dss.text = cekedgblok1.pDayaSesudahStart
//            COS
        binding.csb.text = cekedgblok1.pCosSebelumStart
        binding.css.text = cekedgblok1.pCosSesudahStart
//            bat charg vol
        binding.bcvsb.text = cekedgblok1.pBatteryChargeVoltaseSebelumStart
        binding.bcvss.text = cekedgblok1.pBatteryChargeVoltaseSesudahStart
//            hour
        binding.hsb.text = cekedgblok1.pHourSebelumStart
        binding.hss.text = cekedgblok1.pHourSesudahStart
//            lube oil lvl
        binding.lolsb.text = cekedgblok1.pLubeOilLevelSebelumStart
        binding.lolss.text = cekedgblok1.pLubeOilLevelSesudahStart
//            accu lev
        binding.alsb.text = cekedgblok1.pAccuLevelSebelumStart
        binding.alss.text = cekedgblok1.pAccuLevelSebelumStart
//            radiator lev
        binding.rlsb.text = cekedgblok1.pRadiatorLevelSebelumStart
        binding.rlss.text = cekedgblok1.pRadiatorLevelSesudahStart
//            fuel oil level
        binding.folsb.text = cekedgblok1.pFuelOilLevelSebelumStart
        binding.folss.text = cekedgblok1.pFuelOilLevelSesudahStart

        binding.ket.text = cekedgblok1.catatan

        binding.txtshift.text = cekedgblok1.shift
        binding.txttgl.text = cekedgblok1.tanggalCek

        binding.namak3.text = cekedgblok1.k3Nama
        binding.namaoperator.text = cekedgblok1.operatorNama
        binding.namaspv.text = cekedgblok1.supervisorNama
        Picasso.get().load("${Constant.foto_url+"foto-edgblok1/"}${cekedgblok1.operatorTtd}").into(binding.ttdoperator)
        Picasso.get().load("${Constant.foto_url+"foto-edgblok1/"}${cekedgblok1.k3Ttd}").into(binding.ttdk3)
        Picasso.get().load("${Constant.foto_url+"foto-edgblok1/"}${cekedgblok1.supervisorTtd}").into(binding.ttdspv)


        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Return Edg Blok 1")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_edgblok1(cekedgblok1.id!!).enqueue(object :
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
                                Snackbar.make(it, "return gagal", Snackbar.LENGTH_SHORT)
                                    .show()

                            }


                        } catch (e: Exception) {
                            loading(false)
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
            builder.setTitle("Acc Edg Blok 1")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_edgblok1(cekedgblok1.id!!).enqueue(object :
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
                            loading(false)
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
            cetakpdf()
        }

    }

    fun cetakpdf(){
        loading(true)
        api.edgblok1_pdf(cekedgblok1.id!!).enqueue(object :
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
                    loading(false)
                    info { "dinda e ${e.message}" }
                    toast(e.message.toString())
                }
            }

            override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                info { "dinda failure ${t.message}" }
                loading(false)
                toast(t.message.toString())
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