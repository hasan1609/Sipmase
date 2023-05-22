package com.sipmase.sipmase.model.hydrant

import com.google.gson.annotations.SerializedName

data class ItemHydrantResponse(

	@field:SerializedName("data")
	val data: List<HydrantModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class HydrantModel(

	@field:SerializedName("no_box")
	val noBox: String? = null,

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
