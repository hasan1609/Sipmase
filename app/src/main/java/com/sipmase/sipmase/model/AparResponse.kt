package com.sipmase.sipmase.model

import com.google.gson.annotations.SerializedName

data class AparResponse(

	@field:SerializedName("data")
	val data: Array<AparModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

