package com.sipmase.sipmase.model.kebisingan

import com.google.gson.annotations.SerializedName

data class ScheduleKebisinganResponse(

	@field:SerializedName("data")
	val data: List<ScheduleKebisinganModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)


data class ScheduleKebisinganModel(

	@field:SerializedName("keterangan")
	val keterangan: Any? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("dbrata2")
	val dbrata2: Any? = null,

	@field:SerializedName("tanggal_pemeriksa")
	val tanggalPemeriksa: Any? = null,

	@field:SerializedName("shift")
	val shift: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null,

	@field:SerializedName("nab_kebisingan")
	val nabKebisingan: Any? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("kebisingan")
	val kebisingan: KebisinganModel? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("dbx2")
	val dbx2: Any? = null,

	@field:SerializedName("dbx3")
	val dbx3: Any? = null,

	@field:SerializedName("kode_kebisingan")
	val kodeKebisingan: String? = null,

	@field:SerializedName("dbx1")
	val dbx1: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: Any? = null
)
