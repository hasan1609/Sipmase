package com.sipmase.sipmase.admin.kebisingan.fragment

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
import com.sipmase.sipmase.adapter.kebisingan.ScheduleKebisinganAdapter
import com.sipmase.sipmase.admin.kebisingan.activity.DetailCekKebisinganActivity
import com.sipmase.sipmase.databinding.FragmentHasilKebisinganBinding
import com.sipmase.sipmase.model.kebisingan.ScheduleKebisinganModel
import com.sipmase.sipmase.model.kebisingan.ScheduleKebisinganResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HasilKebisinganFragment : Fragment(), AnkoLogger {

    lateinit var binding: FragmentHasilKebisinganBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    var tahun: String? = null
    private lateinit var mAdapter: ScheduleKebisinganAdapter
    var tw: String? = null
    val sheet = BottomSheetExportKebisinganFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hasil_kebisingan, container, false)
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
                else -> binding.rvschedulekebisingan.visibility = View.GONE
            }
        }
        binding.btnExportScheduleKebisingan.setOnClickListener {
            sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddScheduleKebisinganFragment")
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
        binding.rvschedulekebisingan.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvschedulekebisingan.setHasFixedSize(true)
        (binding.rvschedulekebisingan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.gethasil_kebisingan(tw, tahun)
            .enqueue(object : Callback<ScheduleKebisinganResponse> {
                override fun onResponse(
                    call: Call<ScheduleKebisinganResponse>,
                    response: Response<ScheduleKebisinganResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<ScheduleKebisinganModel>()
                            val data = response.body()
                            if (data!!.data!!.isEmpty()) {
                                binding.btnExportScheduleKebisingan.visibility = View.GONE
                                binding.tvkosong.visibility = View.VISIBLE
                                binding.rvschedulekebisingan.visibility = View.GONE
                                toast("Data Hasil Masih Kosong")
                            } else {
                                loading(false)
                                binding.tvkosong.visibility = View.GONE
                                binding.rvschedulekebisingan.visibility = View.VISIBLE
                                binding.btnExportScheduleKebisingan.visibility = View.VISIBLE
                                binding.btnExportScheduleKebisingan.setOnClickListener {
                                    sheet.show(requireActivity().supportFragmentManager, "BottomSheetExportKebisinganFragment")
//                                    showcustomdialog()
                                }
                                for (hasil in data.data!!) {
                                    notesList.add(hasil)
                                    mAdapter = ScheduleKebisinganAdapter(notesList, requireActivity())
                                    binding.rvschedulekebisingan.adapter = mAdapter
                                    mAdapter.setDialog(object : ScheduleKebisinganAdapter.Dialog {
                                        override fun onClick(position: Int, note: ScheduleKebisinganModel) {
                                            val builder =
                                                AlertDialog.Builder(requireActivity())
                                            builder.setTitle("Cek Kebisingan ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<DetailCekKebisinganActivity>("cekkebisingan" to noteJson)
                                            }


                                            builder.setNegativeButton("Batal ?") { dialog, which ->

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

                override fun onFailure(call: Call<ScheduleKebisinganResponse>, t: Throwable) {
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