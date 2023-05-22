package com.sipmase.sipmase.model.pencahayaan

import com.google.gson.annotations.SerializedName

data class HasilPencahayaanResponse(

	@field:SerializedName("data")
	val data: List<HasilPencahayaanModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)
