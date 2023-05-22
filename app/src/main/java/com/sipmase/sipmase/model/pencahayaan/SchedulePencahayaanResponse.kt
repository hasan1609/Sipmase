package com.sipmase.sipmase.model.pencahayaan

import com.google.gson.annotations.SerializedName

data class SchedulePencahayaanResponse(

	@field:SerializedName("data")
	val data: List<SchedulePencahayaanModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)
