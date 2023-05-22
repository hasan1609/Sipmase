package com.sipmase.sipmase.admin.edgblok2.fragment

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.admin.edgblok1.activity.DetailCekEdgblok1Activity
import com.sipmase.sipmase.admin.edgblok2.activity.DetailCekEdgBlok2Activity
import com.sipmase.sipmase.databinding.FragmentHasilEdgBlok2Binding
import com.sipmase.sipmase.databinding.FragmentScheduleEdgBlok2Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.edgblok2.EdgBlok2Model
import com.sipmase.sipmase.model.edgblok2.EdgBlok2Response
import com.sipmase.sipmase.webservice.ApiClient
import com.sipmat.sipmat.adapter.damkar.SchedleEdgBlok2Adapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HasilEdgBlok2Fragment : Fragment(), AnkoLogger {
    lateinit var binding: FragmentHasilEdgBlok2Binding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    var tahun: String? = null
    private lateinit var mAdapter: SchedleEdgBlok2Adapter
    var tw: String? = null
    val sheet = BottomSheetAddScheduleEdgBlok2Fragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_hasil_edg_blok2,
            container,
            false
        )
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        val dialog = datePickerDialog()
        binding.tahun.setOnClickListener {

            // Show Date Picker
            dialog.show()

            // Hide Year Selector
            val tgl = dialog.findViewById<View>(
                Resources.getSystem().getIdentifier("android:id/day", null, null)
            )
            if (tgl != null) {
                tgl.visibility = View.GONE
            }
            val month = dialog.findViewById<View>(
                Resources.getSystem().getIdentifier("android:id/month", null, null)
            )
            if (month != null) {
                month.visibility = View.GONE
            }


        }
        binding.rbtw.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbtw1 -> if (binding.tahun.text.toString() != "Pilih Tahun") {
                    getschedule("I", binding.tahun.text.toString())
                } else {
                    toast("Pilih Tahun Terlebih Dahulu")
                }
                R.id.rbtw2 -> if (binding.tahun.text.toString() != "Pilih Tahun") {
                    getschedule("II", binding.tahun.text.toString())
                } else {
                    toast("Pilih Tahun Terlebih Dahulu")
                }
                R.id.rbtw3 -> if (binding.tahun.text.toString() != "Pilih Tahun") {
                    getschedule("III", binding.tahun.text.toString())
                } else {
                    toast("Pilih Tahun Terlebih Dahulu")
                }
                R.id.rbtw4 -> if (binding.tahun.text.toString() != "Pilih Tahun") {
                    getschedule("IV", binding.tahun.text.toString())
                } else {
                    toast("Pilih Tahun Terlebih Dahulu")
                }
                else -> binding.rvscheduleedgblok.visibility = View.GONE
            }
        }

        return binding.root
    }


    fun loading(status: Boolean) {
        if (status) {
            progressDialog.setTitle("Loading...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    fun getschedule(tw: String, tahun: String) {
        binding.rvscheduleedgblok.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvscheduleedgblok.setHasFixedSize(true)
        (binding.rvscheduleedgblok.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.gethasil_edgblok2(tw, tahun)
            .enqueue(object : Callback<EdgBlok2Response> {
                override fun onResponse(
                    call: Call<EdgBlok2Response>,
                    response: Response<EdgBlok2Response>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<EdgBlok2Model>()
                            val data = response.body()
                            if (data!!.data!!.isEmpty()) {
                                binding.tvkosong.visibility = View.VISIBLE
                                binding.rvscheduleedgblok.visibility = View.GONE
                                toast("Data Hasil Masih Kosong")
                            } else {
                                loading(false)
                                binding.tvkosong.visibility = View.GONE
                                binding.rvscheduleedgblok.visibility = View.VISIBLE
                                for (hasil in data.data!!) {
                                    notesList.add(hasil)
                                    mAdapter = SchedleEdgBlok2Adapter(notesList, requireActivity())
                                    binding.rvscheduleedgblok.adapter = mAdapter
                                    mAdapter.setDialog(object : SchedleEdgBlok2Adapter.Dialog {
                                        override fun onClick(position: Int, note: EdgBlok2Model) {
                                            val builder =
                                                AlertDialog.Builder(requireActivity())
                                            builder.setTitle("Cek Schedule ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<DetailCekEdgBlok2Activity>("cekedgblok2" to noteJson)


                                            }


                                            builder.setNegativeButton("Batal") { dialog, which ->

                                            }

                                            builder.show()

                                        }

                                    })
                                    mAdapter.notifyDataSetChanged()
                                }

                            }
                        } else {
                            loading(false)
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        loading(false)
                        info { "dinda ${e.message}" }
                        toast(e.message.toString())
                    }
                }

                override fun onFailure(call: Call<EdgBlok2Response>, t: Throwable) {
                    loading(false)
                    info { "dinda ${t.message}" }
                    toast(t.message.toString())
                }

            })


    }

    override fun onStart() {
        super.onStart()
    }

    fun datePickerDialog(): DatePickerDialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Date Picker Dialog
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            android.R.style.Theme_Holo_Dialog,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                binding.tahun.text = "$year"
            },
            year,
            month,
            day
        )
        // Show Date Picker

        return datePickerDialog
    }
}
