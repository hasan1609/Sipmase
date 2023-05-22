package com.sipmase.sipmase.model.damkar

import com.google.gson.annotations.SerializedName

data class DamkarModel (
    @field:SerializedName("hari")
    val hari: String? = null,

    @field:SerializedName("spion")
    val spion: String? = null,

    @field:SerializedName("tw")
    val tw: String? = null,

    @field:SerializedName("lampu_hazard")
    val lampuHazard: String? = null,

    @field:SerializedName("tanggal_pemeriksa")
    val tanggalPemeriksa: String? = null,

    @field:SerializedName("shift")
    val shift: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("tanggal_cek")
    val tanggalCek: String? = null,

    @field:SerializedName("lampu_rem")
    val lampuRem: String? = null,

    @field:SerializedName("lampu_sorot")
    val lampuSorot: String? = null,

    @field:SerializedName("lampu_dalam_tengah")
    val lampuDalamTengah: String? = null,

    @field:SerializedName("level_minyak_rem")
    val levelMinyakRem: String? = null,

    @field:SerializedName("is_status")
    val isStatus: Int? = null,

    @field:SerializedName("lampu_belakang")
    val lampuBelakang: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("air_accu")
    val airAccu: String? = null,

    @field:SerializedName("level_air_radiator")
    val levelAirRadiator: String? = null,

    @field:SerializedName("level_oil")
    val levelOil: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("lampu_sein_kiri_belakang")
    val lampuSeinKiriBelakang: String? = null,

    @field:SerializedName("tahun")
    val tahun: String? = null,

    @field:SerializedName("suara_mesin")
    val suaraMesin: String? = null,

    @field:SerializedName("lampu_depan")
    val lampuDepan: String? = null,

    @field:SerializedName("start")
    val start: String? = null,

    @field:SerializedName("catatan")
    val catatan: String? = null,

    @field:SerializedName("lampu_dalam_belakang")
    val lampuDalamBelakang: String? = null,

    @field:SerializedName("filter_solar")
    val filterSolar: String? = null,

    @field:SerializedName("lampu_sein_kanan_depan")
    val lampuSeinKananDepan: String? = null,

    @field:SerializedName("stop")
    val stop: String? = null,

    @field:SerializedName("lampu_sein_kanan_belakang")
    val lampuSeinKananBelakang: String? = null,

    @field:SerializedName("wiper")
    val wiper: String? = null,

    @field:SerializedName("lampu_dalam_depan")
    val lampuDalamDepan: String? = null,

    @field:SerializedName("tempratur_mesin")
    val tempraturMesin: String? = null,

    @field:SerializedName("lampu_sein_kiri_depan")
    val lampuSeinKiriDepan: String? = null,

    @field:SerializedName("sirine")
    val sirine: String? = null
)