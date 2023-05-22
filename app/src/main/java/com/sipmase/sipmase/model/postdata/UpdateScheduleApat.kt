package com.sipmase.sipmase.model.postdata

import com.google.gson.annotations.SerializedName

data class UpdateScheduleApat(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("ember")
	val ember: String? = null,

	@field:SerializedName("gantungan")
	val gantungan: String? = null,

	@field:SerializedName("shift")
	val shift: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null,

	@field:SerializedName("bak")
	val bak: String? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("karung")
	val karung: String? = null,

	@field:SerializedName("pasir")
	val pasir: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("sekop")
	val sekop: String? = null,

	@field:SerializedName("tanggal_pemeriksa")
	val tanggalPemeriksa: String? = null,
)
