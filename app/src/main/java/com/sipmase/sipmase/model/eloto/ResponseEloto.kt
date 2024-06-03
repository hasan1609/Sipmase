package com.sipmase.sipmase.model.eloto

import com.google.gson.annotations.SerializedName

data class ResponseEloto(


	@SerializedName("data")
	val data: List<ElotoModel?>? = null,

	@field:SerializedName("sukses")
	val sukses: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
