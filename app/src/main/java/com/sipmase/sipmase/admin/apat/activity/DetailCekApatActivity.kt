package com.sipmase.sipmase.admin.apat.activity

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityDetailCekApatBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.apat.HasilApatModel
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class DetailCekApatActivity : AppCompatActivity(), AnkoLogger {
    lateinit var cekapat: HasilApatModel
    lateinit var binding: ActivityDetailCekApatBinding
    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_apat)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekapat =
            gson.fromJson(intent.getStringExtra("cekapat"), HasilApatModel::class.java)

        if (cekapat.isStatus == 0 || cekapat.isStatus ==2 || cekapat.isStatus ==3){
            binding.lyBtn.visibility = View.GONE
        }
        if (cekapat.isStatus ==1){
            binding.lyBtn.visibility = View.VISIBLE
        }

        binding.txtKode.text = cekapat.kodeApat.toString()
        binding.txtshift.text = cekapat.shift.toString()
        binding.txttgl.text = cekapat.tanggalCek.toString()
        binding.txtlokasi.text = cekapat.apat!!.lokasi.toString()
        binding.txtnobak.text = cekapat.apat!!.noBak.toString()
        binding.txtbak.text = cekapat.bak.toString()
        binding.txtpasir.text = cekapat.pasir.toString()
        binding.txtkarung.text = cekapat.karung.toString()
        binding.txtember.text = cekapat.ember.toString()
        binding.txtsekop.text = cekapat.sekop.toString()
        binding.txtgantungan.text = cekapat.gantungan.toString()
        binding.txtketerangan.text = cekapat.keterangan.toString()

        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("APAT")
            builder.setMessage("Return APAT ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_apat(cekapat.id!!).enqueue(object : Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.body()!!.sukses == 1) {
                                loading(false)
                                toast("return schedule berhasil")
                                finish()
                            } else {
                                loading(false)
                                Snackbar.make(it, "Coba Lagi", Snackbar.LENGTH_SHORT)
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
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()
        }

        binding.btnTerima.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("APAT")
            builder.setMessage("Acc APAT ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_apat(cekapat.id!!).enqueue(object : Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.body()!!.sukses == 1) {
                                loading(false)
                                toast("acc schedule berhasil")
                                finish()
                            } else {
                                loading(false)
                                Snackbar.make(it, "acc  gagal", Snackbar.LENGTH_SHORT)
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
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()

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
    fun bitmapToBytes(photo: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}