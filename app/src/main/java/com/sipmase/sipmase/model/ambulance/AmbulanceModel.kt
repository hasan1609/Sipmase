package com.sipmase.sipmase.model.ambulance

import com.google.gson.annotations.SerializedName

data class AmbulanceModel (
    @field:SerializedName("hari")
    val hari: String? = null,

    @field:SerializedName("tisu_kering")
    val tisuKering: String? = null,

    @field:SerializedName("tw")
    val tw: String? = null,

    @field:SerializedName("tanggal_pemeriksa")
    val tanggalPemeriksa: String? = null,

    @field:SerializedName("shift")
    val shift: String? = null,

    @field:SerializedName("kondisi_fisik")
    val kondisiFisik: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("tanggal_cek")
    val tanggalCek: String? = null,

    @field:SerializedName("apar")
    val apar: String? = null,

    @field:SerializedName("roda")
    val roda: String? = null,

    @field:SerializedName("is_status")
    val isStatus: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("spons_kasur")
    val sponsKasur: String? = null,

    @field:SerializedName("tabung_oksigen")
    val tabungOksigen: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("oxycan")
    val oxycan: String? = null,

    @field:SerializedName("air_wastafel")
    val airWastafel: String? = null,

    @field:SerializedName("kebersihan")
    val kebersihan: String? = null,

    @field:SerializedName("antiseptic_gel")
    val antisepticGel: String? = null,

    @field:SerializedName("tahun")
    val tahun: String? = null,

    @field:SerializedName("catatan")
    val catatan: String? = null,

    @field:SerializedName("a_tekanan")
    val aTekanan: String? = null,

    @field:SerializedName("tandu_dorong")
    val tanduDorong: String? = null,

    @field:SerializedName("to_tekanan")
    val toTekanan: String? = null,

    @field:SerializedName("tandu_lipat")
    val tanduLipat: String? = null,

    @field:SerializedName("tas_p3k")
    val tasP3k: String? = null,

    @field:SerializedName("perlak")
    val perlak: String? = null,

    @field:SerializedName("cairan_infus")
    val cairanInfus: String? = null
)