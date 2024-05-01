package com.sipmase.sipmase.admin.ffblok2.fragment

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
import com.sipmase.sipmase.adapter.apar.ItemAparScheduleAdapter
import com.sipmase.sipmase.databinding.BottomSheetAddScheduleBinding
import com.sipmase.sipmase.databinding.FragmentBottomSheetAddScheduleBinding
import com.sipmase.sipmase.model.AparModel
import com.sipmase.sipmase.model.AparResponse
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetAddScheduleFfBlok2Fragment : BottomSheetDialogFragment(), AnkoLogger {

    lateinit var binding: BottomSheetAddScheduleBinding
    private var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    private lateinit var selectedRadioButton: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_schedule, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        binding.txtJudul.text = "Schedule FF Blok 2"

        binding.btnsubmit.setOnClickListener {
            val selectedRadioButtonId: Int = binding.rbtw.checkedRadioButtonId
            selectedRadioButton = find(selectedRadioButtonId)
            val tw: String = selectedRadioButton.text.toString()
            val tahun = binding.edtTahun.text.toString().trim()
            val hari = binding.spnhari.selectedItem.toString()
            if (tw.isNotEmpty() && tahun.isNotEmpty()){
                loading(true)
                api.schedule_ffblok2(hari, tw, tahun).enqueue(object :
                    Callback<PostDataResponse> {
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
                                info(response)
                                toast("Tambah schedule gagal")
                            }
                        } catch (e: Exception) {
                            progressDialog.dismiss()
                            info { "dinda ${e.message}${response.code()} " }
                            toast(e.message.toString())
                        }
                    }
                    override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                        loading(false)
                        toast("Kesalahan Jaringan")
                    }
                })
            }else{
                Snackbar.make(it,"Jangan kosongi kolom",3000).show()

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

    override fun dismiss() {
        super.dismiss()
        arguments = null
        binding.edtTahun.setText("")
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        arguments = null
        binding.edtTahun.setText("")
    }
}