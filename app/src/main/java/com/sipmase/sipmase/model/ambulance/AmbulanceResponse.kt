package com.sipmase.sipmase.model.ambulance

import com.google.gson.annotations.SerializedName

data class AmbulanceResponse(
    @field:SerializedName("data")
    val data: List<AmbulanceModel>? = null,

    @field:SerializedName("message")
    val message: String? = null
)
