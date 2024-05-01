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
import com.sipmase.sipmase.model.ffblok2.FFBlok2NewModel
//import com.sipmase.sipmase.model.ffblok2.FFBlok2Model
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
        var cekffblok2: FFBlok2NewModel? = null
    }
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
                FFBlok2NewModel::class.java
            )
        binding.txttgl.text = cekffblok2!!.tanggalCek

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

        binding.jpwpsb.setText(cekffblok2!!.jpWaktuPencatatanSebelumStart)
        binding.jpwpss.setText(cekffblok2!!.jpWaktuPencatatanSesudahStart)
        //Discharge Press
        binding.jpdpsb.setText(cekffblok2!!.jpDischargePressSebelumStart)
        binding.jpdpss.setText(cekffblok2!!.jpDischargePressSesudahStart)
        //Auto Start
        if (cekffblok2!!.jpAutoStartSebelumStart != null) {
            binding.jpassb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.jpAutoStartSebelumStart }
            ))
        }
        if (cekffblok2!!.jpAutoStartSesudahStart != null) {
            binding.jpassb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.jpAutoStartSesudahStart }
            ))
        }
        //Auto Stop
        if (cekffblok2!!.jpAutoStopSebelumStart != null) {
            binding.jpastsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.jpAutoStopSebelumStart }
            ))
        }
        if (cekffblok2!!.jpAutoStopSesudahStart != null) {
            binding.jpastss.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.jpAutoStopSesudahStart }
            ))
        }
        //=================Motor Driven===================
        //waktu pencatatan
        binding.mdwpsb.setText(cekffblok2!!.mdWaktuPencatatanSebelumStart)
        binding.mdwpss.setText(cekffblok2!!.mdWaktuPencatatanSesudahStart)
        //discharge press
        binding.mddpsb.setText(cekffblok2!!.mdDischargePressSebelumStart)
        binding.mddpss.setText(cekffblok2!!.mdDischargePressSesudahStart)
        //Lube Oil Level
        if (cekffblok2!!.mdLubeOilLevelSebelumStart != null) {
            binding.mdlolsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.mdLubeOilLevelSebelumStart }
            ))
        }
        if (cekffblok2!!.mdLubeOilLevelSesudahStart != null) {
            binding.mdlolss.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.mdLubeOilLevelSesudahStart }
            ))
        }
        //Auto Start
        binding.mdassb.setText(cekffblok2!!.mdAutoStartSebelumStart)
        binding.mdasss.setText(cekffblok2!!.mdAutoStartSesudahStart)
        //=================Diesel Engine===================
        //waktu pencatatan
        binding.dewpsb.setText(cekffblok2!!.deWaktuPencatatanSebelumStart)
        binding.dewpss.setText(cekffblok2!!.deWaktuPencatatanSesudahStart)
        //Battery VOltage
        binding.debvsb.setText(cekffblok2!!.deBatteryVoltageSebelumStart)
        binding.debvss.setText(cekffblok2!!.deBatteryVoltageSesudahStart)
        //Battery Ampere
        binding.debasb.setText(cekffblok2!!.deBatteryAmpereSebelumStart)
        binding.debass.setText(cekffblok2!!.deBatteryAmpereSesudahStart)
        //Speed
        binding.dessb.setText(cekffblok2!!.deSpeedSebelumStart)
        binding.desss.setText(cekffblok2!!.deSpeedSesudahStart)
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
        //Lube Oil press
        binding.delopsb.setText(cekffblok2!!.deLubeOilPressSebelumStart)
        binding.delopss.setText(cekffblok2!!.deLubeOilPressSesudahStart)
        //Lube Oil temperature
        binding.delotsb.setText(cekffblok2!!.deLubeOilTempSebelumStart)
        binding.delotss.setText(cekffblok2!!.deLubeOilTempSesudahStart)

        //Water coolent level
        if (cekffblok2!!.deWaterCoolentLevelSebelumStart != null) {
            binding.dewclsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deWaterCoolentLevelSebelumStart }
            ))
        }
        if (cekffblok2!!.deWaterCoolentLevelSesudahStart != null) {
            binding.dewclss.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deWaterCoolentLevelSesudahStart }
            ))
        }
        //Fuel tank level
        if (cekffblok2!!.deFuelTankLevelSebelumStart != null) {
            binding.deftlsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deFuelTankLevelSebelumStart }
            ))
        }
        if (cekffblok2!!.deFuelTankLevelSesudahStart != null) {
            binding.deftlss.setSelection(low.indexOf(
                low.first { elem -> elem == cekffblok2!!.deFuelTankLevelSesudahStart }
            ))
        }
        //Discharge Press
        binding.dedpsb.setText(cekffblok2!!.deDischargePressSebelumStart)
        binding.dedpss.setText(cekffblok2!!.deDischargePressSesudahStart)
        //Auto Start
        binding.deassb.setText(cekffblok2!!.deAutoStartSebelumStart)
        binding.deasss.setText(cekffblok2!!.deAutoStartSesudahStart)
        //return line rate
        binding.derlrsb.setText(cekffblok2!!.deReturnLineRateSebelumStart)
        binding.derlrss.setText(cekffblok2!!.deReturnLineRateSesudahStart)
        //header line pressure
        binding.dehlpsb.setText(cekffblok2!!.deHeaderLinePressureSebelumStart)
        binding.dehlpss.setText(cekffblok2!!.deHeaderLinePressureSesudahStart)
        //relief valve pressure
        binding.dervpsb.setText(cekffblok2!!.deReliefValvePressureSebelumStart)
        binding.dervpss.setText(cekffblok2!!.deReliefValvePressureSesudahStart)


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

            val body_tanggal_cek: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekffblok2!!.tanggalCek}"
            )
            //=================Jockey Pump===================
            //waktu pencatatan
            val txtjpWpSbs = binding.jpwpsb.text.toString()
            val txtjpWpSds = binding.jpwpss.text.toString()
            val body_txtjpWpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpWpSbs
            )
            val body_txtjpWpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpWpSds
            )
            //Discharge Press
            val txtjpDpSbs = binding.jpdpsb.text.toString()
            val txtjpDpSds = binding.jpdpss.text.toString()
            val body_txtjpDpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpDpSbs
            )
            val body_txtjpDpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpDpSds
            )
            //Auto Start
            val auto_start_sebelum = binding.jpassb.selectedItem.toString()
            val body_txtjpAsSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                auto_start_sebelum
            )
            val auto_start_sesudah = binding.jpassb.selectedItem.toString()
            val body_txtjpAsSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                auto_start_sesudah
            )
            //Auto Stop
            val txtjpAstSbs = binding.jpastsb.selectedItem.toString()
            val txtjpAstSds = binding.jpastss.selectedItem.toString()
            val body_txtjpAstSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpAstSbs
            )
            val body_txtjpAstSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpAstSds
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
            //lube oil Level
            val lube_oil_level_sebelum = binding.mdlolsb.selectedItem.toString()
            val body_txtmdLolSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                lube_oil_level_sebelum
            )
            val lube_oil_level_sesudah = binding.mdlolss.selectedItem.toString()
            val body_txtmdLolSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                lube_oil_level_sesudah
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
            //Lube Oil Pressure
            val txtdeLubeOilPressSbs = binding.delopsb.text.toString().trim()
            val txtdeLubeOilPressSds = binding.delopss.text.toString().trim()
            val body_txtdeLubeOilPressSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilPressSbs
            )
            val body_txtdeLubeOilPressSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilPressSds
            )
            //Lube Oil Teampeerature
            val txtdeLubeOilTempSbs = binding.delotsb.text.toString().trim()
            val txtdeLubeOilTempSds = binding.delotss.text.toString().trim()
            val body_txtdeLubeOilTempSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilTempSbs
            )
            val body_txtdeLubeOilTempSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilTempSds
            )
            //Water Coolent level
            val de_water_coolent_sebelum = binding.dewclsb.selectedItem.toString()
            val body_txtdeWaterCoolentLevelSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_water_coolent_sebelum
            )
            val de_water_coolent_sesudah = binding.dewclss.selectedItem.toString()
            val body_txtdeWaterCoolentLevelSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_water_coolent_sesudah
            )
            //Fuel TankLevel
            val de_fuel_tank_level_sebelum = binding.deftlsb.selectedItem.toString()
            val body_txtdeFuelTankLevelSebelumStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_fuel_tank_level_sebelum
            )
            val de_fuel_tank_level_sesudah = binding.deftlss.selectedItem.toString()
            val body_txtdeFuelTankLevelSesudahStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_fuel_tank_level_sesudah
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
            //Return Line Rate
            val txtdeReturnLineRateSbs =
                binding.derlrsb.text.toString().trim()
            val txtdeReturnLineRateSds =
                binding.derlrss.text.toString().trim()
            val body_txtdeReturnLineRateSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeReturnLineRateSbs
            )
            val body_txtdeReturnLineRateSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeReturnLineRateSds
            )
            //Header Line pressure
            val txtdeHeaderLinePressureSbs =
                binding.dehlpsb.text.toString().trim()
            val txtdeHeaderLinePressureSds =
                binding.dehlpss.text.toString().trim()
            val body_txtdeHeaderLinePressureSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeHeaderLinePressureSbs
            )
            val body_txtdeHeaderLinePressureSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeHeaderLinePressureSds
            )
            //Relief Valve Pressure
            val txtdeReliefValvePressureSbs =
                binding.dervpsb.text.toString().trim()
            val txtdeReliefValvePressureSds =
                binding.dervpss.text.toString().trim()
            val body_txtdeReliefValvePressureSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeReliefValvePressureSbs
            )
            val body_txtdeReliefValvePressureSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeReliefValvePressureSds
            )


            val catatan = binding.ket.text.toString().trim()
            val body_catatan: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                catatan
            )
            if (txtjpWpSbs.isNotEmpty() &&
                txtjpWpSds.isNotEmpty() &&
                txtjpDpSbs.isNotEmpty() &&
                txtjpDpSds.isNotEmpty() &&
                auto_start_sebelum.isNotEmpty() &&
                auto_start_sesudah.isNotEmpty() &&
                txtjpAstSbs.isNotEmpty() &&
                txtjpAstSds.isNotEmpty() &&
                txtmdWpSbs.isNotEmpty() &&
                txtmdWpSds.isNotEmpty() &&
                txtmdDpSbs.isNotEmpty() &&
                txtmdDpSds.isNotEmpty() &&
                txtmdAsSbs.isNotEmpty() &&
                txtmdAsSds.isNotEmpty() &&
                de_waktu_pencatatan_sebelum_start.isNotEmpty() &&
                de_waktu_pencatatan_sesudah_start.isNotEmpty() &&
                de_battery_voltage_sebelum_start.isNotEmpty() &&
                de_battery_voltage_sesudah_start.isNotEmpty() &&
                de_battery_ampere_sebelum_start.isNotEmpty() &&
                de_battery_ampere_sesudah_start.isNotEmpty() &&
                txtdeSpeedSbs.isNotEmpty() &&
                txtdeSpeedSds.isNotEmpty() &&
                de_lube_oil_level_sebelum.isNotEmpty() &&
                de_lube_oil_level_sesudah.isNotEmpty() &&
                txtdeLubeOilPressSbs.isNotEmpty() &&
                txtdeLubeOilPressSds.isNotEmpty() &&
                txtdeLubeOilTempSbs.isNotEmpty() &&
                txtdeLubeOilTempSds.isNotEmpty() &&
                de_water_coolent_sebelum.isNotEmpty() &&
                de_water_coolent_sesudah.isNotEmpty() &&
                de_fuel_tank_level_sebelum.isNotEmpty() &&
                de_fuel_tank_level_sesudah.isNotEmpty() &&
                de_discharge_press_sebelum_start.isNotEmpty() &&
                de_discharge_press_sesudah_start.isNotEmpty() &&
                txtdeAutoStartSbs.isNotEmpty() &&
                txtdeAutoStartSds.isNotEmpty() &&
                txtdeReturnLineRateSbs.isNotEmpty() &&
                txtdeReturnLineRateSds.isNotEmpty() &&
                txtdeHeaderLinePressureSbs.isNotEmpty() &&
                txtdeHeaderLinePressureSds.isNotEmpty() &&
                txtdeReliefValvePressureSbs.isNotEmpty() &&
                txtdeReliefValvePressureSds.isNotEmpty() &&
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
                    body_txtjpWpSbs,
                    body_txtjpWpSds,
                    body_txtjpDpSbs,
                    body_txtjpDpSds,
                    body_txtjpAsSbs,
                    body_txtjpAsSds,
                    body_txtjpAstSbs,
                    body_txtjpAstSds,
                    body_txtmdWpSbs,
                    body_txtmdWpSds,
                    body_txtmdDpSbs,
                    body_txtmdDpSds,
                    body_txtmdLolSbs,
                    body_txtmdLolSds,
                    body_txtmdAsSbs,
                    body_txtmdAsSds,
                    body_de_waktu_pencatatan_sebelum_start,
                    body_de_waktu_pencatatan_sesudah_start,
                    body_de_battery_voltage_sebelum_start,
                    body_de_battery_voltage_sesudah_start,
                    body_de_battery_ampere_sebelum_start,
                    body_de_battery_ampere_sesudah_start,
                    body_txtdeSpeedSbs,
                    body_txtdeSpeedSds,
                    body_txtdeLubeOilLevelSbs,
                    body_txtdeLubeOilLevelSds,
                    body_txtdeLubeOilPressSbs,
                    body_txtdeLubeOilPressSds,
                    body_txtdeLubeOilTempSbs,
                    body_txtdeLubeOilTempSds,
                    body_txtdeWaterCoolentLevelSbs,
                    body_txtdeWaterCoolentLevelSds,
                    body_txtdeFuelTankLevelSebelumStart,
                    body_txtdeFuelTankLevelSesudahStart,
                    body_de_discharge_press_sebelum_start,
                    body_de_discharge_press_sesudah_start,
                    body_txtdeAutoStartSbs,
                    body_txtdeAutoStartSds,
                    body_txtdeReturnLineRateSbs,
                    body_txtdeReturnLineRateSds,
                    body_txtdeHeaderLinePressureSbs,
                    body_txtdeHeaderLinePressureSds,
                    body_txtdeReliefValvePressureSbs,
                    body_txtdeReliefValvePressureSds,
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
                                info(response.body()!!.data)
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

            val body_tanggal_cek: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekffblok2!!.tanggalCek}"
            )
            //=================Jockey Pump===================
            //waktu pencatatan
            val txtjpWpSbs = binding.jpwpsb.text.toString()
            val txtjpWpSds = binding.jpwpss.text.toString()
            val body_txtjpWpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpWpSbs
            )
            val body_txtjpWpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpWpSds
            )
            //Discharge Press
            val txtjpDpSbs = binding.jpdpsb.text.toString()
            val txtjpDpSds = binding.jpdpss.text.toString()
            val body_txtjpDpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpDpSbs
            )
            val body_txtjpDpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpDpSds
            )
            //Auto Start
            val auto_start_sebelum = binding.jpassb.selectedItem.toString()
            val body_txtjpAsSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                auto_start_sebelum
            )
            val auto_start_sesudah = binding.jpassb.selectedItem.toString()
            val body_txtjpAsSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                auto_start_sesudah
            )
            //Auto Stop
            val txtjpAstSbs = binding.jpastsb.selectedItem.toString()
            val txtjpAstSds = binding.jpastss.selectedItem.toString()
            val body_txtjpAstSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpAstSbs
            )
            val body_txtjpAstSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtjpAstSds
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
            //lube oil Level
            val lube_oil_level_sebelum = binding.mdlolsb.selectedItem.toString()
            val body_txtmdLolSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                lube_oil_level_sebelum
            )
            val lube_oil_level_sesudah = binding.mdlolss.selectedItem.toString()
            val body_txtmdLolSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                lube_oil_level_sesudah
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
            //Lube Oil Pressure
            val txtdeLubeOilPressSbs = binding.delopsb.text.toString().trim()
            val txtdeLubeOilPressSds = binding.delopss.text.toString().trim()
            val body_txtdeLubeOilPressSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilPressSbs
            )
            val body_txtdeLubeOilPressSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilPressSds
            )
            //Lube Oil Teampeerature
            val txtdeLubeOilTempSbs = binding.delotsb.text.toString().trim()
            val txtdeLubeOilTempSds = binding.delotss.text.toString().trim()
            val body_txtdeLubeOilTempSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilTempSbs
            )
            val body_txtdeLubeOilTempSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeLubeOilTempSds
            )
            //Water Coolent level
            val de_water_coolent_sebelum = binding.dewclsb.selectedItem.toString()
            val body_txtdeWaterCoolentLevelSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_water_coolent_sebelum
            )
            val de_water_coolent_sesudah = binding.dewclss.selectedItem.toString()
            val body_txtdeWaterCoolentLevelSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_water_coolent_sesudah
            )
            //Fuel TankLevel
            val de_fuel_tank_level_sebelum = binding.deftlsb.selectedItem.toString()
            val body_txtdeFuelTankLevelSebelumStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_fuel_tank_level_sebelum
            )
            val de_fuel_tank_level_sesudah = binding.deftlss.selectedItem.toString()
            val body_txtdeFuelTankLevelSesudahStart: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_fuel_tank_level_sesudah
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
            //Return Line Rate
            val txtdeReturnLineRateSbs =
                binding.derlrsb.text.toString().trim()
            val txtdeReturnLineRateSds =
                binding.derlrss.text.toString().trim()
            val body_txtdeReturnLineRateSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeReturnLineRateSbs
            )
            val body_txtdeReturnLineRateSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeReturnLineRateSds
            )
            //Header Line pressure
            val txtdeHeaderLinePressureSbs =
                binding.dehlpsb.text.toString().trim()
            val txtdeHeaderLinePressureSds =
                binding.dehlpss.text.toString().trim()
            val body_txtdeHeaderLinePressureSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeHeaderLinePressureSbs
            )
            val body_txtdeHeaderLinePressureSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeHeaderLinePressureSds
            )
            //Relief Valve Pressure
            val txtdeReliefValvePressureSbs =
                binding.dervpsb.text.toString().trim()
            val txtdeReliefValvePressureSds =
                binding.dervpss.text.toString().trim()
            val body_txtdeReliefValvePressureSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeReliefValvePressureSbs
            )
            val body_txtdeReliefValvePressureSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtdeReliefValvePressureSds
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
                body_txtjpWpSbs,
                body_txtjpWpSds,
                body_txtjpDpSbs,
                body_txtjpDpSds,
                body_txtjpAsSbs,
                body_txtjpAsSds,
                body_txtjpAstSbs,
                body_txtjpAstSds,
                body_txtmdWpSbs,
                body_txtmdWpSds,
                body_txtmdDpSbs,
                body_txtmdDpSds,
                body_txtmdLolSbs,
                body_txtmdLolSds,
                body_txtmdAsSbs,
                body_txtmdAsSds,
                body_de_waktu_pencatatan_sebelum_start,
                body_de_waktu_pencatatan_sesudah_start,
                body_de_battery_voltage_sebelum_start,
                body_de_battery_voltage_sesudah_start,
                body_de_battery_ampere_sebelum_start,
                body_de_battery_ampere_sesudah_start,
                body_txtdeSpeedSbs,
                body_txtdeSpeedSds,
                body_txtdeLubeOilLevelSbs,
                body_txtdeLubeOilLevelSds,
                body_txtdeLubeOilPressSbs,
                body_txtdeLubeOilPressSds,
                body_txtdeLubeOilTempSbs,
                body_txtdeLubeOilTempSds,
                body_txtdeWaterCoolentLevelSbs,
                body_txtdeWaterCoolentLevelSds,
                body_txtdeFuelTankLevelSebelumStart,
                body_txtdeFuelTankLevelSesudahStart,
                body_de_discharge_press_sebelum_start,
                body_de_discharge_press_sesudah_start,
                body_txtdeAutoStartSbs,
                body_txtdeAutoStartSds,
                body_txtdeReturnLineRateSbs,
                body_txtdeReturnLineRateSds,
                body_txtdeHeaderLinePressureSbs,
                body_txtdeHeaderLinePressureSds,
                body_txtdeReliefValvePressureSbs,
                body_txtdeReliefValvePressureSds,
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
