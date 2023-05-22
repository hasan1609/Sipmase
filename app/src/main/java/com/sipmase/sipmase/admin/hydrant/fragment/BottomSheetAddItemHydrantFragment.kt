package com.sipmase.sipmase.admin.hydrant.fragment

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigator
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.sipmase.sipmase.QRcodeActivity
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.BottomSheetAddItemAparBinding
import com.sipmase.sipmase.databinding.BottomSheetAddItemApatBinding
import com.sipmase.sipmase.databinding.BottomSheetAddItemHydrantBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetAddItemHydrantFragment : BottomSheetDialogFragment(), AnkoLogger {

    lateinit var binding: BottomSheetAddItemHydrantBinding
    var tanggal_pengisian: String? = null
    var lokasi: String? = null
    var id: Int? = null
    var kode: String? = null
    private var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_item_hydrant, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        if (arguments != null){
            binding.lybtn.visibility = View.VISIBLE
            binding.scnqr.visibility = View.GONE

            binding.btnedit.setOnClickListener {
                val kode = arguments?.getString("kode")
                val id = arguments?.getString("id")
                val lokasi = binding.edtlokasi.text.toString().trim()
                val nobak = binding.edtnobak.text.toString().trim()
                if (kode != null && lokasi.isNotEmpty() && nobak.isNotEmpty()) {
                    loading(true)
                    api.updatehydrant(
                        kode,
                        nobak,
                        lokasi,
                        id!!.toInt()
                    ).enqueue(object : Callback<PostDataResponse> {
                        override fun onResponse(
                            call: Call<PostDataResponse>,
                            response: Response<PostDataResponse>
                        ) {
                            try {
                                if (response.body()!!.sukses == 1) {
                                    loading(false)
                                    toast("Update Hydrant Berhasil")
                                    dismiss()
                                } else {
                                    loading(false)
                                    toast("Update Hydrant Gagal")
                                }


                            } catch (e: Exception) {
                                progressDialog.dismiss()
                                info { "dinda ${e.message}${response.code()} " }
                                toast(e.toString())
                            }

                        }

                        override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                            loading(false)
                            toast("Periksa Koneksi Anda")
                        }

                    })
                } else {
                    Snackbar.make(it, "Jangan kosongi kolom", 3000).show()
                }
            }
            binding.btnhapus.setOnClickListener{
                loading(true)
                api.deletehydrant(arguments?.getString("id")!!.toInt()).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.body()!!.sukses == 1) {
                                loading(false)
                                toast("hapus Hydrant berhasil")
                                dismiss()
                            } else {
                                loading(false)
                                toast("hapus Hydrant gagal")
                            }


                        }catch (e : Exception){
                            progressDialog.dismiss()
                            info { "dinda ${e.message }${response.code()} " }
                            toast(e.message.toString())
                        }

                    }

                    override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                        loading(false)
                        toast("kesalahan jaringan")

                    }

                })
            }
        }else {
            binding.scnqr.visibility = View.VISIBLE
            binding.btnsubmit.visibility = View.VISIBLE
            binding.scnqr.setOnClickListener {
                startActivity<QRcodeActivity>()
            }
            binding.btnsubmit.setOnClickListener {
                val lokasi = binding.edtlokasi.text.toString().trim()
                val no_bak = binding.edtnobak.text.toString().trim()
                if (QRcodeActivity.hasilqrcode != null && lokasi.isNotEmpty() && no_bak.isNotEmpty()) {
                    loading(true)
                    api.hydrant(
                        QRcodeActivity.hasilqrcode!!,
                        no_bak,
                        lokasi
                    ).enqueue(object : Callback<PostDataResponse> {
                        override fun onResponse(
                            call: Call<PostDataResponse>,
                            response: Response<PostDataResponse>
                        ) {
                            try {
                                if (response.body()!!.sukses == 1) {
                                    loading(false)
                                    toast("Item Hydrant Telah Ditambahkan")
                                    dismiss()
                                } else {
                                    loading(false)
                                    toast("Item Sudah Ada")
                                }

                            } catch (e: Exception) {
                                loading(false)
                                progressDialog.dismiss()
                                toast(e.toString())
                            }

                        }

                        override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                            loading(false)
                            toast("Periksa Koneksi Internet Anda")
                        }

                    })
                } else {
                    toast("Jangan Kosongi Kolom")
                }
            }
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        if (QRcodeActivity.hasilqrcode != null) {
            binding.txtkodehydrant.text = QRcodeActivity.hasilqrcode.toString()
        }
        if(arguments != null){
            binding.txtkodehydrant.text = arguments?.getString("kode")
            binding.edtlokasi.setText(arguments?.getString("lokasi"))
            binding.edtnobak.setText(arguments?.getString("nobox"))
        }
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
        binding.edtlokasi.setText("")
        binding.txtkodehydrant.text = ""
        QRcodeActivity.hasilqrcode = null

    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        arguments = null
        binding.edtlokasi.setText("")
        binding.txtkodehydrant.text = ""
        QRcodeActivity.hasilqrcode = null
        binding.edtnobak.setText("")

    }
}
