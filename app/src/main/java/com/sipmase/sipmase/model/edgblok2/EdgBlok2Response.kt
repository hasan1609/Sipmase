package com.sipmase.sipmase.model.edgblok2

import com.google.gson.annotations.SerializedName

data class EdgBlok2Response(
    @field:SerializedName("data")
    val data: List<EdgBlok2Model>? = null,

    @field:SerializedName("message")
    val message: String? = null
)
