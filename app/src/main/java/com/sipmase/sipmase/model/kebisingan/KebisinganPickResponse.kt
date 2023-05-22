package com.sipmase.sipmase.model.kebisingan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class KebisinganPickResponse{
    @Expose
    @SerializedName("data")
    var data: List<Kebisingan> = ArrayList()


    class Kebisingan {
        var tgl_pengisian: String? = null
        var tgl_kadaluarsa: String? = null
        var updated_at: String? = null
        var lokasi: String? = null
        var kode: String? = null
        var no_box: String? = null
        var created_at: String? = null
        var id: Int? = null
        override fun toString(): String {
            return "$kode - $lokasi"
        }

    }
}
