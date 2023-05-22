package com.sipmat.sipmat.adapter.damkar

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.model.damkar.DamkarModel
import com.sipmase.sipmase.model.edgblok1.EdgBlokModel
import com.sipmase.sipmase.model.edgblok2.EdgBlok2Model
import com.sipmase.sipmase.model.edgblok3.EdgBlok3Model

class SchedleEdgBlok3Adapter(
    private val notesList: MutableList<EdgBlok3Model>,
    private val context: Context,

    ) : RecyclerView.Adapter<SchedleEdgBlok3Adapter.ViewHolder>() {

    //database
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int, note : EdgBlok3Model)
    }

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var tw: TextView
        internal var tahun: TextView
        internal var hari: TextView
        internal var status: TextView
        internal var tgl: TextView

        init {
            tw = view.findViewById(R.id.txttw)
            tahun = view.findViewById(R.id.txttahun)
            hari = view.findViewById(R.id.txthari)
            status = view.findViewById(R.id.txtstatus)
            tgl = view.findViewById(R.id.txttgl_cek)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_schedule, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]
        if (note.isStatus == 0) {
            holder.status.text = "Belum di cek"
            holder.status.setBackgroundColor(Color.parseColor("#808080"))
        } else if (note.isStatus == 2) {
            holder.status.text = "Selesai"
            holder.status.setBackgroundColor(Color.parseColor("#16A34A"))
        } else if (note.isStatus == 1) {
            holder.status.text = "Silahkan Approve"
            holder.status.setBackgroundColor(Color.parseColor("#FCDE62"))
        } else if (note.isStatus == 3) {
            holder.status.text = "Return"
            holder.status.setBackgroundColor(Color.parseColor("#F87171"))
        }
        holder.tw.text = note.tw
        holder.tahun.text = note.tahun
        holder.hari.text = note.hari
        holder.tgl.text = note.tanggalCek

        holder.itemView.setOnClickListener {
            if (dialog!=null){
                dialog!!.onClick(position,note)
            }
        }

    }

}
