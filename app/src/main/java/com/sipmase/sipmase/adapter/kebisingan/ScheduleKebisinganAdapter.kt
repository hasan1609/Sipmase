package com.sipmase.sipmase.adapter.kebisingan

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.model.apat.HasilApatModel
import com.sipmase.sipmase.model.hydrant.ScheduleHydrantPelaksanaModel
import com.sipmase.sipmase.model.kebisingan.ScheduleKebisinganModel

class ScheduleKebisinganAdapter(
    private val notesList: MutableList<ScheduleKebisinganModel>,
    private val context: Context,

    ) : RecyclerView.Adapter<ScheduleKebisinganAdapter.ViewHolder>() {

    //database
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int, note: ScheduleKebisinganModel)
    }

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var kode: TextView
        internal var lokasi: TextView
        internal var tanggal: TextView
        internal var status: TextView

        init {
            kode = view.findViewById(R.id.txtkode)
            lokasi = view.findViewById(R.id.txtlokasi)
            tanggal = view.findViewById(R.id.txttgl_cek)
            status = view.findViewById(R.id.txtstatus)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_schedule_kebisingan, parent, false)

        return ViewHolder(view)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]
        holder.kode.text = note.kodeKebisingan
        holder.lokasi.text = note.kebisingan!!.lokasi
        if (note.isStatus == 0) {
            holder.status.text = "Belum di cek"
            holder.kode.setBackgroundColor(Color.parseColor("#808080"))
        } else if (note.isStatus == 2) {
            holder.status.text = "Selesai"
            holder.kode.setBackgroundColor(Color.parseColor("#16A34A"))
        } else if (note.isStatus == 1) {
            holder.status.text = "Silahkan Approve"
            holder.kode.setBackgroundColor(Color.parseColor("#FCDE62"))
        } else if (note.isStatus == 3) {
            holder.status.text = "Return"
            holder.kode.setBackgroundColor(Color.parseColor("#F87171"))
        }
        holder.tanggal.text = note.tanggalCek
        holder.itemView.setOnClickListener {
            if (dialog != null) {
                dialog!!.onClick(position, note)
            }
        }
    }
}

