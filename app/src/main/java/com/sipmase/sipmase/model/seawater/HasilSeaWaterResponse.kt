package com.sipmase.sipmase.model.seawater

import com.google.gson.annotations.SerializedName

data class HasilSeaWaterResponse(

    @field:SerializedName("data")
	val data: List<SeaWaterModel>? = null,

    @field:SerializedName("message")
	val message: String? = null
)