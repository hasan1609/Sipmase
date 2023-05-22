package com.sipmase.sipmase.model.apat

import com.google.gson.annotations.SerializedName

data class ApatResponse(

	@field:SerializedName("data")
	val data: List<ApatModel>? = null,
	@field:SerializedName("message")
	val message: String? = null
)

data class ApatModel(

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
){
	var isSelected: Boolean = false
}
