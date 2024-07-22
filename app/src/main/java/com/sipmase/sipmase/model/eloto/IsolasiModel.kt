package com.sipmase.sipmase.model.eloto

import com.google.gson.annotations.SerializedName

data class ResponseIsolasi(

	@field:SerializedName("data")
	val data: IsolasiModel? = null,

	@field:SerializedName("sukses")
	val sukses: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class IsolasiModel(

	@field:SerializedName("posisi_isolasi")
	val posisiIsolasi: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id_isolasi")
	val idIsolasi: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pic")
	val pic: String? = null,

	@field:SerializedName("tag_id")
	val tagId: String? = null,

	@field:SerializedName("eviden")
	val eviden: String? = null
)
