package com.sipmase.sipmase.adapter.eloto

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.sipmase.sipmase.R
import com.sipmase.sipmase.model.eloto.ElotoModel
import java.util.*


class DetailElotoAdapter(
    private val notesList: MutableList<ElotoModel>,
    private val context: Context,

    ) : RecyclerView.Adapter<DetailElotoAdapter.ViewHolder>() {

        private var penormalanDialog: OnPenormalanClickListener? = null
        private var isolasiDialog: OnIsolasiClickListener? = null
        private var dialog: Dialog? = null

        fun setDialog(dialog: Dialog) {
            this.dialog = dialog
        }

        fun setOnIsolasiClickListener(listener: OnIsolasiClickListener) {
            this.isolasiDialog = listener
        }

        fun setOnPenormalanClickListener(listener: OnPenormalanClickListener) {
            this.penormalanDialog = listener
        }

        interface OnIsolasiClickListener {
            fun onIsolasiClick(position: Int, note: ElotoModel)
        }

        interface OnPenormalanClickListener {
            fun onPenormalanClick(position: Int, note: ElotoModel)
        }

        interface Dialog {
            fun onClick(position: Int, note : ElotoModel)
        }

        override fun getItemCount(): Int {
            return notesList.size
        }

        inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
            internal var btn_isolasi: MaterialButton
            internal var btn_penormalan: MaterialButton
            internal var tag: TextView
            internal var lokasi: TextView
            internal var peralatan: TextView
            internal var wo: TextView

            init {
                btn_isolasi = view.findViewById(R.id.btn_isolasi)
                btn_penormalan = view.findViewById(R.id.btn_penormalan)
                tag = view.findViewById(R.id.txt_tag)
                lokasi = view.findViewById(R.id.txt_lokasi)
                peralatan = view.findViewById(R.id.txt_peralatan)
                wo = view.findViewById(R.id.txt_wo)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_data_eloto, parent, false)

            return ViewHolder(view)

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val note = notesList[position]

            holder.tag.text = note.idTag.toString()
            holder.wo.text = note.wo.toString()
            holder.lokasi.text = note.lokasi.toString()
            holder.peralatan.text = note.peralatan.toString()

            if (note.penormalan == null){
                holder.btn_penormalan.visibility = View.VISIBLE
            }else{
                holder.btn_penormalan.visibility = View.GONE
            }

            if (note.isolasi == null){
                holder.btn_isolasi.visibility = View.VISIBLE
            }else{
                holder.btn_isolasi.visibility = View.GONE
            }

            holder.btn_isolasi.setOnClickListener {
                isolasiDialog!!.onIsolasiClick(position, note)
            }
            holder.btn_penormalan.setOnClickListener {
                penormalanDialog!!.onPenormalanClick(position, note)
            }

            holder.itemView.setOnClickListener {
                if (dialog!=null){
                    dialog!!.onClick(position,note)
                }
            }

        }
    val initialDataList = mutableListOf<ElotoModel>().apply {
        notesList.let { addAll(it) }
    }

    fun getFilter(): Filter {
        return dataFilter
    }

    private val dataFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<ElotoModel> = mutableListOf()
            if (constraint == null || constraint.isEmpty()) {
                initialDataList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialDataList.forEach {
                    if (it.wo!!.toLowerCase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }else if (it.idTag!!.toString().contains(query)) {
                        filteredList.add(it)
                    }else if (it.peralatan!!.toLowerCase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is MutableList<*>) {
                notesList.clear()
                notesList.addAll(results.values as MutableList<ElotoModel>)
                notifyDataSetChanged()
            }
        }
    }
}