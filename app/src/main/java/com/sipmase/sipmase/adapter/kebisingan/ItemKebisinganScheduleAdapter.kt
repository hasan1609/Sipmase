package com.sipmase.sipmase.adapter.kebisingan

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.sipmase.sipmase.R
import com.sipmase.sipmase.model.hydrant.HydrantModel
import com.sipmase.sipmase.model.kebisingan.KebisinganModel
import java.util.*

class ItemKebisinganScheduleAdapter(
    private val notesList: MutableList<KebisinganModel>,
    private val context: Context,

    ) : RecyclerView.Adapter<ItemKebisinganScheduleAdapter.ViewHolder>() {
    var lista = mutableListOf<KebisinganModel>()
    var checkBoxStateArray = SparseBooleanArray()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Using Kotlin Android Extensions, access checkbox
        var cb: CheckBox

        init {//called after the constructor.
            cb = itemView.findViewById(R.id.cb)
            cb.setOnClickListener {

                if (!checkBoxStateArray.get(adapterPosition, false)) {//checkbox checked
                    cb.isChecked = true
                    //stores checkbox states and position
                    checkBoxStateArray.put(adapterPosition, true)
                    lista.add(notesList[position])
                } else {//checkbox unchecked
                    cb.isChecked = false
                    //stores checkbox states and position.
                    checkBoxStateArray.put(adapterPosition, false)
                    lista.remove(notesList[position])
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_check_box, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]
        holder.cb.text = note.kode
        holder.cb.isChecked = checkBoxStateArray.get(position, false)
    }

    val initialCityDataList = mutableListOf<KebisinganModel>().apply {
        notesList.let { addAll(it) }
    }

    fun getFilter(): Filter {
        return cityFilter
    }

    private val cityFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<KebisinganModel> = mutableListOf()
            if (constraint == null || constraint.isEmpty()) {
                initialCityDataList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialCityDataList.forEach {
                    if (it.kode!!.toLowerCase(Locale.ROOT).contains(query)) {
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
                notesList.addAll(results.values as MutableList<KebisinganModel>)
                notifyDataSetChanged()
            }
        }
    }
}
