package com.sipmase.sipmase.model.postdata

import com.google.gson.annotations.SerializedName

data class UpdateScheduleHydrant(

	@field:SerializedName("noozle")
	val noozle: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("kunci_box")
	val kunciBox: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("shift")
	val shift: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null,

	@field:SerializedName("main_valve")
	val mainValve: String? = null,

	@field:SerializedName("kunci_f")
	val kunciF: String? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("house_keeping")
	val houseKeeping: String? = null,

	@field:SerializedName("discharge")
	val discharge: String? = null,

	@field:SerializedName("kondisi_box")
	val kondisiBox: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("flushing")
	val flushing: String? = null,

	@field:SerializedName("selang")
	val selang: String? = null,

	@field:SerializedName("tanggal_pemeriksa")
	val tanggalPemeriksa: String? = null,
)
