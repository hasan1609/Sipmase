package com.sipmase.sipmase.model.pencahayaan

import com.google.gson.annotations.SerializedName

data class PencahayaanResponse(

	@field:SerializedName("data")
	val data: List<PencahayaanModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

