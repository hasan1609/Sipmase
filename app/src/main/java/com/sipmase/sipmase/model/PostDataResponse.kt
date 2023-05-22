package com.sipmase.sipmase.model

import com.google.gson.annotations.SerializedName

data class PostDataResponse(

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("sukses")
	val sukses: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
