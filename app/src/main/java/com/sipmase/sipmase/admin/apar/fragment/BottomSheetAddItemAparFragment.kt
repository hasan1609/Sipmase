package com.sipmase.sipmase.admin.apar.fragment

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sipmase.sipmase.QRcodeActivity
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.BottomSheetAddItemAparBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetAddItemAparFragment : BottomSheetDialogFragment(), AnkoLogger {

    lateinit var binding: BottomSheetAddItemAparBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_item_apar, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        if (arguments != null){
            binding.lybtn.visibility = View.VISIBLE
            binding.scnqr.visibility = View.GONE
            binding.tglpengisian.setOnClickListener {
                tanggalmulai()
            }
            binding.btnedit.setOnClickListener {
                val lokasi = binding.edtlokasi.text.toString().trim()
                val jenis_apar = binding.spnjenis.selectedItem.toString().trim()
                val kode = arguments?.getString("kode")
                val id = arguments?.getString("id")
                val tgl = binding.tglpengisian.text.toString()
                if (kode != null && lokasi.isNotEmpty()) {
                    loading(true)
                    api.updateapar(
                        id!!.toInt(),
                        kode,
                        jenis_apar,
                        lokasi,
                        tgl
                    ).enqueue(object : Callback<PostDataResponse> {
                        override fun onResponse(
                            call: Call<PostDataResponse>,
                            response: Response<PostDataResponse>
                        ) {
                            try {
                                if (response.body()!!.sukses == 1) {
                                    loading(false)
                                    toast("Update APAR berhasil")
                                    dismiss()
                                } else {
                                    loading(false)
                                    toast("Update APAR Gagal")
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
                    toast("Jangan kosongi kolom")
                }
            }
            binding.btnhapus.setOnClickListener{
                loading(true)
                api.hapusapar(arguments?.getString("id")!!.toInt()).enqueue(object : Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.body()!!.sukses == 1) {
                                loading(false)
                                toast("hapus apar berhasil")
                                dismiss()
                            } else {
                                loading(false)
                                toast("hapus apar gagal")
                            }


                        }catch (e : Exception){
                            progressDialog.dismiss()
                            loading(false)
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
            binding.tglpengisian.setOnClickListener {
                tanggalmulai()
            }
            binding.btnsubmit.setOnClickListener {
                val lokasi = binding.edtlokasi.text.toString().trim()
                val jenis_apar = binding.spnjenis.selectedItem.toString().trim()
                if (QRcodeActivity.hasilqrcode != null && lokasi.isNotEmpty() && tanggal_pengisian != null) {
                    loading(true)
                    api.tambahapar(
                        QRcodeActivity.hasilqrcode!!,
                        jenis_apar,
                        lokasi,
                        tanggal_pengisian!!
                    ).enqueue(object : Callback<PostDataResponse> {
                        override fun onResponse(
                            call: Call<PostDataResponse>,
                            response: Response<PostDataResponse>
                        ) {
                            try {
                                if (response.body()!!.sukses == 1) {
                                    loading(false)
                                    toast("Item Apar Telah Ditambahkan")
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

            binding.tglpengisian.text = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
            tanggal_pengisian = SimpleDateFormat("yyyy-MM-dd").format(cal.time)

        }

        DatePickerDialog(
            requireContext(), datesetLogger,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show()

    }

    override fun onStart() {
        super.onStart()
        if (QRcodeActivity.hasilqrcode != null) {
            binding.txtkodeapar.text = QRcodeActivity.hasilqrcode.toString()
        }
        if(arguments != null){
            binding.txtkodeapar.text = arguments?.getString("kode")
            binding.edtlokasi.setText(arguments?.getString("lokasi"))
            binding.tglpengisian.text = arguments?.getString("tanggal")

            val spn = arguments?.getString("jenis")
            val xmlArray: Array<String> = resources.getStringArray(R.array.jenisapar)
            binding.spnjenis.setSelection(xmlArray.indexOf(
                xmlArray.first { elem -> elem == spn }
            ))
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
        binding.txtkodeapar.text = ""
        binding.tglpengisian.text = ""
        QRcodeActivity.hasilqrcode = null
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        arguments = null
        binding.edtlokasi.setText("")
        binding.txtkodeapar.text = ""
        binding.tglpengisian.text = ""
        QRcodeActivity.hasilqrcode = null

    }
}
