package com.sipmase.sipmase.model.eloto

import com.google.gson.annotations.SerializedName

data class ResponsePenormalan(

	@field:SerializedName("data")
	val data: PenormalanModel? = null,

	@field:SerializedName("sukses")
	val sukses: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PenormalanModel(

	@field:SerializedName("id_penormalan")
	val idPenormalan: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pic")
	val pic: String? = null,

	@field:SerializedName("id_tag")
	val idTag: String? = null,

	@field:SerializedName("posisi_normal")
	val posisiNormal: String? = null
)
