package com.sipmase.sipmase.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("data")
	val data: UsersModel? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
