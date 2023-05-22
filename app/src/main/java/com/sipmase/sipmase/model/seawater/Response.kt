package com.sipmase.sipmase.model.seawater

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("md_discharge_press_sebelum_start")
	val mdDischargePressSebelumStart: Any? = null,

	@field:SerializedName("de_cooling_water_press_sesudah_start")
	val deCoolingWaterPressSesudahStart: Any? = null,

	@field:SerializedName("md_suction_press_sesudah_start")
	val mdSuctionPressSesudahStart: Any? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("md_discharge_press_sesudah_start")
	val mdDischargePressSesudahStart: Any? = null,

	@field:SerializedName("md_suction_press_sebelum_start")
	val mdSuctionPressSebelumStart: Any? = null,

	@field:SerializedName("shift")
	val shift: Any? = null,

	@field:SerializedName("de_persen_load_sesudah_start")
	val dePersenLoadSesudahStart: Any? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: Any? = null,

	@field:SerializedName("de_cooling_water_press_sebelum_start")
	val deCoolingWaterPressSebelumStart: Any? = null,

	@field:SerializedName("de_persen_load_sebelum_start")
	val dePersenLoadSebelumStart: Any? = null,

	@field:SerializedName("de_engine_coolant_level_sebelum_start")
	val deEngineCoolantLevelSebelumStart: Any? = null,

	@field:SerializedName("de_exthoust_system_sesudah_start")
	val deExthoustSystemSesudahStart: Any? = null,

	@field:SerializedName("de_coolant_temperature_sesudah_start")
	val deCoolantTemperatureSesudahStart: Any? = null,

	@field:SerializedName("k3_nama")
	val k3Nama: Any? = null,

	@field:SerializedName("de_exthoust_system_sebelum_start")
	val deExthoustSystemSebelumStart: Any? = null,

	@field:SerializedName("de_waktu_pencatatan_sebelum_start")
	val deWaktuPencatatanSebelumStart: Any? = null,

	@field:SerializedName("de_coolant_temperature_sebelum_start")
	val deCoolantTemperatureSebelumStart: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("de_battery_I_II_2_sesudah_start")
	val deBatteryIII2SesudahStart: Any? = null,

	@field:SerializedName("de_oil_level_dipstick_sesudah_start")
	val deOilLevelDipstickSesudahStart: Any? = null,

	@field:SerializedName("de_battery_I_II_sebelum_start")
	val deBatteryIIISebelumStart: Any? = null,

	@field:SerializedName("de_flow_sea_water_sesudah_start")
	val deFlowSeaWaterSesudahStart: Any? = null,

	@field:SerializedName("md_lube_oil_level_sebelum_start")
	val mdLubeOilLevelSebelumStart: Any? = null,

	@field:SerializedName("de_press_by_pass_sesudah_start")
	val dePressByPassSesudahStart: Any? = null,

	@field:SerializedName("de_flow_sea_water_sebelum_start")
	val deFlowSeaWaterSebelumStart: Any? = null,

	@field:SerializedName("de_battery_I_II_2_sebelum_start")
	val deBatteryIII2SebelumStart: Any? = null,

	@field:SerializedName("de_speed_sebelum_start")
	val deSpeedSebelumStart: Any? = null,

	@field:SerializedName("de_discharge_press_sebelum_start")
	val deDischargePressSebelumStart: Any? = null,

	@field:SerializedName("hari")
	val hari: String? = null,

	@field:SerializedName("operator_ttd")
	val operatorTtd: Any? = null,

	@field:SerializedName("tanggal_pemeriksa")
	val tanggalPemeriksa: String? = null,

	@field:SerializedName("de_air_filter_sesudah_start")
	val deAirFilterSesudahStart: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("de_air_filter_sebelum_start")
	val deAirFilterSebelumStart: Any? = null,

	@field:SerializedName("de_oil_pressure_sebelum_start")
	val deOilPressureSebelumStart: Any? = null,

	@field:SerializedName("de_press_by_pass_sebelum_start")
	val dePressByPassSebelumStart: Any? = null,

	@field:SerializedName("de_discharge_press_sesudah_start")
	val deDischargePressSesudahStart: Any? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("de_suction_press_sebelum_start")
	val deSuctionPressSebelumStart: Any? = null,

	@field:SerializedName("de_oil_pressure_sesudah_start")
	val deOilPressureSesudahStart: Any? = null,

	@field:SerializedName("de_fuel_temperature_sesudah_start")
	val deFuelTemperatureSesudahStart: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("de_speed_sesudah_start")
	val deSpeedSesudahStart: Any? = null,

	@field:SerializedName("supervisor_ttd")
	val supervisorTtd: Any? = null,

	@field:SerializedName("de_engine_operating_hours_sebelum_start")
	val deEngineOperatingHoursSebelumStart: Any? = null,

	@field:SerializedName("md_lube_oil_level_sesudah_start")
	val mdLubeOilLevelSesudahStart: Any? = null,

	@field:SerializedName("de_battery_I_II_sesudah_start")
	val deBatteryIIISesudahStart: Any? = null,

	@field:SerializedName("de_engine_torque_sesudah_start")
	val deEngineTorqueSesudahStart: Any? = null,

	@field:SerializedName("k3_ttd")
	val k3Ttd: Any? = null,

	@field:SerializedName("de_fuel_level_sebelum_start")
	val deFuelLevelSebelumStart: Any? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("de_engine_operating_hours_sesudah_start")
	val deEngineOperatingHoursSesudahStart: Any? = null,

	@field:SerializedName("catatan")
	val catatan: Any? = null,

	@field:SerializedName("de_fuel_level_sesudah_start")
	val deFuelLevelSesudahStart: Any? = null,

	@field:SerializedName("md_waktu_pencatatan_sesudah_start")
	val mdWaktuPencatatanSesudahStart: Any? = null,

	@field:SerializedName("md_waktu_pencatatan_sebelum_start")
	val mdWaktuPencatatanSebelumStart: Any? = null,

	@field:SerializedName("de_oil_level_dipstick_sebelum_start")
	val deOilLevelDipstickSebelumStart: Any? = null,

	@field:SerializedName("de_engine_torque_sebelum_start")
	val deEngineTorqueSebelumStart: Any? = null,

	@field:SerializedName("operator_nama")
	val operatorNama: Any? = null,

	@field:SerializedName("supervisor_nama")
	val supervisorNama: Any? = null,

	@field:SerializedName("de_fuel_temperature_sebelum_start")
	val deFuelTemperatureSebelumStart: Any? = null,

	@field:SerializedName("de_engine_coolant_level_sesudah_start")
	val deEngineCoolantLevelSesudahStart: Any? = null,

	@field:SerializedName("de_suction_press_sesudah_start")
	val deSuctionPressSesudahStart: Any? = null,

	@field:SerializedName("de_waktu_pencatatan_sesudah_start")
	val deWaktuPencatatanSesudahStart: Any? = null
)
