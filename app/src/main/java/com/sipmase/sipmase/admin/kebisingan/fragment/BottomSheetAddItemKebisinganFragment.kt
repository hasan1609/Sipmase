package com.sipmase.sipmase.admin.kebisingan.fragment

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
import com.sipmase.sipmase.databinding.BottomSheetAddItemKebisinganBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetAddItemKebisinganFragment : BottomSheetDialogFragment(), AnkoLogger {

    lateinit var binding: BottomSheetAddItemKebisinganBinding
    var lokasi: String? = null
    var id: Int? = null
    var kode: String? = null
    private var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_item_kebisingan, container, false)
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
                    api.updatekebisingan(
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
                                    toast("Update Kebisingan Berhasil")
                                    dismiss()
                                } else {
                                    loading(false)
                                    toast("Update Kebisingan Gagal")
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
                api.deletekebisingan(arguments?.getString("id")!!.toInt()).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.body()!!.sukses == 1) {
                                loading(false)
                                toast("hapus kebisingan berhasil")
                                dismiss()
                            } else {
                                loading(false)
                                toast("hapus kebisingan gagal")
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
                    api.kebisingan(
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
                                    toast("Item Kebisingan Telah Ditambahkan")
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
            binding.txtkodekebisingan.text = QRcodeActivity.hasilqrcode.toString()
        }
        if(arguments != null){
            binding.txtkodekebisingan.text = arguments?.getString("kode")
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
        binding.txtkodekebisingan.text =""
        QRcodeActivity.hasilqrcode = null

    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        arguments = null
        binding.edtlokasi.setText("")
        binding.txtkodekebisingan.text =""
        QRcodeActivity.hasilqrcode = null

    }
}
