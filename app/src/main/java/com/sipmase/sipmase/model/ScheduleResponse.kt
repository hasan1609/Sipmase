package com.sipmase.sipmase.model

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(

	@field:SerializedName("data")
	val data: List<ScheduleModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)
