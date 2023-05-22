package com.sipmase.sipmase.model.seawater

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UpdateSeaWater(

	@field:SerializedName("md_discharge_press_sebelum_start")
	val mdDischargePressSebelumStart: String? = null,

	@field:SerializedName("de_cooling_water_press_sesudah_start")
	val deCoolingWaterPressSesudahStart: String? = null,

	@field:SerializedName("md_suction_press_sesudah_start")
	val mdSuctionPressSesudahStart: String? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("md_discharge_press_sesudah_start")
	val mdDischargePressSesudahStart: String? = null,

	@field:SerializedName("md_suction_press_sebelum_start")
	val mdSuctionPressSebelumStart: String? = null,

	@field:SerializedName("shift")
	val shift: String? = null,

	@field:SerializedName("de_persen_load_sesudah_start")
	val dePersenLoadSesudahStart: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null,

	@field:SerializedName("de_cooling_water_press_sebelum_start")
	val deCoolingWaterPressSebelumStart: String? = null,

	@field:SerializedName("de_persen_load_sebelum_start")
	val dePersenLoadSebelumStart: String? = null,

	@field:SerializedName("de_engine_coolant_level_sebelum_start")
	val deEngineCoolantLevelSebelumStart: String? = null,

	@field:SerializedName("de_exthoust_system_sesudah_start")
	val deExthoustSystemSesudahStart: String? = null,

	@field:SerializedName("de_coolant_temperature_sesudah_start")
	val deCoolantTemperatureSesudahStart: String? = null,

	@field:SerializedName("k3_nama")
	val k3Nama: String? = null,

	@field:SerializedName("de_exthoust_system_sebelum_start")
	val deExthoustSystemSebelumStart: String? = null,

	@field:SerializedName("de_waktu_pencatatan_sebelum_start")
	val deWaktuPencatatanSebelumStart: String? = null,

	@field:SerializedName("de_coolant_temperature_sebelum_start")
	val deCoolantTemperatureSebelumStart: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("de_battery_I_II_2_sesudah_start")
	val deBatteryIII2SesudahStart: String? = null,

	@field:SerializedName("de_oil_level_dipstick_sesudah_start")
	val deOilLevelDipstickSesudahStart: String? = null,

	@field:SerializedName("de_battery_I_II_sebelum_start")
	val deBatteryIIISebelumStart: String? = null,

	@field:SerializedName("de_flow_sea_water_sesudah_start")
	val deFlowSeaWaterSesudahStart: String? = null,

	@field:SerializedName("md_lube_oil_level_sebelum_start")
	val mdLubeOilLevelSebelumStart: String? = null,

	@field:SerializedName("de_press_by_pass_sesudah_start")
	val dePressByPassSesudahStart: String? = null,

	@field:SerializedName("de_flow_sea_water_sebelum_start")
	val deFlowSeaWaterSebelumStart: String? = null,

	@field:SerializedName("de_battery_I_II_2_sebelum_start")
	val deBatteryIII2SebelumStart: String? = null,

	@field:SerializedName("de_speed_sebelum_start")
	val deSpeedSebelumStart: String? = null,

	@field:SerializedName("de_discharge_press_sebelum_start")
	val deDischargePressSebelumStart: String? = null,

	@field:SerializedName("hari")
	val hari: String? = null,

	@field:SerializedName("operator_ttd")
	val operatorTtd: String? = null,

	@field:SerializedName("tanggal_pemeriksa")
	val tanggalPemeriksa: String? = null,

	@field:SerializedName("de_air_filter_sesudah_start")
	val deAirFilterSesudahStart: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("de_air_filter_sebelum_start")
	val deAirFilterSebelumStart: String? = null,

	@field:SerializedName("de_oil_pressure_sebelum_start")
	val deOilPressureSebelumStart: String? = null,

	@field:SerializedName("de_press_by_pass_sebelum_start")
	val dePressByPassSebelumStart: String? = null,

	@field:SerializedName("de_discharge_press_sesudah_start")
	val deDischargePressSesudahStart: String? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("de_suction_press_sebelum_start")
	val deSuctionPressSebelumStart: String? = null,

	@field:SerializedName("de_oil_pressure_sesudah_start")
	val deOilPressureSesudahStart: String? = null,

	@field:SerializedName("de_fuel_temperature_sesudah_start")
	val deFuelTemperatureSesudahStart: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("de_speed_sesudah_start")
	val deSpeedSesudahStart: String? = null,

	@field:SerializedName("supervisor_ttd")
	val supervisorTtd: String? = null,

	@field:SerializedName("de_engine_operating_hours_sebelum_start")
	val deEngineOperatingHoursSebelumStart: String? = null,

	@field:SerializedName("md_lube_oil_level_sesudah_start")
	val mdLubeOilLevelSesudahStart: String? = null,

	@field:SerializedName("de_battery_I_II_sesudah_start")
	val deBatteryIIISesudahStart: String? = null,

	@field:SerializedName("de_engine_torque_sesudah_start")
	val deEngineTorqueSesudahStart: String? = null,

	@field:SerializedName("k3_ttd")
	val k3Ttd: String? = null,

	@field:SerializedName("de_fuel_level_sebelum_start")
	val deFuelLevelSebelumStart: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("de_engine_operating_hours_sesudah_start")
	val deEngineOperatingHoursSesudahStart: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("de_fuel_level_sesudah_start")
	val deFuelLevelSesudahStart: String? = null,

	@field:SerializedName("md_waktu_pencatatan_sesudah_start")
	val mdWaktuPencatatanSesudahStart: String? = null,

	@field:SerializedName("md_waktu_pencatatan_sebelum_start")
	val mdWaktuPencatatanSebelumStart: String? = null,

	@field:SerializedName("de_oil_level_dipstick_sebelum_start")
	val deOilLevelDipstickSebelumStart: String? = null,

	@field:SerializedName("de_engine_torque_sebelum_start")
	val deEngineTorqueSebelumStart: String? = null,

	@field:SerializedName("operator_nama")
	val operatorNama: String? = null,

	@field:SerializedName("supervisor_nama")
	val supervisorNama: String? = null,

	@field:SerializedName("de_fuel_temperature_sebelum_start")
	val deFuelTemperatureSebelumStart: String? = null,

	@field:SerializedName("de_engine_coolant_level_sesudah_start")
	val deEngineCoolantLevelSesudahStart: String? = null,

	@field:SerializedName("de_suction_press_sesudah_start")
	val deSuctionPressSesudahStart: String? = null,

	@field:SerializedName("de_waktu_pencatatan_sesudah_start")
	val deWaktuPencatatanSesudahStart: String? = null
)
