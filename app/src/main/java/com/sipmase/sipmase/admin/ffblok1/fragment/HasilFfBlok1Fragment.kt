package com.sipmase.sipmase.admin.ffblok1.fragment

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
import com.sipmase.sipmase.admin.ffblok1.activity.DetailCekFfBlok1Activity
import com.sipmase.sipmase.databinding.FragmentHasilFfBlok1Binding
import com.sipmase.sipmase.databinding.FragmentScheduleFfBlok1Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.ffblok.FFBlokModel
import com.sipmase.sipmase.model.ffblok.FFBlokResponse
import com.sipmase.sipmase.webservice.ApiClient
import com.sipmat.sipmat.adapter.damkar.SchedleFfBlok1Adapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HasilFfBlok1Fragment : Fragment(), AnkoLogger {
    lateinit var binding: FragmentHasilFfBlok1Binding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    var tahun: String? = null
    private lateinit var mAdapter: SchedleFfBlok1Adapter
    var tw: String? = null
    val sheet = BottomSheetAddScheduleFfBlok1Fragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_hasil_ff_blok1, container, false)
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
                else -> binding.rvscheduleffblok.visibility = View.GONE
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
        binding.rvscheduleffblok.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvscheduleffblok.setHasFixedSize(true)
        (binding.rvscheduleffblok.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.gethasil_ffblok(tw, tahun)
            .enqueue(object : Callback<FFBlokResponse> {
                override fun onResponse(
                    call: Call<FFBlokResponse>,
                    response: Response<FFBlokResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<FFBlokModel>()
                            val data = response.body()
                            if (data!!.data!!.isEmpty()) {
                                binding.tvkosong.visibility = View.VISIBLE
                                binding.rvscheduleffblok.visibility = View.GONE
                                toast("Data Hasil Masih Kosong")
                            } else {
                                loading(false)
                                binding.tvkosong.visibility = View.GONE
                                binding.rvscheduleffblok.visibility = View.VISIBLE
                                for (hasil in data.data!!) {
                                    notesList.add(hasil)
                                    mAdapter = SchedleFfBlok1Adapter(notesList, requireActivity())
                                    binding.rvscheduleffblok.adapter = mAdapter
                                    mAdapter.setDialog(object : SchedleFfBlok1Adapter.Dialog {
                                        override fun onClick(position: Int, note: FFBlokModel) {
                                            val builder =
                                                AlertDialog.Builder(requireActivity())
                                            builder.setTitle("Cek Schedule ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<DetailCekFfBlok1Activity>("cekffblok1" to noteJson)


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

                override fun onFailure(call: Call<FFBlokResponse>, t: Throwable) {
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

