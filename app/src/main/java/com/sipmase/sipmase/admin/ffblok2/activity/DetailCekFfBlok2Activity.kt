package com.sipmase.sipmase.admin.ffblok2.activity

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
import com.sipmase.sipmase.databinding.ActivityDetailCekFfBlok2Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.ffblok2.FFBlok2Model
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCekFfBlok2Activity : AppCompatActivity(), AnkoLogger {
    lateinit var cekffblok2: FFBlok2Model
    lateinit var binding: ActivityDetailCekFfBlok2Binding

    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_ff_blok2)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekffblok2 =
            gson.fromJson(intent.getStringExtra("cekffblok2"), FFBlok2Model::class.java)

        if (cekffblok2.isStatus == 1) {
            binding.lyBtn.visibility = View.VISIBLE
            binding.btnExport.visibility = View.GONE
        }

        if (cekffblok2.isStatus == 3 || cekffblok2.isStatus == 0) {
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.GONE
        }

        if (cekffblok2.isStatus == 2) {
            binding.lyBtn.visibility = View.GONE
            binding.btnExport.visibility = View.VISIBLE

        }

        binding.ket.text = cekffblok2.catatan

        binding.txtshift.text = cekffblok2.shift
        binding.txttgl.text = cekffblok2.tanggalCek

        binding.namak3.text = cekffblok2.k3Nama
        binding.namaoperator.text = cekffblok2.operatorNama
        binding.namaspv.text = cekffblok2.supervisorNama
        Picasso.get().load("${Constant.foto_url+"foto-ffblok2/"}${cekffblok2.operatorTtd}").into(binding.ttdoperator)
        Picasso.get().load("${Constant.foto_url+"foto-ffblok2/"}${cekffblok2.k3Ttd}").into(binding.ttdk3)
        Picasso.get().load("${Constant.foto_url+"foto-ffblok2/"}${cekffblok2.supervisorTtd}").into(binding.ttdspv)

        binding.jpwpsb.text = cekffblok2.spWaktuPencatatanSebelumStart
        binding.jpwpss.text = cekffblok2.spWaktuPencatatanSesudahStart
        //Sunction Press
        binding.jpspsb.text = cekffblok2.spSuctionPressSebelumStart
        binding.jpspss.text = cekffblok2.spSuctionPressSesudahStart
        //Discharge Press
        binding.jpdpsb.text = cekffblok2.spDischargePressSebelumStart
        binding.jpdpss.text = cekffblok2.spDischargePressSesudahStart
        //Auto Start
        binding.jpassb.text = cekffblok2.spAutoStartSebelumStart
        binding.jpassb.text = cekffblok2.spAutoStartSesudahStart
        //Auto Stop
        binding.jpastsb.text = cekffblok2.spAutoStopSebelumStart
        binding.jpastss.text = cekffblok2.spAutoStopSesudahStart
        //=================Motor Driven===================
        //waktu pencatatan
        binding.mdwpsb.text = cekffblok2.mdWaktuPencatatanSebelumStart
        binding.mdwpss.text = cekffblok2.mdWaktuPencatatanSesudahStart
        //sunction press
        binding.mdspsb.text = cekffblok2.mdSuctionPressSebelumStart
        binding.mdspss.text = cekffblok2.mdSuctionPressSesudahStart
        //discharge press
        binding.mddpsb.text = cekffblok2.mdDischargePressSebelumStart
        binding.mddpss.text = cekffblok2.mdDischargePressSesudahStart
        //Fuel Level
        binding.mdflsb.text = cekffblok2.mdFullLevelSebelumStart
        binding.mdflss.text = cekffblok2.mdFullLevelSesudahStart
        //Auto Start
        binding.mdassb.text = cekffblok2.mdAutoStartSebelumStart
        binding.mdasss.text = cekffblok2.mdAutoStartSesudahStart
        //=================Diesel Engine===================
        //waktu pencatatan
        binding.dewpsb.text = cekffblok2.deWaktuPencatatanSebelumStart
        binding.dewpss.text = cekffblok2.deWaktuPencatatanSesudahStart
        //Lube Oil Press
        binding.delosb.text = cekffblok2.deLubeOilPressSebelumStart
        binding.deloss.text = cekffblok2.deLubeOilPressSesudahStart
        //Battery VOltage
        binding.debvsb.text = cekffblok2.deBatteryVoltageSebelumStart
        binding.debvss.text = cekffblok2.deBatteryVoltageSesudahStart
        //Battery Ampere
        binding.debasb.text = cekffblok2.deBatteryAmpereSebelumStart
        binding.debass.text = cekffblok2.deBatteryAmpereSesudahStart
        //Battery Level
        binding.deblsb.text = cekffblok2.deBatteryLevelSebelumStart
        binding.deblss.text = cekffblok2.deBatteryLevelSesudahStart
        //Fuel level
        binding.deflsb.text = cekffblok2.deFuelLevelSebelumStart
        binding.deflss.text = cekffblok2.deFuelLevelSesudahStart
        //Lube Oil Level
        binding.delolsb.text = cekffblok2.deLubeOilLevelSebelumStart
        binding.delolss.text = cekffblok2.deLubeOilLevelSesudahStart
        //Water COoler level
        binding.dewclsb.text = cekffblok2.deWaterCoolerLevelSebelumStart
        binding.dewclss.text = cekffblok2.deWaterCoolerLevelSesudahStart
        //Speed
        binding.dessb.text = cekffblok2.deSpeedSebelumStart
        binding.desss.text = cekffblok2.deSpeedSesudahStart

        //Sunction Press
        binding.despsb.text = cekffblok2.deSuctionPressSebelumStart
        binding.despss.text = cekffblok2.deSuctionPressSesudahStart
        //Discharge Press
        binding.dedpsb.text = cekffblok2.deDischargePressSebelumStart
        binding.dedpss.text = cekffblok2.deDischargePressSesudahStart
        //Auto Start
        binding.deassb.text = cekffblok2.deAutoStartSebelumStart
        binding.deasss.text = cekffblok2.deAutoStartSesudahStart

        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Return FF Blok 2")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_ffblok2(cekffblok2.id!!).enqueue(object :
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
            builder.setTitle("Acc Ff Blok 2")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_ffblok2(cekffblok2.id!!).enqueue(object :
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
        api.ffblok2_pdf(cekffblok2.id!!).enqueue(object :
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
                toast("Kesalahan Jaringan")
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