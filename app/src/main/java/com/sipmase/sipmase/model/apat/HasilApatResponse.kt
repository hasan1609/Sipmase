package com.sipmase.sipmase.model.apat

import com.google.gson.annotations.SerializedName

data class HasilApatResponse(

	@field:SerializedName("data")
	val data: List<HasilApatModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class HasilApatModel(

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

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("tanggal_cek")
	val tanggalCek: String? = null,

	@field:SerializedName("bak")
	val bak: String? = null,

	@field:SerializedName("apat")
	val apat: Apat? = null,

	@field:SerializedName("kode_apat")
	val kodeApat: String? = null,

	@field:SerializedName("is_status")
	val isStatus: Int? = null,

	@field:SerializedName("karung")
	val karung: String? = null,

	@field:SerializedName("pasir")
	val pasir: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("sekop")
	val sekop: String? = null,

	@field:SerializedName("tanggal_pemeriksa")
	val tanggalPemeriksa: String? = null

)

data class Apat(

	@field:SerializedName("no_bak")
	val noBak: String? = null,

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
