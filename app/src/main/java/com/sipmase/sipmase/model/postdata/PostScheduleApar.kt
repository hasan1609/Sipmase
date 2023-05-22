package com.sipmase.sipmase.model.postdata

import com.google.gson.annotations.SerializedName

data class PostScheduleApar(

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("tuas")
	val tuas: String? = null,

	@field:SerializedName("rambu")
	val rambu: String? = null,

	@field:SerializedName("gantungan")
	val gantungan: String? = null,

	@field:SerializedName("tanggal_pemeriksa")
	val tanggalPemeriksa: String? = null,

	@field:SerializedName("shift")
	val shift: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null,

	@field:SerializedName("pressure")
	val pressure: String? = null,

	@field:SerializedName("tabung")
	val tabung: String? = null,

	@field:SerializedName("nozzle")
	val nozzle: String? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("pin")
	val pin: String? = null,

	@field:SerializedName("keterangan_tambahan")
	val keteranganTambahan: String? = null,

	@field:SerializedName("segel")
	val segel: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,


	@field:SerializedName("houskeeping")
	val houskeeping: String? = null,

	@field:SerializedName("selang")
	val selang: String? = null,


	@field:SerializedName("kapasitas")
	val kapasitas: Int? = null,

	@field:SerializedName("handle")
	val handle: String? = null,
)
