package com.sipmase.sipmase.admin.hydrant.activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityDetailCekHydrantBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.hydrant.HasilHydrantModel
import com.sipmase.sipmase.webservice.ApiClient
import kotlinx.android.synthetic.main.activity_cekhydrant.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCekHydrantActivity : AppCompatActivity(), AnkoLogger {
    lateinit var cekhydrant: HasilHydrantModel
    lateinit var binding: ActivityDetailCekHydrantBinding

    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_cek_hydrant)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekhydrant =
            gson.fromJson(intent.getStringExtra("cekhydrant"), HasilHydrantModel::class.java)

        if (cekhydrant.isStatus == 0 || cekhydrant.isStatus ==2 || cekhydrant.isStatus ==3){
            binding.lyBtn.visibility = View.GONE
        }
        if (cekhydrant.isStatus ==1){
            binding.lyBtn.visibility = View.VISIBLE
        }

        binding.txtKode.text = cekhydrant.kodeHydrant.toString()
        binding.txtshift.text = cekhydrant.shift.toString()
        binding.txttgl.text = cekhydrant.tanggalCek.toString()
        binding.txtlokasi.text = cekhydrant.hydrant!!.lokasi.toString()
        binding.txtnobox.text = cekhydrant.hydrant!!.noBox.toString()

        binding.txtflashing.text = cekhydrant.flushing.toString()
        binding.txtmainValve.text = cekhydrant.mainValve.toString()
        binding.txtdichargeValve.text = cekhydrant.discharge.toString()
        binding.txtkondisiBox.text = cekhydrant.kondisiBox.toString()
        binding.txtkunciBox.text = cekhydrant.kunciBox.toString()
        binding.txtkunciF.text = cekhydrant.kunciF.toString()
        binding.txtselang.text = cekhydrant.selang.toString()
        binding.txtnozzle.text = cekhydrant.noozle.toString()
        binding.txthouseKeeping.text = cekhydrant.houseKeeping.toString()

        binding.txtketerangan.text = cekhydrant.keterangan.toString()

        binding.btnTolak.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Hydrant")
            builder.setMessage("Return Hydrant ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.return_hydrant(cekhydrant.id!!).enqueue(object : Callback<PostDataResponse> {
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
            builder.setTitle("Hydrant")
            builder.setMessage("Acc Hydrant ? ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                loading(true)
                api.acc_hydrant(cekhydrant.id!!).enqueue(object : Callback<PostDataResponse> {
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
}