package com.sipmase.sipmase.model.kebisingan

import com.google.gson.annotations.SerializedName

data class HasilKebisinganModel(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null,
	@field:SerializedName("shift")
	val shift: String? = null,

	@field:SerializedName("dbrata2")
	val dbrata2: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null,

	@field:SerializedName("nab_kebisingan")
	val nabKebisingan: String? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("kebisingan")
	val kebisingan: KebisinganModel? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("dbx2")
	val dbx2: String? = null,

	@field:SerializedName("dbx3")
	val dbx3: String? = null,

	@field:SerializedName("kode_kebisingan")
	val kodeKebisingan: String? = null,

	@field:SerializedName("dbx1")
	val dbx1: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)