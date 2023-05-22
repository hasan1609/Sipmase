package com.sipmase.sipmase.model.edgblok3

import com.google.gson.annotations.SerializedName

data class EdgBlok3Response(
    @field:SerializedName("data")
    val data: List<EdgBlok3Model>? = null,

    @field:SerializedName("message")
    val message: String? = null
)
