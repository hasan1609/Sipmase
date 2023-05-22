package com.sipmase.sipmase.model.damkar

import com.google.gson.annotations.SerializedName

data class DamkarResponse(
    @field:SerializedName("data")
    val data: List<DamkarModel>? = null,

    @field:SerializedName("message")
    val message: String? = null
)
