package com.sipmase.sipmase.adapter.apar

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.model.AparModel
import java.util.*

class ItemAparKadaluarsaAdapter (
    private val notesList: MutableList<AparModel>,
    private val context: Context,

    ) : RecyclerView.Adapter<ItemAparKadaluarsaAdapter.ViewHolder>() {

    //database
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int, note: AparModel)
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
        internal var jenis: TextView
        internal var kadaluarsa: TextView


        init {
            kode = view.findViewById(R.id.txtkode)
            gedung = view.findViewById(R.id.txtgedung)
            jenis = view.findViewById(R.id.txtjenis)
            kadaluarsa = view.findViewById(R.id.txtkadaluarsa)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_apar, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]
        holder.kode.text = note.kode
        holder.kode.setBackgroundColor(Color.parseColor("#F87171"))
        holder.gedung.text = "Lokasi Penempatan : ${note.lokasi}"
        holder.jenis.text = "Jenis APAR : ${note.jenis}"
        holder.kadaluarsa.text = "Kadaluarsa : ${note.tglKadaluarsa}"
        holder.itemView.setOnClickListener {
            if (dialog != null) {
                dialog!!.onClick(holder.layoutPosition, note)
            }
        }
    }

    val initialCityDataList = mutableListOf<AparModel>().apply {
        notesList.let { addAll(it) }
    }

    fun getFilter(): Filter {
        return cityFilter
    }

    private val cityFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<AparModel> = mutableListOf()
            if (constraint == null || constraint.isEmpty()) {
                initialCityDataList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialCityDataList.forEach {
                    if (it.kode!!.toLowerCase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                    if (it.jenis!!.toLowerCase(Locale.ROOT).contains(query)) {
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
                notesList.addAll(results.values as MutableList<AparModel>)
                notifyDataSetChanged()
            }
        }
    }
}