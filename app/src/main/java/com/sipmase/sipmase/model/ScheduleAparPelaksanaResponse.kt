package com.sipmase.sipmase.model

import com.google.gson.annotations.SerializedName

data class ScheduleAparPelaksanaResponse(

	@field:SerializedName("data")
	val data: List<ScheduleAparPelaksanaModel>? = null,

	@field:SerializedName("message")
	val message: String? = null
)
