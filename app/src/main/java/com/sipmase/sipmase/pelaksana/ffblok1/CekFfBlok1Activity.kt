package com.sipmase.sipmase.pelaksana.ffblok1

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
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sipmase.sipmase.R
import com.sipmase.sipmase.constant.Constant
import com.sipmase.sipmase.databinding.ActivityCekFfBlok1Binding
import com.sipmase.sipmase.databinding.FragmentTtdBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.ffblok.FFBlokModel
import com.sipmase.sipmase.session.SessionManager
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
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
import java.text.SimpleDateFormat
import java.util.*

class CekFfBlok1Activity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekFfBlok1Binding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog

    val sheet= TtdFfBlok1Fragment()

    companion object {
        var cekffblok1: FFBlokModel? = null
    }

    var currentDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_ff_blok1)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekffblok1 =
            gson.fromJson(
                intent.getStringExtra("cekffblok1"),
                FFBlokModel::class.java
            )

        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = currentDate

        binding.namak3.text = cekffblok1!!.k3Nama
        binding.namaoperator.text = cekffblok1!!.operatorNama
        binding.namaspv.text = cekffblok1!!.supervisorNama
        if(cekffblok1!!.supervisorTtd != null){
            binding.btnSpv.visibility = View.GONE
            binding.ttdspv.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-ffblok/"}${cekffblok1!!.supervisorTtd}").into(binding.ttdspv)
        }else{
            binding.btnSpv.visibility = View.VISIBLE
            binding.ttdspv.visibility = View.GONE
            binding.btnSpv.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "spv")
                bundle.putString("id", cekffblok1!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdFfBlok1Fragment")
            }
        }
        if(cekffblok1!!.k3Ttd != null){
            binding.btnK3.visibility = View.GONE
            binding.ttdk3.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-ffblok/"}${cekffblok1!!.k3Ttd}").into(binding.ttdk3)
        }else{
            binding.btnK3.visibility = View.VISIBLE
            binding.ttdk3.visibility = View.GONE
            binding.btnK3.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "k3")
                bundle.putString("id", cekffblok1!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdFfBlok1Fragment")
            }
        }
        if(cekffblok1!!.operatorTtd != null){
            binding.btnOperator.visibility = View.GONE
            binding.ttdoperator.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-ffblok/"}${cekffblok1!!.operatorTtd}").into(binding.ttdoperator)
        }else{
            binding.btnOperator.visibility = View.VISIBLE
            binding.ttdoperator.visibility = View.GONE
            binding.btnOperator.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "operator")
                bundle.putString("id", cekffblok1!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdFfBlok1Fragment")
            }
        }


        if(cekffblok1!!.isStatus == 1){
            binding.btnDraft.visibility = View.GONE
            binding.btnSubmit.visibility = View.GONE
        }else if(cekffblok1!!.isStatus == 3){
            binding.btnDraft.visibility = View.GONE
        }
        if(cekffblok1!!.shift != null){
            binding.txtshift.text = cekffblok1!!.shift
        }else{
            binding.txtshift.text = sessionManager.getNama()
        }

//        sp waktu pencatatan
        binding.spwpsb.setText(cekffblok1!!.spWaktuPencatatanSebelumStart)
        binding.spwpss.setText(cekffblok1!!.spWaktuPencatatanSesudahStart)
//        sp suction press
        binding.spspsb.setText(cekffblok1!!.spSuctionPressSebelumStart)
        binding.spspss.setText(cekffblok1!!.spSuctionPressSesudahStart)
//        sp discharge press
        binding.spdpsb.setText(cekffblok1!!.spDischargePressSebelumStart)
        binding.spdpss.setText(cekffblok1!!.spDischargePressSesudahStart)
//        sp ful lvl
        binding.spflsb.setText(cekffblok1!!.spFuelLevelSebelumStart)
        binding.spflss.setText(cekffblok1!!.spFuelLevelSesudahStart)
//        sp auto start
        binding.spassb.setText(cekffblok1!!.spAutoStartSebelumStart)
        binding.spasss.setText(cekffblok1!!.spAutoStartSesudahStart)
//        md waktu pencatatan
        binding.mdwpsb.setText(cekffblok1!!.mdWaktuPencatatanSebelumStart)
        binding.mdwpss.setText(cekffblok1!!.mdWaktuPencatatanSesudahStart)
//        md suction press
        binding.mdspsb.setText(cekffblok1!!.mdSuctionPressSebelumStart)
        binding.mdspss.setText(cekffblok1!!.mdSuctionPressSesudahStart)
//        md dischaarge press
        binding.mddpsb.setText(cekffblok1!!.mdDischargePressSebelumStart)
        binding.mddpss.setText(cekffblok1!!.mdDischargePressSesudahStart)
//        md ful lvl
        binding.mdflsb.setText(cekffblok1!!.mdFullLevelSebelumStart)
        binding.mdflss.setText(cekffblok1!!.mdFullLevelSesudahStart)
//        md auto start
        binding.mdassb.setText(cekffblok1!!.mdAutoStartSebelumStart)
        binding.mdasss.setText(cekffblok1!!.mdAutoStartSesudahStart)
//        de waktu pencatatan
        binding.dewpsb.setText(cekffblok1!!.deWaktuPencatatanSebelumStart)
        binding.dewpss.setText(cekffblok1!!.deWaktuPencatatanSesudahStart)
//        de lube oil pres
        binding.delosb.setText(cekffblok1!!.deLubeOilPressSebelumStart)
        binding.deloss.setText(cekffblok1!!.deLubeOilPressSesudahStart)
//        de batry vilt
        binding.debvsb.setText(cekffblok1!!.deBatteryVoltageSebelumStart)
        binding.debvss.setText(cekffblok1!!.deBatteryVoltageSesudahStart)
//        de batry ampre
        binding.debasb.setText(cekffblok1!!.deBatteryAmpereSebelumStart)
        binding.debass.setText(cekffblok1!!.deBatteryAmpereSesudahStart)
//        de btry lvl
        binding.deblsb.setText(cekffblok1!!.deBatteryLevelSebelumStart)
        binding.deblss.setText(cekffblok1!!.deBatteryLevelSesudahStart)
//        de ful lvl
        binding.deflsb.setText(cekffblok1!!.deFuelLevelSebelumStart)
        binding.deflss.setText(cekffblok1!!.deFuelLevelSesudahStart)
//        de lube oil lvl
        binding.delolsb.setText(cekffblok1!!.deLubeOilLevelSebelumStart)
        binding.delolss.setText(cekffblok1!!.deLubeOilLevelSesudahStart)
//        de watr cooler lvl
        binding.dewclsb.setText(cekffblok1!!.deWaterCoolerLevelSebelumStart)
        binding.dewclss.setText(cekffblok1!!.deWaterCoolerLevelSesudahStart)
//        de speed
        binding.dessb.setText(cekffblok1!!.deSpeedSebelumStart)
        binding.desss.setText(cekffblok1!!.deSpeedSesudahStart)
//        de suction prees
        binding.despsb.setText(cekffblok1!!.deSuctionPressSebelumStart)
        binding.despss.setText(cekffblok1!!.deSuctionPressSesudahStart)
//        de dischrg press
        binding.dedpsb.setText(cekffblok1!!.deDischargePressSebelumStart)
        binding.dedpss.setText(cekffblok1!!.deDischargePressSesudahStart)
//        de auto start
        binding.deassb.setText(cekffblok1!!.deAutoStartSebelumStart)
        binding.deasss.setText(cekffblok1!!.deAutoStartSesudahStart)
//        ket
        binding.ket.setText(cekffblok1!!.catatan)


        binding.btnDraft.setOnClickListener {
            val body_tw: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekffblok1!!.tw.toString()
            )

            val shift = binding.txtshift.text.toString()
            val body_shift: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                shift
            )

            val body_is_status: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), "0"
            )

            val body_tahun: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekffblok1!!.tahun
            )

            val body_id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekffblok1!!.id}"
            )

            val sdf = SimpleDateFormat("yyyy-M-dd ")
            val tanggal_cek = sdf.format(Date())

            val body_tanggal_cek: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${tanggal_cek}"
            )
            //=================Service Pump===================
            //waktu pencatatan
            val txtspWpSbs = binding.spwpsb.text.toString()
            val txtspWpSds = binding.spwpss.text.toString()
            val body_txtspWpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspWpSbs
            )
            val body_txtspWpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspWpSds
            )
            //Sunction Press
            val txtspSpSbs = binding.spspsb.text.toString()
            val txtspSpSds = binding.spspss.text.toString()
            val body_txtspSpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspSpSbs
            )
            val body_txtspSpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspSpSds
            )
            //Discharge Press
            val txtspDpSbs = binding.spdpsb.text.toString()
            val txtspDpSds = binding.spdpss.text.toString()
            val body_txtspDpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspDpSbs
            )
            val body_txtspDpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspDpSds
            )
            //Fuel Level
            val txtspFlSbs = binding.spflsb.text.toString()
            val txtspFlSds = binding.spflss.text.toString()
            val body_txtspFlSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspFlSbs
            )
            val body_txtspFlSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspFlSds
            )
            //Auto Start
            val txtspAsSbs = binding.spassb.text.toString()
            val txtspAsSds = binding.spasss.text.toString()
            val body_txtspAsSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspAsSbs
            )
            val body_txtspAsSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspAsSds
            )
            //=================Motor Driven===================
            //waktu pencatatan
            val txtmdWpSbs = binding.mdwpsb.text.toString()
            val txtmdWpSds = binding.mdwpss.text.toString()
            val body_txtmdWpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdWpSbs
            )
            val body_txtmdWpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdWpSds
            )
            //sunction press
            val txtmdSpSbs = binding.mdspsb.text.toString()
            val txtmdSpSds = binding.mdspss.text.toString()
            val body_txtmdSpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdSpSbs
            )
            val body_txtmdSpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdSpSds
            )
            //discharge press
            val txtmdDpSbs = binding.mddpsb.text.toString()
            val txtmdDpSds = binding.mddpss.text.toString()
            val body_txtmdDpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdDpSbs
            )
            val body_txtmdDpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdDpSds
            )
            //Fuel Level
            val txtmdFlSbs = binding.mdflsb.text.toString()
            val txtmdFlSds = binding.mdflss.text.toString()
            val body_txtmdFlSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdFlSbs
            )
            val body_txtmdFlSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdFlSds
            )
            //Auto Start
            val txtmdAsSbs = binding.mdassb.text.toString()
            val txtmdAsSds = binding.mdasss.text.toString()
            val body_txtmdAsSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdAsSbs
            )
            val body_txtmdAsSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdAsSds
            )
            //=================Diesel Engine===================
            //waktu pencatatan
            val de_waktu_pencatatan_sebelum_start = binding.dewpsb.text.toString().trim()
            val de_waktu_pencatatan_sesudah_start = binding.dewpss.text.toString().trim()
            val body_de_waktu_pencatatan_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_waktu_pencatatan_sebelum_start
            )
            val body_de_waktu_pencatatan_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_waktu_pencatatan_sesudah_start
            )
            //Lube Oil Press
            val txtdeLubeOilPressSbs = binding.delosb.text.toString().trim()
            val txtdeLubeOilPressSds = binding.deloss.text.toString().trim()
            val body_txtdeLubeOilPressSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilPressSbs
            )
            val body_txtdeLubeOilPressSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilPressSds
            )
            //Battery VOltage
            val de_battery_voltage_sebelum_start =
                binding.debvsb.text.toString().trim()
            val de_battery_voltage_sesudah_start =
                binding.debvss.text.toString().trim()
            val body_de_battery_voltage_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_voltage_sebelum_start
            )
            val body_de_battery_voltage_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_voltage_sesudah_start
            )
            //Battery Ampere
            val de_battery_ampere_sebelum_start =
                binding.debasb.text.toString().trim()
            val de_battery_ampere_sesudah_start =
                binding.debass.text.toString().trim()
            val body_de_battery_ampere_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_ampere_sebelum_start
            )
            val body_de_battery_ampere_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_ampere_sesudah_start
            )
            //Battery Level
            val txtdeBatteryLevelSebelumStart =
                binding.deblsb.text.toString().trim()
            val txtdeBatteryLevelSesudahStart =
                binding.deblss.text.toString().trim()
            val body_txtdeBatteryLevelSebelumStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeBatteryLevelSebelumStart
            )
            val body_txtdeBatteryLevelSesudahStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeBatteryLevelSesudahStart
            )
            //Fuel Level
            val txtdeFuelLevelSebelumStart =
                binding.deflsb.text.toString().trim()
            val txtdeFuelLevelSesudahStart =
                binding.deflss.text.toString().trim()
            val body_txtdeFuelLevelSebelumStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeFuelLevelSebelumStart
            )
            val body_txtdeFuelLevelSesudahStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeFuelLevelSesudahStart
            )
            //Lube Oil Level
            val txtdeLubeOilLevelSbs =
                binding.delolsb.text.toString().trim()
            val txtdeLubeOilLevelSds =
                binding.delolss.text.toString().trim()
            val body_txtdeLubeOilLevelSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilLevelSbs
            )
            val body_txtdeLubeOilLevelSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilLevelSds
            )
            //Water Cooler level
            val txtdeWaterCoolerLevelSbs =
                binding.dewclsb.text.toString().trim()
            val txtdeWaterCoolerLevelSds =
                binding.dewclss.text.toString().trim()
            val body_txtdeWaterCoolerLevelSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeWaterCoolerLevelSbs
            )
            val body_txtdeWaterCoolerLevelSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeWaterCoolerLevelSds
            )
            //Speed
            val txtdeSpeedSbs =
                binding.dessb.text.toString().trim()
            val txtdeSpeedSds =
                binding.desss.text.toString().trim()
            val body_txtdeSpeedSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeSpeedSbs
            )
            val body_txtdeSpeedSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeSpeedSds
            )
            //Sunction Press
            val de_suction_press_sebelum_start =
                binding.despsb.text.toString().trim()
            val de_suction_press_sesudah_start =
                binding.despss.text.toString().trim()
            val body_de_suction_press_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_suction_press_sebelum_start
            )
            val body_de_suction_press_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_suction_press_sesudah_start
            )
            //Discharge Press
            val de_discharge_press_sebelum_start =
                binding.dedpsb.text.toString().trim()
            val de_discharge_press_sesudah_start =
                binding.dedpss.text.toString().trim()
            val body_de_discharge_press_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_discharge_press_sebelum_start
            )
            val body_de_discharge_press_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_discharge_press_sesudah_start
            )
            //Auto Start
            val txtdeAutoStartSbs =
                binding.deassb.text.toString().trim()
            val txtdeAutoStartSds =
                binding.deasss.text.toString().trim()
            val body_txtdeAutoStartSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeAutoStartSbs
            )
            val body_txtdeAutoStartSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeAutoStartSds
            )

            val catatan = binding.ket.text.toString().trim()
            val body_catatan: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                catatan
            )

                loading(true)

                api.update_ffblok(
                    body_id,
                    body_tw,
                    body_tahun,
                    body_tanggal_cek,
                    body_shift,
                    body_is_status,
                    body_txtspWpSbs,
                    body_txtspWpSds,
                    body_txtspSpSbs,
                    body_txtspSpSds,
                    body_txtspDpSbs,
                    body_txtspDpSds,
                    body_txtspFlSbs,
                    body_txtspFlSds,
                    body_txtspAsSbs,
                    body_txtspAsSds,
                    body_txtmdWpSbs,
                    body_txtmdWpSds,
                    body_txtmdSpSbs,
                    body_txtmdSpSds,
                    body_txtmdDpSbs,
                    body_txtmdDpSds,
                    body_txtmdFlSbs,
                    body_txtmdFlSds,
                    body_txtmdAsSbs,
                    body_txtmdAsSds,
                    body_de_waktu_pencatatan_sebelum_start,
                    body_de_waktu_pencatatan_sesudah_start,
                    body_txtdeLubeOilPressSbs,
                    body_txtdeLubeOilPressSds,
                    body_de_battery_voltage_sebelum_start,
                    body_de_battery_voltage_sesudah_start,
                    body_de_battery_ampere_sebelum_start,
                    body_de_battery_ampere_sesudah_start,
                    body_txtdeBatteryLevelSebelumStart,
                    body_txtdeBatteryLevelSesudahStart,
                    body_txtdeFuelLevelSebelumStart,
                    body_txtdeFuelLevelSesudahStart,
                    body_txtdeLubeOilLevelSbs,
                    body_txtdeLubeOilLevelSds,
                    body_txtdeWaterCoolerLevelSbs,
                    body_txtdeWaterCoolerLevelSds,
                    body_txtdeSpeedSbs,
                    body_txtdeSpeedSds,
                    body_de_suction_press_sebelum_start,
                    body_de_suction_press_sesudah_start,
                    body_de_discharge_press_sebelum_start,
                    body_de_discharge_press_sesudah_start,
                    body_txtdeAutoStartSbs,
                    body_txtdeAutoStartSds,
                    body_catatan,
                ).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.sukses == 1) {
                                finish()
                                toast("Berhasil")
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
        binding.btnSubmit.setOnClickListener {
            val body_tw: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekffblok1!!.tw.toString()
            )

            val shift = binding.txtshift.text.toString()
            val body_shift: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                shift
            )

            val body_is_status: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), "1"
            )

            val body_tahun: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekffblok1!!.tahun
            )

            val body_id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekffblok1!!.id}"
            )

            val sdf = SimpleDateFormat("yyyy-M-dd ")
            val tanggal_cek = sdf.format(Date())

            val body_tanggal_cek: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${tanggal_cek}"
            )
            //=================Service Pump===================
            //waktu pencatatan
            val txtspWpSbs = binding.spwpsb.text.toString()
            val txtspWpSds = binding.spwpss.text.toString()
            val body_txtspWpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspWpSbs
            )
            val body_txtspWpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspWpSds
            )
            //Sunction Press
            val txtspSpSbs = binding.spspsb.text.toString()
            val txtspSpSds = binding.spspss.text.toString()
            val body_txtspSpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspSpSbs
            )
            val body_txtspSpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspSpSds
            )
            //Discharge Press
            val txtspDpSbs = binding.spdpsb.text.toString()
            val txtspDpSds = binding.spdpss.text.toString()
            val body_txtspDpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspDpSbs
            )
            val body_txtspDpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspDpSds
            )
            //Fuel Level
            val txtspFlSbs = binding.spflsb.text.toString()
            val txtspFlSds = binding.spflss.text.toString()
            val body_txtspFlSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspFlSbs
            )
            val body_txtspFlSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspFlSds
            )
            //Auto Start
            val txtspAsSbs = binding.spassb.text.toString()
            val txtspAsSds = binding.spasss.text.toString()
            val body_txtspAsSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspAsSbs
            )
            val body_txtspAsSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspAsSds
            )
            //=================Motor Driven===================
            //waktu pencatatan
            val txtmdWpSbs = binding.mdwpsb.text.toString()
            val txtmdWpSds = binding.mdwpss.text.toString()
            val body_txtmdWpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdWpSbs
            )
            val body_txtmdWpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdWpSds
            )
            //sunction press
            val txtmdSpSbs = binding.mdspsb.text.toString()
            val txtmdSpSds = binding.mdspss.text.toString()
            val body_txtmdSpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdSpSbs
            )
            val body_txtmdSpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdSpSds
            )
            //discharge press
            val txtmdDpSbs = binding.mddpsb.text.toString()
            val txtmdDpSds = binding.mddpss.text.toString()
            val body_txtmdDpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdDpSbs
            )
            val body_txtmdDpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdDpSds
            )
            //Fuel Level
            val txtmdFlSbs = binding.mdflsb.text.toString()
            val txtmdFlSds = binding.mdflss.text.toString()
            val body_txtmdFlSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdFlSbs
            )
            val body_txtmdFlSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdFlSds
            )
            //Auto Start
            val txtmdAsSbs = binding.mdassb.text.toString()
            val txtmdAsSds = binding.mdasss.text.toString()
            val body_txtmdAsSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdAsSbs
            )
            val body_txtmdAsSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtmdAsSds
            )
            //=================Diesel Engine===================
            //waktu pencatatan
            val de_waktu_pencatatan_sebelum_start = binding.dewpsb.text.toString().trim()
            val de_waktu_pencatatan_sesudah_start = binding.dewpss.text.toString().trim()
            val body_de_waktu_pencatatan_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_waktu_pencatatan_sebelum_start
            )
            val body_de_waktu_pencatatan_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_waktu_pencatatan_sesudah_start
            )
            //Lube Oil Press
            val txtdeLubeOilPressSbs = binding.delosb.text.toString().trim()
            val txtdeLubeOilPressSds = binding.deloss.text.toString().trim()
            val body_txtdeLubeOilPressSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilPressSbs
            )
            val body_txtdeLubeOilPressSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilPressSds
            )
            //Battery VOltage
            val de_battery_voltage_sebelum_start =
                binding.debvsb.text.toString().trim()
            val de_battery_voltage_sesudah_start =
                binding.debvss.text.toString().trim()
            val body_de_battery_voltage_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_voltage_sebelum_start
            )
            val body_de_battery_voltage_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_voltage_sesudah_start
            )
            //Battery Ampere
            val de_battery_ampere_sebelum_start =
                binding.debasb.text.toString().trim()
            val de_battery_ampere_sesudah_start =
                binding.debass.text.toString().trim()
            val body_de_battery_ampere_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_ampere_sebelum_start
            )
            val body_de_battery_ampere_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_ampere_sesudah_start
            )
            //Battery Level
            val txtdeBatteryLevelSebelumStart =
                binding.deblsb.text.toString().trim()
            val txtdeBatteryLevelSesudahStart =
                binding.deblss.text.toString().trim()
            val body_txtdeBatteryLevelSebelumStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeBatteryLevelSebelumStart
            )
            val body_txtdeBatteryLevelSesudahStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeBatteryLevelSesudahStart
            )
            //Fuel Level
            val txtdeFuelLevelSebelumStart =
                binding.deflsb.text.toString().trim()
            val txtdeFuelLevelSesudahStart =
                binding.deflss.text.toString().trim()
            val body_txtdeFuelLevelSebelumStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeFuelLevelSebelumStart
            )
            val body_txtdeFuelLevelSesudahStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeFuelLevelSesudahStart
            )
            //Lube Oil Level
            val txtdeLubeOilLevelSbs =
                binding.delolsb.text.toString().trim()
            val txtdeLubeOilLevelSds =
                binding.delolss.text.toString().trim()
            val body_txtdeLubeOilLevelSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilLevelSbs
            )
            val body_txtdeLubeOilLevelSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilLevelSds
            )
            //Water Cooler level
            val txtdeWaterCoolerLevelSbs =
                binding.dewclsb.text.toString().trim()
            val txtdeWaterCoolerLevelSds =
                binding.dewclss.text.toString().trim()
            val body_txtdeWaterCoolerLevelSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeWaterCoolerLevelSbs
            )
            val body_txtdeWaterCoolerLevelSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeWaterCoolerLevelSds
            )
            //Speed
            val txtdeSpeedSbs =
                binding.dessb.text.toString().trim()
            val txtdeSpeedSds =
                binding.desss.text.toString().trim()
            val body_txtdeSpeedSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeSpeedSbs
            )
            val body_txtdeSpeedSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeSpeedSds
            )
            //Sunction Press
            val de_suction_press_sebelum_start =
                binding.despsb.text.toString().trim()
            val de_suction_press_sesudah_start =
                binding.despss.text.toString().trim()
            val body_de_suction_press_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_suction_press_sebelum_start
            )
            val body_de_suction_press_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_suction_press_sesudah_start
            )
            //Discharge Press
            val de_discharge_press_sebelum_start =
                binding.dedpsb.text.toString().trim()
            val de_discharge_press_sesudah_start =
                binding.dedpss.text.toString().trim()
            val body_de_discharge_press_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_discharge_press_sebelum_start
            )
            val body_de_discharge_press_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_discharge_press_sesudah_start
            )
            //Auto Start
            val txtdeAutoStartSbs =
                binding.deassb.text.toString().trim()
            val txtdeAutoStartSds =
                binding.deasss.text.toString().trim()
            val body_txtdeAutoStartSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeAutoStartSbs
            )
            val body_txtdeAutoStartSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeAutoStartSds
            )

            val catatan = binding.ket.text.toString().trim()
            val body_catatan: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                catatan
            )
            if (txtspWpSbs.isNotEmpty() &&
                txtspWpSds.isNotEmpty() &&
                txtspSpSbs.isNotEmpty() &&
                txtspSpSds.isNotEmpty() &&
                txtspDpSbs.isNotEmpty() &&
                txtspDpSds.isNotEmpty() &&
                txtspFlSbs.isNotEmpty() &&
                txtspFlSds.isNotEmpty() &&
                txtspAsSbs.isNotEmpty() &&
                txtspAsSds.isNotEmpty() &&
                txtspFlSbs.isNotEmpty() &&
                txtspFlSds.isNotEmpty() &&
                txtspAsSbs.isNotEmpty() &&
                txtspAsSds.isNotEmpty() &&
                txtmdWpSbs.isNotEmpty() &&
                txtmdWpSds.isNotEmpty() &&
                txtmdDpSbs.isNotEmpty() &&
                txtmdDpSds.isNotEmpty() &&
                txtmdSpSbs.isNotEmpty() &&
                txtmdSpSds.isNotEmpty() &&
                txtmdFlSbs.isNotEmpty() &&
                txtmdFlSds.isNotEmpty() &&
                txtmdAsSbs.isNotEmpty() &&
                txtmdAsSds.isNotEmpty() &&
                de_waktu_pencatatan_sebelum_start.isNotEmpty() &&
                de_waktu_pencatatan_sesudah_start.isNotEmpty() &&
                txtdeLubeOilPressSbs.isNotEmpty() &&
                txtdeLubeOilPressSds.isNotEmpty() &&
                de_battery_voltage_sebelum_start.isNotEmpty() &&
                de_battery_voltage_sesudah_start.isNotEmpty() &&
                de_battery_ampere_sebelum_start.isNotEmpty() &&
                de_battery_ampere_sesudah_start.isNotEmpty() &&
                txtdeBatteryLevelSebelumStart.isNotEmpty() &&
                txtdeBatteryLevelSesudahStart.isNotEmpty() &&
                txtdeFuelLevelSebelumStart.isNotEmpty() &&
                txtdeFuelLevelSesudahStart.isNotEmpty() &&
                txtdeLubeOilLevelSbs.isNotEmpty() &&
                txtdeLubeOilLevelSds.isNotEmpty() &&
                txtdeWaterCoolerLevelSbs.isNotEmpty() &&
                txtdeWaterCoolerLevelSds.isNotEmpty() &&
                txtdeSpeedSbs.isNotEmpty() &&
                txtdeSpeedSds.isNotEmpty() &&
                txtdeAutoStartSbs.isNotEmpty() &&
                txtdeAutoStartSds.isNotEmpty() &&
                de_suction_press_sebelum_start.isNotEmpty() &&
                de_suction_press_sesudah_start.isNotEmpty() &&
                de_discharge_press_sebelum_start.isNotEmpty() &&
                de_discharge_press_sesudah_start.isNotEmpty() &&
                catatan.isNotEmpty()
            ) {
                loading(true)

                api.update_ffblok(
                    body_id,
                    body_tw,
                    body_tahun,
                    body_tanggal_cek,
                    body_shift,
                    body_is_status,
                    body_txtspWpSbs,
                    body_txtspWpSds,
                    body_txtspSpSbs,
                    body_txtspSpSds,
                    body_txtspDpSbs,
                    body_txtspDpSds,
                    body_txtspFlSbs,
                    body_txtspFlSds,
                    body_txtspAsSbs,
                    body_txtspAsSds,
                    body_txtmdWpSbs,
                    body_txtmdWpSds,
                    body_txtmdSpSbs,
                    body_txtmdSpSds,
                    body_txtmdDpSbs,
                    body_txtmdDpSds,
                    body_txtmdFlSbs,
                    body_txtmdFlSds,
                    body_txtmdAsSbs,
                    body_txtmdAsSds,
                    body_de_waktu_pencatatan_sebelum_start,
                    body_de_waktu_pencatatan_sesudah_start,
                    body_txtdeLubeOilPressSbs,
                    body_txtdeLubeOilPressSds,
                    body_de_battery_voltage_sebelum_start,
                    body_de_battery_voltage_sesudah_start,
                    body_de_battery_ampere_sebelum_start,
                    body_de_battery_ampere_sesudah_start,
                    body_txtdeBatteryLevelSebelumStart,
                    body_txtdeBatteryLevelSesudahStart,
                    body_txtdeFuelLevelSebelumStart,
                    body_txtdeFuelLevelSesudahStart,
                    body_txtdeLubeOilLevelSbs,
                    body_txtdeLubeOilLevelSds,
                    body_txtdeWaterCoolerLevelSbs,
                    body_txtdeWaterCoolerLevelSds,
                    body_txtdeSpeedSbs,
                    body_txtdeSpeedSds,
                    body_de_suction_press_sebelum_start,
                    body_de_suction_press_sesudah_start,
                    body_de_discharge_press_sebelum_start,
                    body_de_discharge_press_sesudah_start,
                    body_txtdeAutoStartSbs,
                    body_txtdeAutoStartSds,
                    body_catatan,
                ).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.sukses == 1) {
                                finish()
                                toast("Disimpan Sbagai Draft")
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

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        cekffblok1 = null
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

