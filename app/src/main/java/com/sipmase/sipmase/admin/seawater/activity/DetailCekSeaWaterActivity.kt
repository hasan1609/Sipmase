package com.sipmase.sipmase.admin.seawater.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sipmase.sipmase.R
import com.sipmase.sipmase.constant.Constant
import com.sipmase.sipmase.databinding.ActivityDetailCekSeaWaterBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.seawater.SeaWaterModel
import com.sipmase.sipmase.pelaksana.seawater.CekSeaWaterActivity
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCekSeaWaterActivity : AppCompatActivity(), AnkoLogger {
    lateinit var cekseawater: SeaWaterModel
    lateinit var binding: ActivityDetailCekSeaWaterBinding

    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_sea_water)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekseawater =
            gson.fromJson(intent.getStringExtra("cekseawater"), SeaWaterModel::class.java)

        if (cekseawater.isStatus == 1) {
            binding.lyBtn.visibility = View.VISIBLE
            binding.btnExport.visibility = View.GONE
        }

        if (cekseawater.isStatus == 3 || cekseawater.isStatus == 0) {
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.GONE
        }

        if (cekseawater.isStatus == 2) {
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.VISIBLE

        }

        binding.mdwpsb.text = cekseawater.mdWaktuPencatatanSebelumStart
        binding.mdwpss.text = cekseawater.mdWaktuPencatatanSesudahStart
        binding.mddpsb.text = cekseawater.mdDischargePressSebelumStart
        binding.mddpss.text = cekseawater.mdDischargePressSesudahStart
        binding.mdspsb.text = cekseawater.mdSuctionPressSebelumStart
        binding.mdspss.text = cekseawater.mdSuctionPressSesudahStart
        binding.mdlolsb.text = cekseawater.mdLubeOilLevelSebelumStart
        binding.mdlolss.text = cekseawater.mdLubeOilLevelSesudahStart


        binding.dewpsb.text = cekseawater.deWaktuPencatatanSebelumStart
        binding.dewpss.text = cekseawater.deWaktuPencatatanSesudahStart
        binding.deeohsb.text = cekseawater.deEngineOperatingHoursSebelumStart
        binding.deeohss.text = cekseawater.deEngineOperatingHoursSesudahStart
        binding.debisb.text = cekseawater.deBatteryIIISebelumStart
        binding.debiss.text = cekseawater.deBatteryIIISesudahStart
        binding.debiisb.text = cekseawater.deBatteryIII2SebelumStart
        binding.debiiss.text = cekseawater.deBatteryIII2SesudahStart
        binding.deoldsb.text = cekseawater.deOilLevelDipstickSebelumStart
        binding.deoldss.text = cekseawater.deOilLevelDipstickSesudahStart
        binding.deflsb.text = cekseawater.deFuelLevelSebelumStart
        binding.deflss.text = cekseawater.deFuelLevelSesudahStart
        binding.deeclsb.text = cekseawater.deEngineCoolantLevelSebelumStart
        binding.deeclss.text = cekseawater.deEngineCoolantLevelSesudahStart
        binding.deopsb.text = cekseawater.deOilPressureSebelumStart
        binding.deopss.text = cekseawater.deOilPressureSesudahStart
        binding.deafsb.text = cekseawater.deAirFilterSebelumStart
        binding.deafss.text = cekseawater.deAirFilterSesudahStart
        binding.deessb.text = cekseawater.deExthoustSystemSebelumStart
        binding.deesss.text = cekseawater.deExthoustSystemSesudahStart
        binding.decwpsb.text = cekseawater.deCoolingWaterPressSebelumStart
        binding.decwpss.text = cekseawater.deCoolingWaterPressSesudahStart
        binding.dectsb.text = cekseawater.deCoolantTemperatureSebelumStart
        binding.dectss.text = cekseawater.deCoolantTemperatureSesudahStart
        binding.dessb.text = cekseawater.deSpeedSebelumStart
        binding.desss.text = cekseawater.deSpeedSesudahStart
        binding.despsb.text = cekseawater.deSuctionPressSebelumStart
        binding.despss.text = cekseawater.deSuctionPressSesudahStart
        binding.dedpsb.text = cekseawater.deDischargePressSebelumStart
        binding.dedpss.text = cekseawater.deDischargePressSesudahStart
        binding.deftsb.text = cekseawater.deFuelTemperatureSebelumStart
        binding.deftss.text = cekseawater.deFuelTemperatureSesudahStart
        binding.deetsb.text = cekseawater.deEngineTorqueSebelumStart
        binding.deetss.text = cekseawater.deEngineTorqueSesudahStart
        binding.deplsb.text = cekseawater.dePersenLoadSebelumStart
        binding.deplss.text = cekseawater.dePersenLoadSesudahStart
        binding.defswsb.text = cekseawater.deFlowSeaWaterSebelumStart
        binding.defswss.text = cekseawater.deFlowSeaWaterSesudahStart
        binding.depbpsb.text = cekseawater.dePressByPassSebelumStart
        binding.depbpss.text = cekseawater.dePressByPassSesudahStart

        binding.ket.text = cekseawater.catatan

        binding.txtshift.text = cekseawater.shift
        binding.txttgl.text = cekseawater.tanggalCek

        binding.namak3.text = cekseawater.k3Nama
        binding.namaoperator.text = cekseawater.operatorNama
        binding.namaspv.text = cekseawater.supervisorNama
        Picasso.get().load("${Constant.foto_url+"foto-seawater/"}${cekseawater.operatorTtd}").into(binding.ttdoperator)
        Picasso.get().load("${Constant.foto_url+"foto-seawater/"}${cekseawater.k3Ttd}").into(binding.ttdk3)
        Picasso.get().load("${Constant.foto_url+"foto-seawater/"}${cekseawater.supervisorTtd}").into(binding.ttdspv)

        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Return seawater ?")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_seawater(cekseawater.id!!).enqueue(object :
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
                            loading(false)
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
            builder.setTitle("Acc Seawater")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_seawater(cekseawater.id!!).enqueue(object :
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
                            loading(false)
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
        api.seawater_pdf(cekseawater.id!!).enqueue(object :
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
                    loading(false)
                    info { "dinda ${e.message}${response.code()} " }
                    toast(e.message.toString())
                }
            }

            override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                info { "dinda failure ${t.message}" }
                loading(false)
                toast("Kesalahan Jaringan")
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