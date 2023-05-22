package com.sipmase.sipmase.pelaksana.apar

import android.app.DatePickerDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityCekAparKadaluarsaBinding
import com.sipmase.sipmase.model.AparModel
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.pelaksana.apar.QrCoderCekAparKadaluarsaActivity.Companion.jeniskadaluarsa
import com.sipmase.sipmase.pelaksana.apar.QrCoderCekAparKadaluarsaActivity.Companion.kadaluarsakadaluarsa
import com.sipmase.sipmase.pelaksana.apar.QrCoderCekAparKadaluarsaActivity.Companion.kodeaparkadaluarsa
import com.sipmase.sipmase.pelaksana.apar.QrCoderCekAparKadaluarsaActivity.Companion.lokasikadaluarsa
import com.sipmase.sipmase.session.SessionManager
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CekAparKadaluarsaActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekAparKadaluarsaBinding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog
    var currentDate: String? = null
    var tanggal_pengisian: String? = null
    var id: Int? = null
    companion object {
        var cekapar: AparModel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_cek_apar_kadaluarsa)
        val gson = Gson()
        cekapar =
            gson.fromJson(intent.getStringExtra("cekapar"), AparModel::class.java)
        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        binding.tglBaru.setOnClickListener {
            tanggalmulai()
        }
        binding.txtshift.text = sessionManager.getNama()
        binding.txtKode.text = cekapar!!.kode.toString()
        binding.scan.setOnClickListener {
            startActivity<QrCoderCekAparKadaluarsaActivity>()
        }
        binding.btnSubmit.setOnClickListener{
            val jenis = binding.spnjenis.selectedItem

            if (QrCoderCekAparKadaluarsaActivity.kodeaparkadaluarsa != null) {
                loading(true)
                api.updatekuapar(
                    QrCoderCekAparKadaluarsaActivity.hasilqrcodekadaluarsa!!,
                    jenis.toString(),
                    QrCoderCekAparKadaluarsaActivity.lokasikadaluarsa!!,
                    tanggal_pengisian!!
                ).enqueue(object : Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.body()!!.sukses == 1) {
                                loading(false)
                                toast("update apar berhasil")
                                finish()
                            } else {
                                loading(false)
                                Snackbar.make(it, "update apar gagal", Snackbar.LENGTH_SHORT)
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
            } else {
                toast("Scan APAR Terlebih dahulu")
            }
        }
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

            binding.tglBaru.text = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
            tanggal_pengisian = SimpleDateFormat("yyyy-MM-dd").format(cal.time)

        }

        DatePickerDialog(
            this, datesetLogger,
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

    override fun onStart() {
        super.onStart()
        //CEK APAR
        if (kodeaparkadaluarsa != null && jeniskadaluarsa != null && lokasikadaluarsa != null && kadaluarsakadaluarsa != null) {
            binding.txtKode.text = kodeaparkadaluarsa.toString()
            binding.txtjenis.text = jeniskadaluarsa.toString()
            binding.txtlokasi.text = lokasikadaluarsa.toString()
            binding.txttglKadaluarsa.text = kadaluarsakadaluarsa.toString()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        QrCoderCekAparKadaluarsaActivity.kodeaparkadaluarsa = null
        QrCoderCekAparKadaluarsaActivity.jeniskadaluarsa = null
        QrCoderCekAparKadaluarsaActivity.lokasikadaluarsa = null
        QrCoderCekAparKadaluarsaActivity.kadaluarsakadaluarsa = null
    }
}