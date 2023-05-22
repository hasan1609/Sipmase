package com.sipmase.sipmase.admin.hydrant.fragment

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.hydrant.ScheduleHydrantAdapter
import com.sipmase.sipmase.databinding.FragmentScheduleHydrantBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.hydrant.ScheduleHydrantPelaksanaModel
import com.sipmase.sipmase.model.hydrant.ScheduleHydrantPelaksanaResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ScheduleHydrantFragment : Fragment(), AnkoLogger {

    lateinit var binding: FragmentScheduleHydrantBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    var tahun: String? = null
    private lateinit var mAdapter: ScheduleHydrantAdapter
    var tw: String? = null
    val sheet = BottomSheetAddScheduleHydrantFragment()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_hydrant, container, false)
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
                else -> binding.rvschedulehydrant.visibility = View.GONE
            }
        }

        binding.btnTambahScheduleHydrant.setOnClickListener {
            sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddScheduleHydrantFragment")
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
        binding.rvschedulehydrant.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvschedulehydrant.setHasFixedSize(true)
        (binding.rvschedulehydrant.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getschedule_hydrant(tw, tahun)
            .enqueue(object : Callback<ScheduleHydrantPelaksanaResponse> {
                override fun onResponse(
                    call: Call<ScheduleHydrantPelaksanaResponse>,
                    response: Response<ScheduleHydrantPelaksanaResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                binding.tvkosong.visibility = View.GONE
                                binding.rvschedulehydrant.visibility = View.VISIBLE
                                val notesList = mutableListOf<ScheduleHydrantPelaksanaModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter = ScheduleHydrantAdapter(notesList, requireActivity())
                                    binding.rvschedulehydrant.adapter = mAdapter
                                    mAdapter.setDialog(object : ScheduleHydrantAdapter.Dialog {
                                        override fun onClick(position: Int, note: ScheduleHydrantPelaksanaModel) {

                                            val builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                                            builder.setMessage("Hapus Schedule ? ")
                                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                                loading(true)
                                                api.hapus_schedule_hydrant(note.id!!).enqueue(object :
                                                    Callback<PostDataResponse> {
                                                    override fun onResponse(
                                                        call: Call<PostDataResponse>,
                                                        response: Response<PostDataResponse>
                                                    ) {
                                                        try {
                                                            if (response.body()!!.sukses == 1) {
                                                                loading(false)
                                                                toast("Hapus schedule berhasil")
                                                                getschedule(note.tw!!, note.tahun!!)
                                                            } else {
                                                                loading(false)
                                                                toast("Hapus schedule gagal")
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

                                            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                                            }

                                            builder.show()

                                        }

                                    })
                                    mAdapter.notifyDataSetChanged()
                                }
                            } else {
                                binding.tvkosong.visibility = View.VISIBLE
                                binding.rvschedulehydrant.visibility = View.GONE
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

                override fun onFailure(call: Call<ScheduleHydrantPelaksanaResponse>, t: Throwable) {
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