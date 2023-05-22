package com.sipmase.sipmase.adapter.apar

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.model.ScheduleAparPelaksanaModel
import com.sipmase.sipmase.model.ScheduleModel

class ScheduleAparPelaksanaAdapter(
    private val notesList: MutableList<ScheduleAparPelaksanaModel>,
    private val context: Context,

    ) : RecyclerView.Adapter<ScheduleAparPelaksanaAdapter.ViewHolder>() {

    //database
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int, note : ScheduleAparPelaksanaModel)
    }

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var kode: TextView
        internal var gedung: TextView
        internal var status: TextView
        internal var jadwal: TextView
        internal var jenis: TextView

        init {
            jenis = view.findViewById(R.id.txtjenis)
            kode = view.findViewById(R.id.txtkode)
            gedung = view.findViewById(R.id.txtlokasi)
            status = view.findViewById(R.id.txtstatus)
            jadwal = view.findViewById(R.id.txttgl_cek)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_schedule_apar, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]
        holder.kode.text = note.kodeApar
        holder.gedung.text = note.apar!!.lokasi
        if (note.isStatus == 0) {
            holder.status.text = "Belum di cek"
            holder.kode.setBackgroundColor(Color.parseColor("#808080"))
        } else if (note.isStatus == 2) {
            holder.status.text = "Selesai"
            holder.kode.setBackgroundColor(Color.parseColor("#16A34A"))
        } else if (note.isStatus == 1) {
            holder.status.text = "Proses Pengecekan"
            holder.kode.setBackgroundColor(Color.parseColor("#FCDE62"))
        } else if (note.isStatus == 3) {
            holder.status.text = "Return"
            holder.kode.setBackgroundColor(Color.parseColor("#F87171"))
        }
        holder.jadwal.text = note.tanggalCek
        holder.jenis.text = note.apar.jenis
        holder.itemView.setOnClickListener {
            if (dialog!=null){
                dialog!!.onClick(position,note)
            }
        }
    }

}
