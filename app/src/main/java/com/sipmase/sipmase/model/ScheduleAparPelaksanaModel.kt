package com.sipmase.sipmase.model

import com.google.gson.annotations.SerializedName


data class ScheduleAparPelaksanaModel(

    @field:SerializedName("tw")
    val tw: String? = null,

    @field:SerializedName("tahun")
    val tahun: String? = null,

    @field:SerializedName("tuas")
    val tuas: Any? = null,

    @field:SerializedName("rambu")
    val rambu: Any? = null,

    @field:SerializedName("gantungan")
    val gantungan: Any? = null,

    @field:SerializedName("shift")
    val shift: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("tanggal_cek")
    val tanggalCek: String? = null,

    @field:SerializedName("pressure")
    val pressure: Any? = null,

    @field:SerializedName("tabung")
    val tabung: Any? = null,

    @field:SerializedName("nozzle")
    val nozzle: Any? = null,

    @field:SerializedName("is_status")
    val isStatus: Int? = null,

    @field:SerializedName("pin")
    val pin: Any? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("kode_apar")
    val kodeApar: String? = null,

    @field:SerializedName("keterangan_tambahan")
    val keteranganTambahan: Any? = null,

    @field:SerializedName("segel")
    val segel: Any? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("kapasitas")
    val kapasitas: Int? = null,

    @field:SerializedName("houskeeping")
    val houskeeping: Any? = null,

    @field:SerializedName("selang")
    val selang: Any? = null,

    @field:SerializedName("apar")
    val apar: Apar? = null,

    @field:SerializedName("handle")
    val handle: Any? = null,
)

data class Apar(

    @field:SerializedName("tgl_pengisian")
    val tglPengisian: String? = null,

    @field:SerializedName("tgl_kadaluarsa")
    val tglKadaluarsa: String? = null,

    @field:SerializedName("lokasi")
    val lokasi: String? = null,

    @field:SerializedName("kode")
    val kode: String? = null,

    @field:SerializedName("jenis")
    val jenis: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
