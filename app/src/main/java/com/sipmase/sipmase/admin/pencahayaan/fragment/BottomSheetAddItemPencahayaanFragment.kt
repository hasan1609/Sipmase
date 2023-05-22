package com.sipmase.sipmase.admin.pencahayaan.fragment

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.sipmase.sipmase.QRcodeActivity
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.BottomSheetAddItemPencahayaanBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetAddItemPencahayaanFragment : BottomSheetDialogFragment(), AnkoLogger {

    lateinit var binding: BottomSheetAddItemPencahayaanBinding
    var lokasi: String? = null
    var id: Int? = null
    var kode: String? = null
    private var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_item_pencahayaan, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        if (arguments != null){
            binding.lybtn.visibility = View.VISIBLE
            binding.scnqr.visibility = View.GONE

            binding.btnedit.setOnClickListener {
                val kode = arguments?.getString("kode")
                val id = arguments?.getString("id")
                val lokasi = binding.edtlokasi.text.toString().trim()
                if (kode != null && lokasi.isNotEmpty()) {
                    loading(true)
                    api.updatepencahayaan(
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
                                    toast("Update Pencahayaan Berhasil")
                                    dismiss()
                                } else {
                                    loading(false)
                                    toast("Update Pencahayaan Gagal")
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
                api.deletepencahayaan(arguments?.getString("id")!!.toInt()).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.body()!!.sukses == 1) {
                                loading(false)
                                toast("hapus pencahayaan berhasil")
                                dismiss()
                            } else {
                                loading(false)
                                toast("hapus pencahayaan gagal")
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
                if (QRcodeActivity.hasilqrcode != null && lokasi.isNotEmpty()) {
                    loading(true)
                    api.pencahayaan(
                        QRcodeActivity.hasilqrcode!!,
                        lokasi
                    ).enqueue(object : Callback<PostDataResponse> {
                        override fun onResponse(
                            call: Call<PostDataResponse>,
                            response: Response<PostDataResponse>
                        ) {
                            try {
                                if (response.body()!!.sukses == 1) {
                                    loading(false)
                                    toast("Item Pencahayaan Telah Ditambahkan")
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
            binding.txtkodepencahayaan.text = QRcodeActivity.hasilqrcode.toString()
        }
        if(arguments != null){
            binding.txtkodepencahayaan.text = arguments?.getString("kode")
            binding.edtlokasi.setText(arguments?.getString("lokasi"))
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
        QRcodeActivity.hasilqrcode = null
        binding.txtkodepencahayaan.text = null
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        arguments = null
        binding.edtlokasi.setText("")
        QRcodeActivity.hasilqrcode = null
    }
}
