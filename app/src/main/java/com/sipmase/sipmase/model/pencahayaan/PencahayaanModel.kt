package com.sipmase.sipmase.model.pencahayaan

import com.google.gson.annotations.SerializedName

data class PencahayaanModel(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("lokasi")
    val lokasi: String? = null,

    @field:SerializedName("kode")
    val kode: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
){
    var isSelected: Boolean = false
}
