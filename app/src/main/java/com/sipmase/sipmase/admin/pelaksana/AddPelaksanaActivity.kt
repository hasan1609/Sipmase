package com.sipmase.sipmase.admin.pelaksana

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityAddPelaksanaBinding
import com.sipmase.sipmase.databinding.ActivityPelaksanaBinding
import com.sipmase.sipmase.model.RegisterResponse
import com.sipmase.sipmase.pelaksana.edgblok1.CekEdgBlok1Activity
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPelaksanaActivity : AppCompatActivity(), AnkoLogger {

    lateinit var binding: ActivityAddPelaksanaBinding
    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_pelaksana)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)

        binding.btnSubmit.setOnClickListener{
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val nama = binding.edtNama.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty() && nama.isNotEmpty()) {
                loading(true)
                api.register(nama, username, password, 1)
                    .enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>
                        ) {
                            try {
                                if (response.isSuccessful) {
                                    if (response.body()!!.status == 1) {
                                        loading(false)
                                        toast("Berhasil tambah user")
                                        startActivity<PelaksanaActivity>()
                                    } else if (response.body()!!.status == 0) {
                                        loading(false)
                                        toast("Usernama Sudah ada")
                                    }

                                } else {
                                    loading(false)
                                    toast("Coba lagi")
                                }


                            } catch (e: Exception) {
                                loading(false)
                                info { "dinda ${e.message}${response.code()} " }
                                toast(e.toString())
                            }
                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            loading(false)
                            info { "dinda ${t.message} " }
                            toast("Periksa Koneksi anda")
                        }

                    })
            } else {
                toast("jangan kosongi kolom")
            }
        }
        binding.btnBatal.setOnClickListener{
            finish()
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