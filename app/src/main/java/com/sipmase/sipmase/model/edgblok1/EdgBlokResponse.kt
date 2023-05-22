package com.sipmase.sipmase.model.edgblok1

import com.google.gson.annotations.SerializedName

data class EdgBlokResponse(
    @field:SerializedName("data")
    val data: List<EdgBlokModel>? = null,

    @field:SerializedName("message")
    val message: String? = null
)
