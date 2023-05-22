package com.sipmase.sipmase.model.hydrant

import com.google.gson.annotations.SerializedName

data class ScheduleHydrantPelaksanaResponse(

	@field:SerializedName("data")
	val data: List<ScheduleHydrantPelaksanaModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ScheduleHydrantPelaksanaModel(

	@field:SerializedName("noozle")
	val noozle: Any? = null,

	@field:SerializedName("keterangan")
	val keterangan: Any? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("kunci_box")
	val kunciBox: Any? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("shift")
	val shift: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null,

	@field:SerializedName("kode_hydrant")
	val kodeHydrant: String? = null,

	@field:SerializedName("main_valve")
	val mainValve: Any? = null,

	@field:SerializedName("kunci_f")
	val kunciF: Any? = null,

	@field:SerializedName("hydrant")
	val hydrant: Hydrant? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("house_keeping")
	val houseKeeping: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("discharge")
	val discharge: Any? = null,

	@field:SerializedName("kondisi_box")
	val kondisiBox: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("flushing")
	val flushing: Any? = null,

	@field:SerializedName("selang")
	val selang: Any? = null
)

data class Hydrant(

	@field:SerializedName("no_box")
	val noBox: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("lokasi")
	val lokasi: String? = null,

	@field:SerializedName("kode")
	val kode: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
