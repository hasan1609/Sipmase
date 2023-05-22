package com.sipmase.sipmase.model.kebisingan

import com.google.gson.annotations.SerializedName

data class PostScheduleKebisingan(

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("kode_kebisingan")
	val kodeKebisingan: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null
)
