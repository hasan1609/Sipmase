package com.sipmase.sipmase.model.eloto

import com.google.gson.annotations.SerializedName

data class ElotoModel(

    @field:SerializedName("posisi_awal")
    val posisiAwal: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("penormalan")
    val penormalan: PenormalanModel? = null,

    @field:SerializedName("lokasi")
    val lokasi: String? = null,

    @field:SerializedName("wo")
    val wo: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("isolasi")
    val isolasi: IsolasiModel? = null,

    @field:SerializedName("peralatan")
    val peralatan: String? = null,

    @field:SerializedName("ket")
    val ket: String? = null,

    @field:SerializedName("tw")
    val tw: String? = null,

    @field:SerializedName("tahun")
    val tahun: String? = null,

    @field:SerializedName("id_tag")
    val idTag: Int? = null,
)