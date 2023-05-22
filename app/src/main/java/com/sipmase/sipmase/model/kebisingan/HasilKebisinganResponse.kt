package com.sipmase.sipmase.model.kebisingan

import com.google.gson.annotations.SerializedName

data class HasilKebisinganResponse(

	@field:SerializedName("data")
	val data: List<HasilKebisinganModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)