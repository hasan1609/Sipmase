package com.sipmase.sipmase.admin.edgblok2.activity

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
import com.sipmase.sipmase.databinding.ActivityDetailCekEdgBlok2Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.edgblok2.EdgBlok2Model
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCekEdgBlok2Activity : AppCompatActivity(), AnkoLogger {
    lateinit var cekedgblok2: EdgBlok2Model
    lateinit var binding: ActivityDetailCekEdgBlok2Binding

    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_edg_blok2)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekedgblok2 =
            gson.fromJson(intent.getStringExtra("cekedgblok2"), EdgBlok2Model::class.java)

        if (cekedgblok2.isStatus ==1){
            binding.lyBtn.visibility = View.VISIBLE
            binding.btnExport.visibility = View.GONE
        }

        if (cekedgblok2.isStatus ==3 || cekedgblok2.isStatus == 0){
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.GONE
        }

        if (cekedgblok2.isStatus ==2 ) {
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.VISIBLE

        }

        binding.wpsb.text = cekedgblok2.pWaktuPencatatanSesudahStart
        binding.wpss.text = cekedgblok2.pWaktuPencatatanSebelumStart

        binding.opsb.text = cekedgblok2.pOilPressureSebelumStart
        binding.opss.text = cekedgblok2.pOilPressureSesudahStart

        binding.fpsb.text = cekedgblok2.pFuelPressureSebelumStart
        binding.fpss.text = cekedgblok2.pFuelPressureSesudahStart

        binding.ftsb.text = cekedgblok2.pFuelTempratureSebelumStart
        binding.ftss.text = cekedgblok2.pFuelTempratureSesudahStart

        binding.cpsb.text = cekedgblok2.pCoolantPressureSebelumStart
        binding.cpss.text = cekedgblok2.pCoolantPressureSesudahStart

        binding.ctsb.text = cekedgblok2.pCoolantTempratureSebelumStart
        binding.ctss.text = cekedgblok2.pCoolantTempratureSesudahStart

        binding.ssb.text = cekedgblok2.pSpeedSebelumStart
        binding.sss.text = cekedgblok2.pSpeedSesudahStart

        binding.itsb.text = cekedgblok2.pInletTempratureSebelumStart
        binding.itss.text = cekedgblok2.pInletTempratureSesudahStart

        binding.tpsb.text = cekedgblok2.pTurboPleasureSebelumStart
        binding.tpss.text = cekedgblok2.pTurboPleasureSesudahStart

        binding.gbsb.text = cekedgblok2.pGeneratorBreakerSebelumStart
        binding.gbss.text = cekedgblok2.pGeneratorBreakerSesudahStart

        binding.gvsb.text = cekedgblok2.pGeneratorVoltageSebelumStart
        binding.gvss.text = cekedgblok2.pGeneratorVoltageSesudahStart

        binding.gfsb.text = cekedgblok2.pGeneratorFrequencySebelumStart
        binding.gfss.text = cekedgblok2.pGeneratorFrequencySesudahStart

        binding.gcsb.text = cekedgblok2.pGeneratorCurrentSebelumStart
        binding.gcss.text = cekedgblok2.pGeneratorCurrentSesudahStart

        binding.lsb.text = cekedgblok2.pLoadSebelumStart
        binding.lss.text = cekedgblok2.pLoadSesudahStart

        binding.bcvsb.text = cekedgblok2.pBatteryChargeVoltaseSebelumStart
        binding.bcvss.text = cekedgblok2.pBatteryChargeVoltaseSesudahStart

        binding.losb.text = cekedgblok2.pLubeOilLevelSebelumStart
        binding.loss.text = cekedgblok2.pLubeOilLevelSesudahStart

        binding.alsb.text = cekedgblok2.pAccuLevelSebelumStart
        binding.alss.text = cekedgblok2.pAccuLevelSesudahStart

        binding.rlsb.text = cekedgblok2.pRadiatorLevelSebelumStart
        binding.rlss.text = cekedgblok2.pRadiatorLevelSesudahStart

        binding.folsb.text = cekedgblok2.pFuelOilLevelSebelumStart
        binding.folss.text = cekedgblok2.pFuelOilLevelSesudahStart

        binding.ket.text = cekedgblok2.catatan

        binding.txtshift.text = cekedgblok2.shift
        binding.txttgl.text = cekedgblok2.tanggalCek

        binding.namak3.text = cekedgblok2.k3Nama
        binding.namaoperator.text = cekedgblok2.operatorNama
        binding.namaspv.text = cekedgblok2.supervisorNama
        Picasso.get().load("${Constant.foto_url+"foto-edgblok2/"}${cekedgblok2.operatorTtd}").into(binding.ttdoperator)
        Picasso.get().load("${Constant.foto_url+"foto-edgblok2/"}${cekedgblok2.k3Ttd}").into(binding.ttdk3)
        Picasso.get().load("${Constant.foto_url+"foto-edgblok2/"}${cekedgblok2.supervisorTtd}").into(binding.ttdspv)



        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Return Edg Blok 2")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_edgblok2(cekedgblok2.id!!).enqueue(object :
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
            builder.setTitle("Acc Edg Blok 2")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_edgblok2(cekedgblok2.id!!).enqueue(object :
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
        api.edgblok2_pdf(cekedgblok2.id!!).enqueue(object :
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
                    progressDialog.dismiss()
                    info { "dinda ${e.message}${response.code()} " }
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
