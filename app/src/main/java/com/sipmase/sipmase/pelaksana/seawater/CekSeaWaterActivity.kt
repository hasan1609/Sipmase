package com.sipmase.sipmase.pelaksana.seawater

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.constant.Constant
import com.sipmase.sipmase.databinding.ActivityCekSeaWaterBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.seawater.SeaWaterModel
import com.sipmase.sipmase.pelaksana.ffblok1.CekFfBlok1Activity
import com.sipmase.sipmase.pelaksana.ffblok1.TtdFfBlok1Fragment
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

class CekSeaWaterActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekSeaWaterBinding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog
    val sheet= TtdSeawaterFragment()

    companion object {
        var cekseawater: SeaWaterModel? = null

    }

    var currentDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_sea_water)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekseawater =
            gson.fromJson(
                intent.getStringExtra("cekseawater"),
                SeaWaterModel::class.java
            )
        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = currentDate

        binding.namak3.text = cekseawater!!.k3Nama
        binding.namaoperator.text = cekseawater!!.operatorNama
        binding.namaspv.text = cekseawater!!.supervisorNama
        if(cekseawater!!.supervisorTtd != null){
            binding.btnSpv.visibility = View.GONE
            binding.ttdspv.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-seawater/"}${cekseawater!!.supervisorTtd}").into(binding.ttdspv)
        }else{
            binding.btnSpv.visibility = View.VISIBLE
            binding.ttdspv.visibility = View.GONE
            binding.btnSpv.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "spv")
                bundle.putString("id", cekseawater!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdFfBlok1Fragment")
            }
        }
        if(cekseawater!!.k3Ttd != null){
            binding.btnK3.visibility = View.GONE
            binding.ttdk3.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-seawater/"}${cekseawater!!.k3Ttd}").into(binding.ttdk3)
        }else{
            binding.btnK3.visibility = View.VISIBLE
            binding.ttdk3.visibility = View.GONE
            binding.btnK3.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "k3")
                bundle.putString("id", cekseawater!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdFfBlok1Fragment")
            }
        }
        if(cekseawater!!.operatorTtd != null){
            binding.btnOperator.visibility = View.GONE
            binding.ttdoperator.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-seawater/"}${cekseawater!!.operatorTtd}").into(binding.ttdoperator)
        }else{
            binding.btnOperator.visibility = View.VISIBLE
            binding.ttdoperator.visibility = View.GONE
            binding.btnOperator.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "operator")
                bundle.putString("id", cekseawater!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdFfBlok1Fragment")
            }
        }


        if(cekseawater!!.isStatus == 1){
            binding.btnDraft.visibility = View.GONE
            binding.btnSubmit.visibility = View.GONE
        }else if(cekseawater!!.isStatus == 3){
            binding.btnDraft.visibility = View.GONE
        }
        if(cekseawater!!.shift != null){
            binding.txtshift.text = cekseawater!!.shift
        }else{
            binding.txtshift.text = sessionManager.getNama()
        }

        val low: Array<String> = resources.getStringArray(R.array.low)
        val openclose: Array<String> = resources.getStringArray(R.array.opncls)

        binding.mdwpsb.setText(cekseawater!!.mdWaktuPencatatanSebelumStart)
        binding.mdwpss.setText(cekseawater!!.mdWaktuPencatatanSesudahStart)
        binding.mdspsb.setText(cekseawater!!.mdSuctionPressSebelumStart)
        binding.mdspss.setText(cekseawater!!.mdSuctionPressSesudahStart)
        binding.mddpsb.setText(cekseawater!!.mdDischargePressSebelumStart)
        binding.mddpss.setText(cekseawater!!.mdDischargePressSesudahStart)
        if (cekseawater!!.mdLubeOilLevelSebelumStart != null) {
            binding.mdlolsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekseawater!!.mdLubeOilLevelSebelumStart }
            ))
        }
        if (cekseawater!!.mdLubeOilLevelSesudahStart != null) {
            binding.mdlolss.setSelection(low.indexOf(
                low.first { elem -> elem == cekseawater!!.mdLubeOilLevelSesudahStart }
            ))
        }


        binding.dewpsb.setText(cekseawater!!.deWaktuPencatatanSebelumStart)
        binding.dewpss.setText(cekseawater!!.deWaktuPencatatanSesudahStart)
        binding.deeohsb.setText(cekseawater!!.deEngineOperatingHoursSebelumStart)
        binding.deeohss.setText(cekseawater!!.deEngineOperatingHoursSesudahStart)
        binding.debisb.setText(cekseawater!!.deBatteryIIISebelumStart)
        binding.debiss.setText(cekseawater!!.deBatteryIIISesudahStart)
        binding.debiisb.setText(cekseawater!!.deBatteryIII2SebelumStart)
        binding.debiiss.setText(cekseawater!!.deBatteryIII2SesudahStart)
        if (cekseawater!!.deOilLevelDipstickSebelumStart != null) {
            binding.deoldss.setSelection(low.indexOf(
                low.first { elem -> elem == cekseawater!!.deOilLevelDipstickSebelumStart }
            ))
        }
        if (cekseawater!!.deOilLevelDipstickSesudahStart != null) {
            binding.deoldsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekseawater!!.deOilLevelDipstickSesudahStart }
            ))
        }

        binding.deflsb.setText(cekseawater!!.deFuelLevelSebelumStart)
        binding.deflss.setText(cekseawater!!.deFuelLevelSesudahStart)

        if (cekseawater!!.deEngineCoolantLevelSebelumStart != null) {
            binding.deeclsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekseawater!!.deEngineCoolantLevelSebelumStart }
            ))
        }
        if (cekseawater!!.deEngineCoolantLevelSesudahStart != null) {
            binding.deeclss.setSelection(low.indexOf(
                low.first { elem -> elem == cekseawater!!.deEngineCoolantLevelSesudahStart }
            ))
        }

        binding.deopsb.setText(cekseawater!!.deOilPressureSebelumStart)
        binding.deopss.setText(cekseawater!!.deOilPressureSesudahStart)

        if (cekseawater!!.deAirFilterSebelumStart != null) {
            binding.deafsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekseawater!!.deAirFilterSebelumStart }
            ))
        }
        if (cekseawater!!.deAirFilterSesudahStart != null) {
            binding.deafss.setSelection(low.indexOf(
                low.first { elem -> elem == cekseawater!!.deAirFilterSesudahStart }
            ))
        }

        if (cekseawater!!.deExthoustSystemSebelumStart != null) {
            binding.deessb.setSelection(low.indexOf(
                low.first { elem -> elem == cekseawater!!.deExthoustSystemSebelumStart }
            ))
        }
        if (cekseawater!!.deExthoustSystemSesudahStart != null) {
            binding.deesss.setSelection(low.indexOf(
                low.first { elem -> elem == cekseawater!!.deExthoustSystemSesudahStart }
            ))
        }
        binding.decwpsb.setText(cekseawater!!.deCoolingWaterPressSebelumStart)
        binding.decwpss.setText(cekseawater!!.deCoolingWaterPressSesudahStart)
        binding.dectsb.setText(cekseawater!!.deCoolantTemperatureSebelumStart)
        binding.dectss.setText(cekseawater!!.deCoolantTemperatureSesudahStart)
        binding.dessb.setText(cekseawater!!.deSpeedSebelumStart)
        binding.desss.setText(cekseawater!!.deSpeedSesudahStart)
        binding.despsb.setText(cekseawater!!.deSuctionPressSebelumStart)
        binding.despss.setText(cekseawater!!.deSuctionPressSesudahStart)
        binding.dedpsb.setText(cekseawater!!.deDischargePressSebelumStart)
        binding.dedpss.setText(cekseawater!!.deDischargePressSesudahStart)
        binding.deftsb.setText(cekseawater!!.deFuelTemperatureSebelumStart)
        binding.deftss.setText(cekseawater!!.deFuelTemperatureSesudahStart)
        binding.deetsb.setText(cekseawater!!.deEngineTorqueSebelumStart)
        binding.deetss.setText(cekseawater!!.deEngineTorqueSesudahStart)
        binding.deplsb.setText(cekseawater!!.dePersenLoadSebelumStart)
        binding.deplss.setText(cekseawater!!.dePersenLoadSesudahStart)
        binding.defswsb.setText(cekseawater!!.deFlowSeaWaterSebelumStart)
        binding.defswss.setText(cekseawater!!.deFlowSeaWaterSesudahStart)
        binding.depbpsb.setText(cekseawater!!.dePressByPassSebelumStart)
        binding.depbpss.setText(cekseawater!!.dePressByPassSesudahStart)

        binding.ket.setText(cekseawater!!.catatan)

        binding.btnSubmit.setOnClickListener {
            val md_waktu_pencatatan_sebelum_start = binding.mdwpsb.text.toString().trim()
            val md_waktu_pencatatan_sesudah_start = binding.mdwpss.text.toString().trim()
            val md_discharge_press_sebelum_start = binding.mddpsb.text.toString().trim()
            val md_discharge_press_sesudah_start = binding.mddpss.text.toString().trim()
            val md_suction_press_sebelum_start = binding.mdspsb.text.toString().trim()
            val md_suction_press_sesudah_start = binding.mdspss.text.toString().trim()
            val md_lube_oil_level_sebelum_start =
                binding.mdlolsb.selectedItem.toString().trim()
            val md_lube_oil_level_sesudah_start =
                binding.mdlolss.selectedItem.toString().trim()


            val de_waktu_pencatatan_sebelum_start = binding.dewpsb.text.toString().trim()
            val de_waktu_pencatatan_sesudah_start = binding.dewpss.text.toString().trim()
            val de_engine_operating_hours_sebelum_start =
                binding.deeohsb.text.toString().trim()
            val de_engine_operating_hours_sesudah_start =
                binding.deeohss.text.toString().trim()
            val de_battery_i_sebelum_start =
                binding.debisb.text.toString().trim()
            val de_battery_i_sesudah_start =
                binding.debiss.text.toString().trim()
            val de_battery_ii_sebelum_start =
                binding.debiisb.text.toString().trim()
            val de_battery_ii_sesudah_start =
                binding.debiiss.text.toString().trim()
            val de_oil_level_dipstick_sebelum_start =
                binding.deoldsb.selectedItem.toString().trim()
            val de_oil_level_dipstick_sesudah_start =
                binding.deoldss.selectedItem.toString().trim()
            val de_fuel_level_sebelum_start =
                binding.deflsb.text.toString().trim()
            val de_fuel_level_sesudah_start =
                binding.deflss.text.toString().trim()
            val de_engine_coolant_level_sebelum_start =
                binding.deeclsb.selectedItem.toString().trim()
            val de_engine_coolant_level_sesudah_start =
                binding.deeclss.selectedItem.toString().trim()
            val de_oil_pressure_sebelum_start =
                binding.deopsb.text.toString().trim()
            val de_oil_pressure_sesudah_start =
                binding.deopss.text.toString().trim()
            val de_air_filter_sebelum_start =
                binding.deafsb.selectedItem.toString().trim()
            val de_air_filter_sesudah_start =
                binding.deafss.selectedItem.toString().trim()
            val de_exthoust_system_sebelum_start =
                binding.deessb.selectedItem.toString().trim()
            val de_exthoust_system_sesudah_start =
                binding.deesss.selectedItem.toString().trim()

            val de_cooling_water_press_sebelum_start =
                binding.decwpsb.text.toString().trim()
            val de_cooling_water_press_sesudah_start =
                binding.decwpss.text.toString().trim()
            val de_coolant_temperature_sebelum_start =
                binding.dectsb.text.toString().trim()
            val de_coolant_temperature_sesudah_start =
                binding.dectss.text.toString().trim()
            val de_speed_sebelum_start = binding.dessb.text.toString().trim()
            val de_speed_sesudah_start = binding.desss.text.toString().trim()
            val de_suction_press_sebelum_start =
                binding.despsb.text.toString().trim()
            val de_suction_press_sesudah_start =
                binding.despss.text.toString().trim()
            val de_discharge_press_sebelum_start =
                binding.dedpsb.text.toString().trim()
            val de_discharge_press_sesudah_start =
                binding.dedpss.text.toString().trim()
            val de_fuel_temperature_sebelum_start =
                binding.deftsb.text.toString().trim()
            val de_fuel_temperature_sesudah_start =
                binding.deftss.text.toString().trim()
            val de_engine_torque_sebelum_start =
                binding.deetsb.text.toString().trim()
            val de_engine_torque_sesudah_start =
                binding.deetss.text.toString().trim()
            val de_persen_load_sebelum_start =
                binding.deplsb.text.toString().trim()
            val de_persen_load_sesudah_start =
                binding.deplss.text.toString().trim()
            val de_flow_sea_water_sebelum_start =
                binding.defswsb.text.toString().trim()
            val de_flow_sea_water_sesudah_start =
                binding.defswss.text.toString().trim()
            val de_press_by_press_sebelum_start =
                binding.depbpsb.text.toString().trim()
            val de_press_by_press_sesudah_start =
                binding.depbpss.text.toString().trim()

            val catatan = binding.ket.text.toString().trim()

            if (md_waktu_pencatatan_sebelum_start.isNotEmpty() &&
                md_waktu_pencatatan_sesudah_start.isNotEmpty() &&
                md_discharge_press_sebelum_start.isNotEmpty() &&
                md_discharge_press_sesudah_start.isNotEmpty() &&
                de_waktu_pencatatan_sebelum_start.isNotEmpty() &&
                de_waktu_pencatatan_sesudah_start.isNotEmpty() &&
                de_engine_operating_hours_sebelum_start.isNotEmpty() &&
                de_engine_operating_hours_sesudah_start.isNotEmpty() &&
                de_oil_pressure_sebelum_start.isNotEmpty() &&
                de_oil_pressure_sesudah_start.isNotEmpty() &&
                de_coolant_temperature_sebelum_start.isNotEmpty() &&
                de_coolant_temperature_sesudah_start.isNotEmpty() &&
                de_speed_sebelum_start.isNotEmpty() &&
                de_speed_sesudah_start.isNotEmpty() &&
                de_suction_press_sebelum_start.isNotEmpty() &&
                de_suction_press_sesudah_start.isNotEmpty() &&
                de_discharge_press_sebelum_start.isNotEmpty() &&
                de_discharge_press_sesudah_start.isNotEmpty() &&
                catatan.isNotEmpty()
            ) {
                loading(true)

                val body_md_waktu_pencatatan_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    md_waktu_pencatatan_sebelum_start
                )
                val body_md_waktu_pencatatan_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    md_waktu_pencatatan_sesudah_start
                )

                val body_md_suction_press_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    md_suction_press_sebelum_start
                )
                val body_md_suction_press_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    md_suction_press_sesudah_start
                )

                val body_md_discharge_press_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    md_discharge_press_sebelum_start
                )
                val body_md_discharge_press_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    md_discharge_press_sesudah_start
                )

                val body_md_lube_oil_level_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    md_lube_oil_level_sebelum_start
                )
                val body_md_lube_oil_level_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    md_lube_oil_level_sesudah_start
                )

                val body_de_waktu_pencatatan_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_waktu_pencatatan_sebelum_start
                )
                val body_de_waktu_pencatatan_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_waktu_pencatatan_sesudah_start
                )
                val body_de_engine_operating_hours_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_engine_operating_hours_sebelum_start
                )
                val body_de_engine_operating_hours_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_engine_operating_hours_sesudah_start
                )
                val body_de_battery_i_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_battery_i_sebelum_start
                )
                val body_de_battery_i_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_battery_i_sesudah_start
                )
                val body_de_battery_ii_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_battery_ii_sebelum_start
                )
                val body_de_battery_ii_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_battery_ii_sesudah_start
                )

                val body_de_oil_level_dipstick_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_oil_level_dipstick_sebelum_start
                )
                val body_de_oil_level_dipstick_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_oil_level_dipstick_sesudah_start
                )

                val body_de_fuel_level_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_fuel_level_sebelum_start
                )
                val body_de_fuel_level_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_fuel_level_sesudah_start
                )

                val body_de_engine_coolant_level_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_engine_coolant_level_sebelum_start
                )
                val body_de_engine_coolant_level_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_engine_coolant_level_sesudah_start
                )

                val body_de_oil_pressure_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_oil_pressure_sebelum_start
                )
                val body_de_oil_pressure_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_oil_pressure_sesudah_start
                )

                val body_de_air_filter_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_air_filter_sebelum_start
                )
                val body_de_air_filter_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_air_filter_sesudah_start
                )

                val body_de_exthoust_system_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_exthoust_system_sebelum_start
                )
                val body_de_exthoust_system_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_exthoust_system_sesudah_start
                )

                val body_de_cooling_water_press_sebelum_start: RequestBody =
                    RequestBody.create(
                        MediaType.parse("text/plain"),
                        de_cooling_water_press_sebelum_start
                    )
                val body_de_cooling_water_press_sesudah_start: RequestBody =
                    RequestBody.create(
                        MediaType.parse("text/plain"),
                        de_cooling_water_press_sesudah_start
                    )
                val body_de_coolant_temperature_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_coolant_temperature_sebelum_start
                )
                val body_de_coolant_temperature_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_coolant_temperature_sesudah_start
                )
                val body_de_speed_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_speed_sebelum_start
                )
                val body_de_speed_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_speed_sesudah_start
                )
                val body_de_suction_press_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_suction_press_sebelum_start
                )
                val body_de_suction_press_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_suction_press_sesudah_start
                )
                val body_de_discharge_press_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_discharge_press_sebelum_start
                )
                val body_de_discharge_press_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_discharge_press_sesudah_start
                )
                val body_de_fuel_temperature_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_fuel_temperature_sebelum_start
                )

                val body_de_fuel_temperature_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_fuel_temperature_sesudah_start
                )

                val body_de_engine_torque_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_engine_torque_sebelum_start
                )

                val body_de_engine_torque_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_engine_torque_sesudah_start
                )
                val body_de_persen_load_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_persen_load_sebelum_start
                )

                val body_de_persen_load_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_persen_load_sesudah_start
                )

                val body_de_flow_sea_water_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_flow_sea_water_sebelum_start
                )

                val body_de_flow_sea_water_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_flow_sea_water_sesudah_start
                )

                val body_de_press_by_press_sebelum_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_press_by_press_sebelum_start
                )

                val body_de_press_by_press_sesudah_start: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_press_by_press_sesudah_start
                )

                val body_catatan: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    catatan
                )

                val body_is_status: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"), "1"
                )

                val body_tahun: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    cekseawater!!.tahun
                )
                val body_id: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    "${cekseawater!!.id}"
                )
                val body_tgl_pemeriksa: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    currentDate
                )
                val body_tw: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    cekseawater!!.tw
                )
                val body_shift: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    sessionManager.getNama().toString()
                )
                api.update_seawater(
                    body_id,
                    body_tw,
                    body_tahun,
                    body_shift,
                    body_is_status,
                    body_tgl_pemeriksa,
                    body_md_waktu_pencatatan_sebelum_start,
                    body_md_waktu_pencatatan_sesudah_start,
                    body_md_discharge_press_sebelum_start,
                    body_md_discharge_press_sesudah_start,
                    body_md_suction_press_sebelum_start,
                    body_md_suction_press_sesudah_start,
                    body_md_lube_oil_level_sebelum_start,
                    body_md_lube_oil_level_sesudah_start,
                    body_de_waktu_pencatatan_sebelum_start,
                    body_de_waktu_pencatatan_sesudah_start,
                    body_de_engine_operating_hours_sebelum_start,
                    body_de_engine_operating_hours_sesudah_start,
                    body_de_battery_i_sebelum_start,
                    body_de_battery_i_sesudah_start,
                    body_de_battery_ii_sebelum_start,
                    body_de_battery_ii_sesudah_start,
                    body_de_oil_level_dipstick_sebelum_start,
                    body_de_oil_level_dipstick_sesudah_start,
                    body_de_fuel_level_sebelum_start,
                    body_de_fuel_level_sesudah_start,
                    body_de_engine_coolant_level_sebelum_start,
                    body_de_engine_coolant_level_sesudah_start,
                    body_de_oil_pressure_sebelum_start,
                    body_de_oil_pressure_sesudah_start,
                    body_de_air_filter_sebelum_start,
                    body_de_air_filter_sesudah_start,
                    body_de_exthoust_system_sebelum_start,
                    body_de_exthoust_system_sesudah_start,
                    body_de_cooling_water_press_sebelum_start,
                    body_de_cooling_water_press_sesudah_start,
                    body_de_coolant_temperature_sebelum_start,
                    body_de_coolant_temperature_sesudah_start,
                    body_de_speed_sebelum_start,
                    body_de_speed_sesudah_start,
                    body_de_suction_press_sebelum_start,
                    body_de_suction_press_sesudah_start,
                    body_de_discharge_press_sebelum_start,
                    body_de_discharge_press_sesudah_start,
                    body_de_fuel_temperature_sebelum_start,
                    body_de_fuel_temperature_sesudah_start,
                    body_de_engine_torque_sebelum_start,
                    body_de_engine_torque_sesudah_start,
                    body_de_persen_load_sebelum_start,
                    body_de_persen_load_sesudah_start,
                    body_de_flow_sea_water_sebelum_start,
                    body_de_flow_sea_water_sesudah_start,
                    body_de_press_by_press_sebelum_start,
                    body_de_press_by_press_sesudah_start,
                    body_catatan

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
            val md_waktu_pencatatan_sebelum_start = binding.mdwpsb.text.toString().trim()
            val md_waktu_pencatatan_sesudah_start = binding.mdwpss.text.toString().trim()
            val md_discharge_press_sebelum_start = binding.mddpsb.text.toString().trim()
            val md_discharge_press_sesudah_start = binding.mddpss.text.toString().trim()
            val md_suction_press_sebelum_start = binding.mdspsb.text.toString().trim()
            val md_suction_press_sesudah_start = binding.mdspss.text.toString().trim()
            val md_lube_oil_level_sebelum_start =
                binding.mdlolsb.selectedItem.toString().trim()
            val md_lube_oil_level_sesudah_start =
                binding.mdlolss.selectedItem.toString().trim()


            val de_waktu_pencatatan_sebelum_start = binding.dewpsb.text.toString().trim()
            val de_waktu_pencatatan_sesudah_start = binding.dewpss.text.toString().trim()
            val de_engine_operating_hours_sebelum_start =
                binding.deeohsb.text.toString().trim()
            val de_engine_operating_hours_sesudah_start =
                binding.deeohss.text.toString().trim()
            val de_battery_i_sebelum_start =
                binding.debisb.text.toString().trim()
            val de_battery_i_sesudah_start =
                binding.debiss.text.toString().trim()
            val de_battery_ii_sebelum_start =
                binding.debiisb.text.toString().trim()
            val de_battery_ii_sesudah_start =
                binding.debiiss.text.toString().trim()
            val de_oil_level_dipstick_sebelum_start =
                binding.deoldsb.selectedItem.toString().trim()
            val de_oil_level_dipstick_sesudah_start =
                binding.deoldss.selectedItem.toString().trim()
            val de_fuel_level_sebelum_start =
                binding.deflsb.text.toString().trim()
            val de_fuel_level_sesudah_start =
                binding.deflss.text.toString().trim()
            val de_engine_coolant_level_sebelum_start =
                binding.deeclsb.selectedItem.toString().trim()
            val de_engine_coolant_level_sesudah_start =
                binding.deeclss.selectedItem.toString().trim()
            val de_oil_pressure_sebelum_start =
                binding.deopsb.text.toString().trim()
            val de_oil_pressure_sesudah_start =
                binding.deopss.text.toString().trim()
            val de_air_filter_sebelum_start =
                binding.deafsb.selectedItem.toString().trim()
            val de_air_filter_sesudah_start =
                binding.deafss.selectedItem.toString().trim()
            val de_exthoust_system_sebelum_start =
                binding.deessb.selectedItem.toString().trim()
            val de_exthoust_system_sesudah_start =
                binding.deesss.selectedItem.toString().trim()

            val de_cooling_water_press_sebelum_start =
                binding.decwpsb.text.toString().trim()
            val de_cooling_water_press_sesudah_start =
                binding.decwpss.text.toString().trim()
            val de_coolant_temperature_sebelum_start =
                binding.dectsb.text.toString().trim()
            val de_coolant_temperature_sesudah_start =
                binding.dectss.text.toString().trim()
            val de_speed_sebelum_start = binding.dessb.text.toString().trim()
            val de_speed_sesudah_start = binding.desss.text.toString().trim()
            val de_suction_press_sebelum_start =
                binding.despsb.text.toString().trim()
            val de_suction_press_sesudah_start =
                binding.despss.text.toString().trim()
            val de_discharge_press_sebelum_start =
                binding.dedpsb.text.toString().trim()
            val de_discharge_press_sesudah_start =
                binding.dedpss.text.toString().trim()
            val de_fuel_temperature_sebelum_start =
                binding.deftsb.text.toString().trim()
            val de_fuel_temperature_sesudah_start =
                binding.deftss.text.toString().trim()
            val de_engine_torque_sebelum_start =
                binding.deetsb.text.toString().trim()
            val de_engine_torque_sesudah_start =
                binding.deetss.text.toString().trim()
            val de_persen_load_sebelum_start =
                binding.deplsb.text.toString().trim()
            val de_persen_load_sesudah_start =
                binding.deplss.text.toString().trim()
            val de_flow_sea_water_sebelum_start =
                binding.defswsb.text.toString().trim()
            val de_flow_sea_water_sesudah_start =
                binding.defswss.text.toString().trim()
            val de_press_by_press_sebelum_start =
                binding.depbpsb.text.toString().trim()
            val de_press_by_press_sesudah_start =
                binding.depbpss.text.toString().trim()

            val catatan = binding.ket.text.toString().trim()

                loading(true)

            val body_md_waktu_pencatatan_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                md_waktu_pencatatan_sebelum_start
            )
            val body_md_waktu_pencatatan_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                md_waktu_pencatatan_sesudah_start
            )

            val body_md_suction_press_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                md_suction_press_sebelum_start
            )
            val body_md_suction_press_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                md_suction_press_sesudah_start
            )

            val body_md_discharge_press_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                md_discharge_press_sebelum_start
            )
            val body_md_discharge_press_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                md_discharge_press_sesudah_start
            )

            val body_md_lube_oil_level_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                md_lube_oil_level_sebelum_start
            )
            val body_md_lube_oil_level_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                md_lube_oil_level_sesudah_start
            )

            val body_de_waktu_pencatatan_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_waktu_pencatatan_sebelum_start
            )
            val body_de_waktu_pencatatan_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_waktu_pencatatan_sesudah_start
            )
            val body_de_engine_operating_hours_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_engine_operating_hours_sebelum_start
            )
            val body_de_engine_operating_hours_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_engine_operating_hours_sesudah_start
            )
            val body_de_battery_i_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_i_sebelum_start
            )
            val body_de_battery_i_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_i_sesudah_start
            )
            val body_de_battery_ii_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_ii_sebelum_start
            )
            val body_de_battery_ii_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_battery_ii_sesudah_start
            )

            val body_de_oil_level_dipstick_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_oil_level_dipstick_sebelum_start
            )
            val body_de_oil_level_dipstick_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_oil_level_dipstick_sesudah_start
            )

            val body_de_fuel_level_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_fuel_level_sebelum_start
            )
            val body_de_fuel_level_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_fuel_level_sesudah_start
            )

            val body_de_engine_coolant_level_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_engine_coolant_level_sebelum_start
            )
            val body_de_engine_coolant_level_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_engine_coolant_level_sesudah_start
            )

            val body_de_oil_pressure_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_oil_pressure_sebelum_start
            )
            val body_de_oil_pressure_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_oil_pressure_sesudah_start
            )

            val body_de_air_filter_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_air_filter_sebelum_start
            )
            val body_de_air_filter_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_air_filter_sesudah_start
            )

            val body_de_exthoust_system_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_exthoust_system_sebelum_start
            )
            val body_de_exthoust_system_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_exthoust_system_sesudah_start
            )

            val body_de_cooling_water_press_sebelum_start: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_cooling_water_press_sebelum_start
                )
            val body_de_cooling_water_press_sesudah_start: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    de_cooling_water_press_sesudah_start
                )
            val body_de_coolant_temperature_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_coolant_temperature_sebelum_start
            )
            val body_de_coolant_temperature_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_coolant_temperature_sesudah_start
            )
            val body_de_speed_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_speed_sebelum_start
            )
            val body_de_speed_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_speed_sesudah_start
            )
            val body_de_suction_press_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_suction_press_sebelum_start
            )
            val body_de_suction_press_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_suction_press_sesudah_start
            )
            val body_de_discharge_press_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_discharge_press_sebelum_start
            )
            val body_de_discharge_press_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_discharge_press_sesudah_start
            )
            val body_de_fuel_temperature_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_fuel_temperature_sebelum_start
            )

            val body_de_fuel_temperature_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_fuel_temperature_sesudah_start
            )

            val body_de_engine_torque_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_engine_torque_sebelum_start
            )

            val body_de_engine_torque_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_engine_torque_sesudah_start
            )
            val body_de_persen_load_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_persen_load_sebelum_start
            )

            val body_de_persen_load_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_persen_load_sesudah_start
            )

            val body_de_flow_sea_water_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_flow_sea_water_sebelum_start
            )

            val body_de_flow_sea_water_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_flow_sea_water_sesudah_start
            )

            val body_de_press_by_press_sebelum_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_press_by_press_sebelum_start
            )

            val body_de_press_by_press_sesudah_start: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                de_press_by_press_sesudah_start
            )

            val body_catatan: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                catatan
            )

            val body_is_status: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), "${cekseawater!!.isStatus}"
            )

            val body_tahun: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekseawater!!.tahun
            )
            val body_id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekseawater!!.id}"
            )
            val body_tgl_pemeriksa: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                currentDate
            )
            val body_tw: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekseawater!!.tw
            )
            val body_shift: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                sessionManager.getNama().toString()
            )
                api.update_seawater(
                    body_id,
                    body_tw,
                    body_tahun,
                    body_shift,
                    body_is_status,
                    body_tgl_pemeriksa,
                    body_md_waktu_pencatatan_sebelum_start,
                    body_md_waktu_pencatatan_sesudah_start,
                    body_md_discharge_press_sebelum_start,
                    body_md_discharge_press_sesudah_start,
                    body_md_suction_press_sebelum_start,
                    body_md_suction_press_sesudah_start,
                    body_md_lube_oil_level_sebelum_start,
                    body_md_lube_oil_level_sesudah_start,
                    body_de_waktu_pencatatan_sebelum_start,
                    body_de_waktu_pencatatan_sesudah_start,
                    body_de_engine_operating_hours_sebelum_start,
                    body_de_engine_operating_hours_sesudah_start,
                    body_de_battery_i_sebelum_start,
                    body_de_battery_i_sesudah_start,
                    body_de_battery_ii_sebelum_start,
                    body_de_battery_ii_sesudah_start,
                    body_de_oil_level_dipstick_sebelum_start,
                    body_de_oil_level_dipstick_sesudah_start,
                    body_de_fuel_level_sebelum_start,
                    body_de_fuel_level_sesudah_start,
                    body_de_engine_coolant_level_sebelum_start,
                    body_de_engine_coolant_level_sesudah_start,
                    body_de_oil_pressure_sebelum_start,
                    body_de_oil_pressure_sesudah_start,
                    body_de_air_filter_sebelum_start,
                    body_de_air_filter_sesudah_start,
                    body_de_exthoust_system_sebelum_start,
                    body_de_exthoust_system_sesudah_start,
                    body_de_cooling_water_press_sebelum_start,
                    body_de_cooling_water_press_sesudah_start,
                    body_de_coolant_temperature_sebelum_start,
                    body_de_coolant_temperature_sesudah_start,
                    body_de_speed_sebelum_start,
                    body_de_speed_sesudah_start,
                    body_de_suction_press_sebelum_start,
                    body_de_suction_press_sesudah_start,
                    body_de_discharge_press_sebelum_start,
                    body_de_discharge_press_sesudah_start,
                    body_de_fuel_temperature_sebelum_start,
                    body_de_fuel_temperature_sesudah_start,
                    body_de_engine_torque_sebelum_start,
                    body_de_engine_torque_sesudah_start,
                    body_de_persen_load_sebelum_start,
                    body_de_persen_load_sesudah_start,
                    body_de_flow_sea_water_sebelum_start,
                    body_de_flow_sea_water_sesudah_start,
                    body_de_press_by_press_sebelum_start,
                    body_de_press_by_press_sesudah_start,
                    body_catatan

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
                                toast("Disimpan sebagai draft")
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
        cekseawater = null
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