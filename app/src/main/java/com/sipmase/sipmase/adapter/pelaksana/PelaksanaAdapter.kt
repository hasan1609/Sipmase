package com.sipmase.sipmase.adapter.pelaksana

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.model.AparModel
import com.sipmase.sipmase.model.UsersModel
import java.util.*

class PelaksanaAdapter (
    private val notesList: MutableList<UsersModel>,
    private val context: Context,

    ) : RecyclerView.Adapter<PelaksanaAdapter.ViewHolder>() {

    //database
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int, note: UsersModel)
    }

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var id: TextView
        internal var nama: TextView


        init {
            id = view.findViewById(R.id.txtusername)
            nama = view.findViewById(R.id.txtnama)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_pelaksana, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]
        holder.id.text = note.username
        holder.nama.text = note.name
        holder.itemView.setOnClickListener {
            if (dialog != null) {
                dialog!!.onClick(holder.layoutPosition, note)
            }
        }
    }

    val initialCityDataList = mutableListOf<UsersModel>().apply {
        notesList.let { addAll(it) }
    }

    fun getFilter(): Filter {
        return cityFilter
    }

    private val cityFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<UsersModel> = mutableListOf()
            if (constraint == null || constraint.isEmpty()) {
                initialCityDataList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialCityDataList.forEach {
                    if (it.name!!.toLowerCase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                    if (it.username!!.toLowerCase(Locale.ROOT).contains(query)) {
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
                notesList.addAll(results.values as MutableList<UsersModel>)
                notifyDataSetChanged()
            }
        }
    }

}