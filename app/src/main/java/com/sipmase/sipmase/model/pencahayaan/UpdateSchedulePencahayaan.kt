package com.sipmase.sipmase.model.pencahayaan

import com.google.gson.annotations.SerializedName

data class UpdateSchedulePencahayaan(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("shift")
	val shift: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null,


	@field:SerializedName("lux1")
	val lux1: Int? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("lux2")
	val lux2: Int? = null,

	@field:SerializedName("lux3")
	val lux3: Int? = null,

//	@field:SerializedName("luxrata2")
//	val luxrata2: Float? = null,

	@field:SerializedName("sumber_pencahayaan")
	val sumberPencahayaan: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("tanggal_pemeriksa")
val tanggalPemeriksa: String? = null,
)
