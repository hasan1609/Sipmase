package com.sipmase.sipmase.model.seawater

import com.google.gson.annotations.SerializedName

data class PostSeaWaterSchedule(

	@field:SerializedName("hari")
	val hari: String? = null,

	@field:SerializedName("tw")
	val tw: String? = null,

	@field:SerializedName("tahun")
	val tahun: String? = null
)
