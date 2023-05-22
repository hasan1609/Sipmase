package com.sipmase.sipmase.adapter.pencahayaan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.model.pencahayaan.PencahayaanModel
import java.util.*

class ItemPencahayaanAdminAdapater (
    private val notesList: MutableList<PencahayaanModel>,
    private val context: Context,

    ) : RecyclerView.Adapter<ItemPencahayaanAdminAdapater.ViewHolder>() {

        //database
        private var dialog: Dialog? = null


        interface Dialog {
            fun onClick(position: Int, note : PencahayaanModel)
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


            init {
                kode = view.findViewById(R.id.txtkode)
                lokasi = view.findViewById(R.id.txtlokasi)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_pencahayaan, parent, false)

            return ViewHolder(view)

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val note = notesList[position]
            holder.kode.text = note.kode
            holder.lokasi.text = "Lokasi Pengecekan : ${note.lokasi}"
            holder.itemView.setOnClickListener {
                if(dialog != null){
                    dialog!!.onClick(holder.layoutPosition,note)
                }
            }
        }

        val initialCityDataList = mutableListOf<PencahayaanModel>().apply {
            notesList.let { addAll(it) }
        }

        fun getFilter(): Filter {
            return cityFilter
        }

        private val cityFilter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: MutableList<PencahayaanModel> = mutableListOf()
                if (constraint == null || constraint.isEmpty()) {
                    initialCityDataList.let { filteredList.addAll(it) }
                } else {
                    val query = constraint.toString().trim().toLowerCase()
                    initialCityDataList.forEach {
                        if (it.kode!!.toLowerCase(Locale.ROOT).contains(query)) {
                            filteredList.add(it)
                        }
                        if (it.lokasi!!.toLowerCase(Locale.ROOT).contains(query)) {
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
                    notesList.addAll(results.values as MutableList<PencahayaanModel>)
                    notifyDataSetChanged()
                }
            }
        }
    }
