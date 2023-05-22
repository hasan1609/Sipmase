package com.sipmase.sipmase.admin.hydrant.fragment

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.apat.ItemApatScheduleAdapter
import com.sipmase.sipmase.adapter.hydrant.ItemHydrantScheduleAdapter
import com.sipmase.sipmase.databinding.FragmentBottomSheetAddScheduleBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.hydrant.HydrantModel
import com.sipmase.sipmase.model.hydrant.ItemHydrantResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetAddScheduleHydrantFragment : BottomSheetDialogFragment(), AnkoLogger {

    lateinit var binding: FragmentBottomSheetAddScheduleBinding
    var tanggal_cek: String? = null
    var lokasi: String? = null
    var id: Int? = null
    var kode: String? = null
    private var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    lateinit var itemHydrantScheduleAdapter: ItemHydrantScheduleAdapter
    var sb: StringBuilder? = null
    private lateinit var selectedRadioButton: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_add_schedule, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        binding.txtJudul.text = "Schedule Hydrant"

        binding.tglPengecekan.setOnClickListener {
            tanggalmulai()
        }
        pilihhydrant()
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                itemHydrantScheduleAdapter.getFilter().filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                itemHydrantScheduleAdapter.getFilter().filter(newText)
                return true
            }

        })
        binding.btnsubmit.setOnClickListener {
            sb = StringBuilder()
            var i = 0
            while (i < itemHydrantScheduleAdapter.lista.size) {
                val spiritualTeacher = itemHydrantScheduleAdapter.lista[i]
                sb!!.append(spiritualTeacher.kode)
                if (i != itemHydrantScheduleAdapter.lista.size - 1) {
                    sb!!.append("n")
                }
                i++

            }
            val selectedRadioButtonId: Int = binding.rbtw.checkedRadioButtonId
            val tahun = binding.edtTahun.text.toString().trim()
            if (selectedRadioButtonId != -1 && tanggal_cek!=null && itemHydrantScheduleAdapter.lista.size > 0 && tahun.isNotEmpty()){
                loading(true)
                selectedRadioButton = find(selectedRadioButtonId)
                val tw: String = selectedRadioButton.text.toString()
                api.schedule_hydrant(sb.toString(),tw,tahun,tanggal_cek!!).enqueue(object :Callback<PostDataResponse>{
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.body()!!.sukses == 1) {
                                loading(false)
                                toast("Tambah schedule berhasil")
                                dismiss()
                            } else {
                                loading(false)
                                Snackbar.make(it, "Tambah schedule gagal", Snackbar.LENGTH_SHORT)
                                    .show()
                            }


                        } catch (e: Exception) {
                            progressDialog.dismiss()
                            info { "dinda ${e.message}${response.code()} " }
                        }

                    }

                    override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                        loading(false)
                        Snackbar.make(it, "Kesalahan jaringan", Snackbar.LENGTH_SHORT).show()

                    }

                })
            }else{
                Snackbar.make(it,"Jangan kosongi kolom",3000).show()

            }

        }
        return binding.root
    }

    private fun pilihhydrant() {
        binding.rvItem.setHasFixedSize(true)
        binding.rvItem.layoutManager = LinearLayoutManager(requireActivity())
        (binding.rvItem.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.itemhydrant().enqueue(object : Callback<ItemHydrantResponse> {
            override fun onFailure(call: Call<ItemHydrantResponse>, t: Throwable) {
            }
            override fun onResponse(
                call: Call<ItemHydrantResponse>,
                response: Response<ItemHydrantResponse>
            ) {
                loading(false)
                if (response.isSuccessful) {
                    val notesList = mutableListOf<HydrantModel>()
                    val data = response.body()
                    for (hasil in data!!.data!!) {
                        notesList.add(hasil)
                        itemHydrantScheduleAdapter = ItemHydrantScheduleAdapter(notesList, requireActivity())
                        binding.rvItem.adapter = itemHydrantScheduleAdapter
                        itemHydrantScheduleAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        )
    }

    private fun tanggalmulai() {
        val cal = Calendar.getInstance()
        val calend = Calendar.getInstance()
        val datesetLogger = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            calend.set(Calendar.YEAR, year)
            calend.set(Calendar.MONTH, month)
            calend.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            binding.tglPengecekan.text = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
            tanggal_cek = SimpleDateFormat("yyyy-MM-dd").format(cal.time)

        }

        DatePickerDialog(
            requireActivity(), datesetLogger,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show()

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

    override fun dismiss() {
        super.dismiss()
        arguments = null
        binding.tglPengecekan.setText("")
        binding.edtTahun.setText("")
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        arguments = null
        binding.tglPengecekan.setText("")
        binding.edtTahun.setText("")
    }
}