package com.sipmase.sipmase.model.apat

import com.google.gson.annotations.SerializedName

data class ScheduleApatResponse(

	@field:SerializedName("data")
	val data: List<ScheduleApatModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ScheduleApatModel(

	@field:SerializedName("keterangan")
	val keterangan: Any? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("ember")
	val ember: Any? = null,

	@field:SerializedName("gantungan")
	val gantungan: Any? = null,

	@field:SerializedName("shift")
	val shift: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null,

	@field:SerializedName("bak")
	val bak: Any? = null,

	@field:SerializedName("kode_apat")
	val kodeApat: String? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("karung")
	val karung: Any? = null,

	@field:SerializedName("pasir")
	val pasir: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("lokasi")
	val lokasi: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("sekop")
	val sekop: Any? = null
)
