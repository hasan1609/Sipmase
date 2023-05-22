package com.sipmase.sipmase.model.pencahayaan

import com.google.gson.annotations.SerializedName

data class PostSchedulePencahayaanResponse(

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,

	@field:SerializedName("kode_pencahayaan")
	val kodePencahayaan: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null
)
