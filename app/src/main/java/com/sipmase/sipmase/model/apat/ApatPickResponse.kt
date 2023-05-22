package com.sipmase.sipmase.model.apat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ApatPickResponse{
    @Expose
    @SerializedName("data")
    var data: List<Apat> = ArrayList()


    class Apat {
        var tgl_pengisian: String? = null
        var tgl_kadaluarsa: String? = null
        var updated_at: String? = null
        var lokasi: String? = null
        var kode: String? = null
        var no_bak: String? = null
        var created_at: String? = null
        var id: Int? = null
        override fun toString(): String {
            return "$kode - $lokasi"
        }

    }
}
