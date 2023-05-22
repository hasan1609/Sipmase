package com.sipmase.sipmase.model

import com.google.gson.annotations.SerializedName

data class UsersResponse(

	@field:SerializedName("data")
	val data: List<UsersModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

