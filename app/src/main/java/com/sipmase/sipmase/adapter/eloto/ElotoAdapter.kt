package com.sipmase.sipmase.adapter.eloto

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.sipmase.sipmase.R
import com.sipmase.sipmase.model.eloto.GroupedDataEloto


class ElotoAdapter(
    private val notesList: MutableList<GroupedDataEloto>,
    private val context: Context,

    ) : RecyclerView.Adapter<ElotoAdapter.ViewHolder>() {

        //database
        private var dialog: Dialog? = null


        interface Dialog {
            fun onClick(position: Int, note : GroupedDataEloto)
        }

        fun setDialog(dialog: Dialog) {
            this.dialog = dialog
        }

        override fun getItemCount(): Int {
            return notesList.size
        }

        inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
            internal var btn: MaterialButton

            init {
                btn = view.findViewById(R.id.btn_eloto)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_eloto, parent, false)

            return ViewHolder(view)

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val note = notesList[position]

            holder.btn.text  = note.data.toString()

            holder.btn.setOnClickListener {
                if (dialog!=null){
                    dialog!!.onClick(position,note)
                }
            }

        }
}