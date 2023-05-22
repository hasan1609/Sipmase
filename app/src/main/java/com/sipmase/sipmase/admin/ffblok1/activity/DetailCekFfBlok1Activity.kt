package com.sipmase.sipmase.admin.ffblok1.activity

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
import com.sipmase.sipmase.databinding.ActivityDetailCekFfBlok1Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.ffblok.FFBlokModel
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCekFfBlok1Activity : AppCompatActivity(), AnkoLogger {
    lateinit var cekffblok1: FFBlokModel
    lateinit var binding: ActivityDetailCekFfBlok1Binding

    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_ff_blok1)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekffblok1 =
            gson.fromJson(intent.getStringExtra("cekffblok1"), FFBlokModel::class.java)

        if (cekffblok1.isStatus == 1) {
            binding.lyBtn.visibility = View.VISIBLE
            binding.btnExport.visibility = View.GONE
        }

        if (cekffblok1.isStatus == 3 || cekffblok1.isStatus == 0) {
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.GONE
        }

        if (cekffblok1.isStatus == 2) {
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.VISIBLE

        }

        //servicepump
        binding.spwpsb.text = cekffblok1.spWaktuPencatatanSebelumStart
        binding.spwpss.text = cekffblok1.spWaktuPencatatanSesudahStart

        binding.spspsb.text = cekffblok1.spSuctionPressSebelumStart
        binding.spspss.text = cekffblok1.spSuctionPressSesudahStart

        binding.spdpsb.text = cekffblok1.spDischargePressSebelumStart
        binding.spdpss.text = cekffblok1.spDischargePressSesudahStart

        binding.spflsb.text = cekffblok1.spFuelLevelSebelumStart
        binding.spflss.text = cekffblok1.spFuelLevelSesudahStart

        binding.spassb.text = cekffblok1.spAutoStartSebelumStart
        binding.spasss.text = cekffblok1.spAutoStartSesudahStart

        //================Motor Driven==============
        binding.mdwpsb.text = cekffblok1.mdWaktuPencatatanSebelumStart
        binding.mdwpss.text = cekffblok1.mdWaktuPencatatanSesudahStart
        //discharge press
        binding.mddpsb.text = cekffblok1.mdDischargePressSebelumStart
        binding.mddpss.text = cekffblok1.mdDischargePressSesudahStart
        //sunction press
        binding.mdspsb.text = cekffblok1.mdSuctionPressSebelumStart
        binding.mdspss.text = cekffblok1.mdSuctionPressSesudahStart
        //fuel level
        binding.mdflsb.text = cekffblok1.mdFullLevelSebelumStart
        binding.mdflss.text = cekffblok1.mdFullLevelSesudahStart
        //autostart
        binding.mdassb.text = cekffblok1.mdAutoStartSebelumStart
        binding.mdasss.text = cekffblok1.mdAutoStartSesudahStart

        //================Diesel  Engine==============
        //waktu start
        binding.dewpsb.text = cekffblok1.deWaktuPencatatanSebelumStart
        binding.dewpss.text = cekffblok1.deWaktuPencatatanSesudahStart
        //Lube Oil Press
        binding.delosb.text = cekffblok1.deLubeOilPressSebelumStart
        binding.deloss.text = cekffblok1.deLubeOilPressSesudahStart
        //Battery Voltage
        binding.debvsb.text = cekffblok1.deBatteryVoltageSebelumStart
        binding.debvss.text = cekffblok1.deBatteryVoltageSesudahStart
        //Battery AMpere
        binding.debasb.text = cekffblok1.deBatteryAmpereSebelumStart
        binding.debass.text = cekffblok1.deBatteryAmpereSesudahStart
        //Battery Level
        binding.deblsb.text = cekffblok1.deBatteryLevelSebelumStart
        binding.deblss.text = cekffblok1.deBatteryLevelSesudahStart
        //Fuel level
        binding.deflsb.text = cekffblok1.deFuelLevelSebelumStart
        binding.deflss.text = cekffblok1.deFuelLevelSesudahStart
        //Lube Oil Level
        binding.delolsb.text = cekffblok1.deLubeOilLevelSebelumStart
        binding.delolss.text = cekffblok1.deLubeOilLevelSesudahStart
        //Water COoler level
        binding.dewclsb.text = cekffblok1.deWaterCoolerLevelSebelumStart
        binding.dewclss.text = cekffblok1.deWaterCoolerLevelSesudahStart
        //Spped
        binding.dessb.text = cekffblok1.deSpeedSebelumStart
        binding.desss.text = cekffblok1.deSpeedSesudahStart
        //Sunction Press
        binding.despsb.text = cekffblok1.deSuctionPressSebelumStart
        binding.despss.text = cekffblok1.deSuctionPressSesudahStart
        //Discharge PRess
        binding.dedpsb.text = cekffblok1.deDischargePressSebelumStart
        binding.dedpss.text = cekffblok1.deDischargePressSesudahStart
        //Auto Start
        binding.deassb.text = cekffblok1.deAutoStartSebelumStart
        binding.deasss.text = cekffblok1.deAutoStartSesudahStart

        binding.ket.text = cekffblok1.catatan

        binding.txtshift.text = cekffblok1.shift
        binding.txttgl.text = cekffblok1.tanggalCek

        binding.namak3.text = cekffblok1.k3Nama
        binding.namaoperator.text = cekffblok1.operatorNama
        binding.namaspv.text = cekffblok1.supervisorNama
        Picasso.get().load("${Constant.foto_url+"foto-ffblok/"}${cekffblok1.operatorTtd}").into(binding.ttdoperator)
        Picasso.get().load("${Constant.foto_url+"foto-ffblok/"}${cekffblok1.k3Ttd}").into(binding.ttdk3)
        Picasso.get().load("${Constant.foto_url+"foto-ffblok/"}${cekffblok1.supervisorTtd}").into(binding.ttdspv)

        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Return FF Blok 1")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_ffblok(cekffblok1.id!!).enqueue(object :
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
                            info { "dinda e ${e.message}" }
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
            builder.setTitle("Acc Ff Blok ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_ffblok(cekffblok1.id!!).enqueue(object :
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
                            info { "dinda e ${e.message}" }
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
        api.ffblok_pdf(cekffblok1.id!!).enqueue(object :
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
