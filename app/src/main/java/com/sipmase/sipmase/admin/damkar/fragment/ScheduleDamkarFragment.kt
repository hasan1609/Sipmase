package com.sipmase.sipmase.admin.damkar.fragment

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
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.ambulance.ScheduleAmbulanceAdapter
import com.sipmase.sipmase.admin.ambulance.fragment.BottomSheetAddScheduleAmbulanceFragment
import com.sipmase.sipmase.admin.edgblok1.fragment.BottomSheetAddScheduleEdgBlok1Fragment
import com.sipmase.sipmase.databinding.FragmentScheduleAmbulanceBinding
import com.sipmase.sipmase.databinding.FragmentScheduleDamkarBinding
import com.sipmase.sipmase.databinding.FragmentScheduleEdgBlok1Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.ambulance.AmbulanceModel
import com.sipmase.sipmase.model.ambulance.AmbulanceResponse
import com.sipmase.sipmase.model.damkar.DamkarModel
import com.sipmase.sipmase.model.damkar.DamkarResponse
import com.sipmase.sipmase.model.edgblok1.EdgBlokModel
import com.sipmase.sipmase.model.edgblok1.EdgBlokResponse
import com.sipmase.sipmase.webservice.ApiClient
import com.sipmat.sipmat.adapter.damkar.SchedleEdgBlok1Adapter
import com.sipmat.sipmat.adapter.damkar.ScheduleDamkarAdapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ScheduleDamkarFragment : Fragment(), AnkoLogger {
    lateinit var binding: FragmentScheduleDamkarBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    var tahun: String? = null
    private lateinit var mAdapter: ScheduleDamkarAdapter
    var tw: String? = null
    val sheet = BottomSheetAddScheduleDamkarFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_damkar, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        val dialog = datePickerDialog()
        binding.tahun.setOnClickListener {

            // Show Date Picker
            dialog.show()

            // Hide Year Selector
            val tgl = dialog.findViewById<View>(Resources.getSystem().getIdentifier("android:id/day", null, null))
            if (tgl != null) {
                tgl.visibility = View.GONE
            }
            val month = dialog.findViewById<View>(Resources.getSystem().getIdentifier("android:id/month", null, null))
            if (month != null) {
                month.visibility = View.GONE
            }


        }
        binding.rbtw.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rbtw1 -> if (binding.tahun.text.toString() != "Pilih Tahun"){
                    getschedule("I", binding.tahun.text.toString())
                }else{
                    toast("Pilih Tahun Terlebih Dahulu")
                }
                R.id.rbtw2 -> if (binding.tahun.text.toString() != "Pilih Tahun"){
                    getschedule("II", binding.tahun.text.toString())
                }else{
                    toast("Pilih Tahun Terlebih Dahulu")
                }
                R.id.rbtw3 -> if (binding.tahun.text.toString() != "Pilih Tahun"){
                    getschedule("III", binding.tahun.text.toString())
                }else{
                    toast("Pilih Tahun Terlebih Dahulu")
                }
                R.id.rbtw4 -> if (binding.tahun.text.toString() != "Pilih Tahun"){
                    getschedule("IV", binding.tahun.text.toString())
                }else{
                    toast("Pilih Tahun Terlebih Dahulu")
                }
                else -> binding.rvscheduledamkar.visibility = View.GONE
            }
        }
        binding.btnTambahScheduleDamkar.setOnClickListener {
            sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddScheduleAmbulanceFragment")
        }
        return binding.root
    }


    fun loading(status : Boolean){
        if (status){
            progressDialog.setTitle("Loading...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        }else{
            progressDialog.dismiss()
        }
    }

    fun getschedule(tw: String, tahun: String) {
        binding.rvscheduledamkar.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvscheduledamkar.setHasFixedSize(true)
        (binding.rvscheduledamkar.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.get_damkar(tw, tahun)
            .enqueue(object : Callback<DamkarResponse> {
                override fun onResponse(
                    call: Call<DamkarResponse>,
                    response: Response<DamkarResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<DamkarModel>()
                            val data = response.body()
                            if (data!!.data!!.isEmpty()) {
                                binding.tvkosong.visibility = View.VISIBLE
                                binding.rvscheduledamkar.visibility = View.GONE
                                toast("Data Hasil Masih Kosong")
                            } else {
                                loading(false)
                                binding.tvkosong.visibility = View.GONE
                                binding.rvscheduledamkar.visibility = View.VISIBLE
                                for (hasil in data.data!!) {
                                    notesList.add(hasil)
                                    mAdapter = ScheduleDamkarAdapter(notesList, requireActivity())
                                    binding.rvscheduledamkar.adapter = mAdapter
                                    mAdapter.setDialog(object : ScheduleDamkarAdapter.Dialog {
                                        override fun onClick(position: Int, note: DamkarModel) {
                                            val builder =
                                                AlertDialog.Builder(requireActivity())
                                            builder.setTitle("Hapus Schedule ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                api.hapus_damkar(note.id!!).enqueue(object :
                                                    Callback<PostDataResponse> {
                                                    override fun onResponse(
                                                        call: Call<PostDataResponse>,
                                                        response: Response<PostDataResponse>
                                                    ) {
                                                        try {
                                                            if (response.body()!!.sukses == 1) {
                                                                loading(false)
                                                                toast("Hapus Schedule berhasil")
                                                                getschedule(note.tw!!, note.tahun!!)
                                                            } else {
                                                                loading(false)
                                                                toast("Hapus Schedule gagal")
                                                            }


                                                        } catch (e: Exception) {
                                                            progressDialog.dismiss()
                                                            info { "dinda ${e.message}${response.code()} " }
                                                            toast(e.message.toString())
                                                        }

                                                    }

                                                    override fun onFailure(
                                                        call: Call<PostDataResponse>,
                                                        t: Throwable
                                                    ) {
                                                        loading(false)
                                                        toast("kesalahan jaringan")

                                                    }

                                                })

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

                override fun onFailure(call: Call<DamkarResponse>, t: Throwable) {
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
        val datePickerDialog = DatePickerDialog(requireActivity(), android.R.style.Theme_Holo_Dialog, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            binding.tahun.text = "$year"
        }, year, month, day)
        // Show Date Picker

        return datePickerDialog
    }


}