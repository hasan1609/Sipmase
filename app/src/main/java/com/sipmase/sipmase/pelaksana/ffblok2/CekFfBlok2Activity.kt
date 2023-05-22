package com.sipmase.sipmase.pelaksana.ffblok2

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.constant.Constant
import com.sipmase.sipmase.databinding.ActivityCekFfBlok2Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.ffblok2.FFBlok2Model
import com.sipmase.sipmase.pelaksana.seawater.CekSeaWaterActivity
import com.sipmase.sipmase.session.SessionManager
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CekFfBlok2Activity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekFfBlok2Binding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog

    val sheet= TtdFfBlok2Fragment()

    companion object {
        var cekffblok2: FFBlok2Model? = null
    }

    var currentDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_ff_blok2)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekffblok2 =
            gson.fromJson(
                intent.getStringExtra("cekffblok2"),
                FFBlok2Model::class.java
            )

        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = currentDate

        binding.namak3.text = cekffblok2!!.k3Nama
        binding.namaoperator.text = cekffblok2!!.operatorNama
        binding.namaspv.text = cekffblok2!!.supervisorNama
        if(cekffblok2!!.supervisorTtd != null){
            binding.btnSpv.visibility = View.GONE
            binding.ttdspv.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-ffblok2/"}${cekffblok2!!.supervisorTtd}").into(binding.ttdspv)
        }else{
            binding.btnSpv.visibility = View.VISIBLE
            binding.ttdspv.visibility = View.GONE
            binding.btnSpv.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "spv")
                bundle.putString("id", cekffblok2!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdFfBlok2Fragment")
            }
        }
        if(cekffblok2!!.k3Ttd != null){
            binding.btnK3.visibility = View.GONE
            binding.ttdk3.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-ffblok2/"}${cekffblok2!!.k3Ttd}").into(binding.ttdk3)
        }else{
            binding.btnK3.visibility = View.VISIBLE
            binding.ttdk3.visibility = View.GONE
            binding.btnK3.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "k3")
                bundle.putString("id", cekffblok2!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdFfBlok2Fragment")
            }
        }
        if(cekffblok2!!.operatorTtd != null){
            binding.btnOperator.visibility = View.GONE
            binding.ttdoperator.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-ffblok2/"}${cekffblok2!!.operatorTtd}").into(binding.ttdoperator)
        }else{
            binding.btnOperator.visibility = View.VISIBLE
            binding.ttdoperator.visibility = View.GONE
            binding.btnOperator.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "operator")
                bundle.putString("id", cekffblok2!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdFfBlok2Fragment")
            }
        }


        if(cekffblok2!!.isStatus == 1){
            binding.btnDraft.visibility = View.GONE
            binding.btnSubmit.visibility = View.GONE
        }else if(cekffblok2!!.isStatus == 3){
            binding.btnDraft.visibility = View.GONE
        }
        if(cekffblok2!!.shift != null){
            binding.txtshift.text = cekffblok2!!.shift
        }else{
            binding.txtshift.text = sessionManager.getNama()
        }


        val low: Array<String> = resources.getStringArray(R.array.low)

        binding.jpwpsb.setText(cekffblok2!!.spWaktuPencatatanSebelumStart)
        binding.jpwpss.setText(cekffblok2!!.spWaktuPencatatanSesudahStart)
        //Sunction Press
        binding.jpspsb.setText(cekffblok2!!.spSuctionPressSebelumStart)
        binding.jpspss.setText(cekffblok2!!.spSuctionPressSesudahStart)
        //Discharge Press
        binding.jpdpsb.setText(cekffblok2!!.spDischargePressSebelumStart)
        binding.jpdpss.setText(cekffblok2!!.spDischargePressSesudahStart)
        //Auto Start
        if (cekffblok2!!.spAutoStartSebelumStart != null) {
            binding.jpassb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.spAutoStartSebelumStart }
            ))
        }
        if (cekffblok2!!.spAutoStartSesudahStart != null) {
            binding.jpassb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.spAutoStartSesudahStart }
            ))
        }
        //Auto Stop
        if (cekffblok2!!.spAutoStopSebelumStart != null) {
            binding.jpastsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.spAutoStopSebelumStart }
            ))
        }
        if (cekffblok2!!.spAutoStopSesudahStart != null) {
            binding.jpastss.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.spAutoStopSesudahStart }
            ))
        }
        //=================Motor Driven===================
        //waktu pencatatan
        binding.mdwpsb.setText(cekffblok2!!.mdWaktuPencatatanSebelumStart)
        binding.mdwpss.setText(cekffblok2!!.mdWaktuPencatatanSesudahStart)
        //sunction press
        binding.mdspsb.setText(cekffblok2!!.mdSuctionPressSebelumStart)
        binding.mdspss.setText(cekffblok2!!.mdSuctionPressSesudahStart)
        //discharge press
        binding.mddpsb.setText(cekffblok2!!.mdDischargePressSebelumStart)
        binding.mddpss.setText(cekffblok2!!.mdDischargePressSesudahStart)
        //Fuel Level
        if (cekffblok2!!.mdFullLevelSebelumStart != null) {
            binding.mdflsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.mdFullLevelSebelumStart }
            ))
        }
        if (cekffblok2!!.mdFullLevelSesudahStart != null) {
            binding.mdflss.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.mdFullLevelSesudahStart }
            ))
        }
        //Auto Start
        binding.mdassb.setText(cekffblok2!!.mdAutoStartSebelumStart)
        binding.mdasss.setText(cekffblok2!!.mdAutoStartSesudahStart)
        //=================Diesel Engine===================
        //waktu pencatatan
        binding.dewpsb.setText(cekffblok2!!.deWaktuPencatatanSebelumStart)
        binding.dewpss.setText(cekffblok2!!.deWaktuPencatatanSesudahStart)
        //Lube Oil Press
        binding.delosb.setText(cekffblok2!!.deLubeOilPressSebelumStart)
        binding.deloss.setText(cekffblok2!!.deLubeOilPressSesudahStart)
        //Battery VOltage
        binding.debvsb.setText(cekffblok2!!.deBatteryVoltageSebelumStart)
        binding.debvss.setText(cekffblok2!!.deBatteryVoltageSesudahStart)
        //Battery Ampere
        binding.debasb.setText(cekffblok2!!.deBatteryAmpereSebelumStart)
        binding.debass.setText(cekffblok2!!.deBatteryAmpereSesudahStart)
        //Battery Level
        if (cekffblok2!!.deBatteryLevelSebelumStart != null) {
            binding.deblsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deBatteryLevelSebelumStart }
            ))
        }
        if (cekffblok2!!.deBatteryLevelSesudahStart != null) {
            binding.deblss.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deBatteryLevelSesudahStart }
            ))
        }
        //Fuel Level
        if (cekffblok2!!.deFuelLevelSebelumStart != null) {
            binding.deflsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deFuelLevelSebelumStart }
            ))
        }
        if (cekffblok2!!.deFuelLevelSesudahStart != null) {
            binding.deflss.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deFuelLevelSesudahStart }
            ))
        }
        //Lube Oil Level
        if (cekffblok2!!.deLubeOilLevelSebelumStart != null) {
            binding.delolsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deLubeOilLevelSebelumStart }
            ))
        }
        if (cekffblok2!!.deLubeOilLevelSesudahStart != null) {
            binding.delolss.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deLubeOilLevelSesudahStart }
            ))
        }
        //Water Cooler level
        if (cekffblok2!!.deWaterCoolerLevelSebelumStart != null) {
            binding.dewclsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deWaterCoolerLevelSebelumStart }
            ))
        }
        if (cekffblok2!!.deWaterCoolerLevelSesudahStart != null) {
            binding.dewclss.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deWaterCoolerLevelSesudahStart }
            ))
        }
        //Speed
        binding.dessb.setText(cekffblok2!!.deSpeedSebelumStart)
        binding.desss.setText(cekffblok2!!.deSpeedSesudahStart)

        //Sunction Press
        binding.despsb.setText(cekffblok2!!.deSuctionPressSebelumStart)
        binding.despss.setText(cekffblok2!!.deSuctionPressSesudahStart)
        //Discharge Press
        binding.dedpsb.setText(cekffblok2!!.deDischargePressSebelumStart)
        binding.dedpss.setText(cekffblok2!!.deDischargePressSesudahStart)
        //Auto Start
        binding.deassb.setText(cekffblok2!!.deAutoStartSebelumStart)
        binding.deasss.setText(cekffblok2!!.deAutoStartSesudahStart)

        binding.ket.setText(cekffblok2!!.catatan)


        binding.btnSubmit.setOnClickListener {
                val body_tw: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    cekffblok2!!.tw.toString()
                )

                val body_shift: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    sessionManager.getNama().toString()
                )

                val body_is_status: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"), "1"
                )

                val body_tahun: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    cekffblok2!!.tahun
                )

                val body_id: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    "${cekffblok2!!.id}"
                )

                val sdf = SimpleDateFormat("yyyy-M-dd ")
                val tanggal_cek = sdf.format(Date())

                val body_tanggal_cek: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    "${tanggal_cek}"
                )
                //=================Jockey Pump===================
                //waktu pencatatan
                val txtspWpSbs = binding.jpwpsb.text.toString()
                val txtspWpSds = binding.jpwpss.text.toString()
                val body_txtspWpSbs: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    txtspWpSbs
                )
                val body_txtspWpSds: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    txtspWpSds
                )
                //Sunction Press
                val txtspSpSbs = binding.jpspsb.text.toString()
                val txtspSpSds = binding.jpspss.text.toString()
                val body_txtspSpSbs: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    txtspSpSbs
                )
                val body_txtspSpSds: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    txtspSpSds
                )
                //Discharge Press
                val txtspDpSbs = binding.jpdpsb.text.toString()
                val txtspDpSds = binding.jpdpss.text.toString()
                val body_txtspDpSbs: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    txtspDpSbs
                )
                val body_txtspDpSds: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    txtspDpSds
                )
                //Auto Start
             val auto_start_sebelum = binding.jpassb.selectedItem.toString()
                val body_txtspAsSbs: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    auto_start_sebelum
                )
            val auto_start_sesudah = binding.jpassb.selectedItem.toString()
                val body_txtspAsSds: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    auto_start_sesudah
                )
                //Auto Stop
                val txtspAstSbs = binding.jpastsb.selectedItem.toString()
                val txtspAstSds = binding.jpastss.selectedItem.toString()
                val body_txtspAstSbs: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    txtspAstSbs
                )
                val body_txtspAstSds: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    txtspAstSds
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
            val fuel_level_sebelum = binding.mdflsb.selectedItem.toString()
                val body_txtmdFlSbs: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    fuel_level_sebelum
                )
            val fuel_level_sesudah = binding.mdflss.selectedItem.toString()
                val body_txtmdFlSds: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    fuel_level_sesudah
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
            val battery_level_sebelum = binding.deblsb.selectedItem.toString()
            val body_txtdeBatteryLevelSebelumStart: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    battery_level_sebelum
                )
            val battery_level_sesudah = binding.deblss.selectedItem.toString()
            val body_txtdeBatteryLevelSesudahStart: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    battery_level_sesudah
                )
                //Fuel Level
            val de_fuel_level_sebelum = binding.deflsb.selectedItem.toString()
            val body_txtdeFuelLevelSebelumStart: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_fuel_level_sebelum
                )
            val de_fuel_level_sesudah = binding.deflss.selectedItem.toString()
            val body_txtdeFuelLevelSesudahStart: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_fuel_level_sesudah
                )
                //Lube Oil Level
            val de_lube_oil_level_sebelum = binding.delolsb.selectedItem.toString()
            val body_txtdeLubeOilLevelSbs: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_lube_oil_level_sebelum
                )
            val de_lube_oil_level_sesudah = binding.delolss.selectedItem.toString()
                val body_txtdeLubeOilLevelSds: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_lube_oil_level_sesudah
                )
                //Water Cooler level
            val de_water_cooler_sebelum = binding.dewclsb.selectedItem.toString()
                val body_txtdeWaterCoolerLevelSbs: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_water_cooler_sebelum
                )
            val de_water_cooler_sesudah = binding.dewclss.selectedItem.toString()
                val body_txtdeWaterCoolerLevelSds: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_water_cooler_sesudah
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
                    txtspAstSbs.isNotEmpty() &&
                    txtspAstSds.isNotEmpty() &&
                    txtmdWpSbs.isNotEmpty() &&
                    txtmdWpSds.isNotEmpty() &&
                    txtmdDpSbs.isNotEmpty() &&
                    txtmdDpSds.isNotEmpty() &&
                    txtmdSpSbs.isNotEmpty() &&
                    txtmdSpSds.isNotEmpty() &&
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

                    api.update_ffblok2(
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
                        body_txtspAsSbs,
                        body_txtspAsSds,
                        body_txtspAstSbs,
                        body_txtspAstSds,
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
            val body_tw: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekffblok2!!.tw.toString()
            )

            val body_shift: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                sessionManager.getNama().toString()
            )

            val body_is_status: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), "${cekffblok2!!.isStatus}"
            )

            val body_tahun: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekffblok2!!.tahun
            )

            val body_id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekffblok2!!.id}"
            )

            val sdf = SimpleDateFormat("yyyy-M-dd ")
            val tanggal_cek = sdf.format(Date())

            val body_tanggal_cek: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${tanggal_cek}"
            )
            //=================Jockey Pump===================
            //waktu pencatatan
            val txtspWpSbs = binding.jpwpsb.text.toString()
            val txtspWpSds = binding.jpwpss.text.toString()
            val body_txtspWpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspWpSbs
            )
            val body_txtspWpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspWpSds
            )
            //Sunction Press
            val txtspSpSbs = binding.jpspsb.text.toString()
            val txtspSpSds = binding.jpspss.text.toString()
            val body_txtspSpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspSpSbs
            )
            val body_txtspSpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspSpSds
            )
            //Discharge Press
            val txtspDpSbs = binding.jpdpsb.text.toString()
            val txtspDpSds = binding.jpdpss.text.toString()
            val body_txtspDpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspDpSbs
            )
            val body_txtspDpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspDpSds
            )
            //Auto Start
            val auto_start_sebelum = binding.jpassb.selectedItem.toString()
            val body_txtspAsSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                auto_start_sebelum
            )
            val auto_start_sesudah = binding.jpassb.selectedItem.toString()
            val body_txtspAsSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                auto_start_sesudah
            )
            //Auto Stop
            val txtspAstSbs = binding.jpastsb.selectedItem.toString()
            val txtspAstSds = binding.jpastss.selectedItem.toString()
            val body_txtspAstSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspAstSbs
            )
            val body_txtspAstSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspAstSds
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
            val fuel_level_sebelum = binding.mdflsb.selectedItem.toString()
            val body_txtmdFlSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                fuel_level_sebelum
            )
            val fuel_level_sesudah = binding.mdflss.selectedItem.toString()
            val body_txtmdFlSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                fuel_level_sesudah
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
            val battery_level_sebelum = binding.deblsb.selectedItem.toString()
            val body_txtdeBatteryLevelSebelumStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                battery_level_sebelum
            )
            val battery_level_sesudah = binding.deblss.selectedItem.toString()
            val body_txtdeBatteryLevelSesudahStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                battery_level_sesudah
            )
            //Fuel Level
            val de_fuel_level_sebelum = binding.deflsb.selectedItem.toString()
            val body_txtdeFuelLevelSebelumStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_fuel_level_sebelum
            )
            val de_fuel_level_sesudah = binding.deflss.selectedItem.toString()
            val body_txtdeFuelLevelSesudahStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_fuel_level_sesudah
            )
            //Lube Oil Level
            val de_lube_oil_level_sebelum = binding.delolsb.selectedItem.toString()
            val body_txtdeLubeOilLevelSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_lube_oil_level_sebelum
            )
            val de_lube_oil_level_sesudah = binding.delolss.selectedItem.toString()
            val body_txtdeLubeOilLevelSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_lube_oil_level_sesudah
            )
            //Water Cooler level
            val de_water_cooler_sebelum = binding.dewclsb.selectedItem.toString()
            val body_txtdeWaterCoolerLevelSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_water_cooler_sebelum
            )
            val de_water_cooler_sesudah = binding.dewclss.selectedItem.toString()
            val body_txtdeWaterCoolerLevelSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_water_cooler_sesudah
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

                api.update_ffblok2(
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
                    body_txtspAsSbs,
                    body_txtspAsSds,
                    body_txtspAstSbs,
                    body_txtspAstSds,
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

        }


    }

    override fun onStart() {
        super.onStart()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        cekffblok2 = null
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
