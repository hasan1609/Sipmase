package com.sipmase.sipmase.pelaksana.eloto

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.eloto.ElotoAdapter
import com.sipmase.sipmase.databinding.ActivityElotoBinding
import com.sipmase.sipmase.model.eloto.*
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ElotoActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityElotoBinding
    var api = ApiClient.instance()
    private lateinit var mAdapter: ElotoAdapter
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_eloto)
        binding.lifecycleOwner = this

        progressDialog = ProgressDialog(this)
//        binding.btnTambahEloto.setOnClickListener {
//            startActivity<TambahLotoActivity>()
//        }

    }

    override fun onStart() {
        super.onStart()
        getEloto()
    }


    fun getEloto() {
        binding.rvEloto.layoutManager = LinearLayoutManager(this)
        binding.rvEloto.setHasFixedSize(true)
        (binding.rvEloto.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getitemeloto()
            .enqueue(object : Callback<ResponseItemEloto> {
                override fun onResponse(
                    call: Call<ResponseItemEloto>,
                    response: Response<ResponseItemEloto>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.groupedData!!.isNotEmpty()) {
                                val notesList = mutableListOf<GroupedDataEloto>()
                                val data = response.body()
                                for (hasil in data!!.groupedData!!) {
                                    notesList.add(hasil!!)
                                    mAdapter =
                                        ElotoAdapter(notesList, this@ElotoActivity)
                                    binding.rvEloto.adapter = mAdapter
                                    mAdapter.setDialog(object : ElotoAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: GroupedDataEloto
                                        ) {
                                            val gson = Gson()
                                            val noteJson = gson.toJson(note)
                                            startActivity<DetailElotoActivity>("dataeloto" to noteJson)
                                        }

                                    })
                                    mAdapter.notifyDataSetChanged()
                                }
                            } else {
                                binding.tvkosong.visibility = View.VISIBLE
                            }
                        } else {
                            loading(false)
                            toast("gagal mendapatkan response")
                            info ( response )
                        }
                    } catch (e: Exception) {
                        loading(false)
                        info { "dinda ${e.message}" }
                        toast("kesalahan server")
                    }
                }

                override fun onFailure(call: Call<ResponseItemEloto>, t: Throwable) {
                    loading(false)
                    info { "dinda ${t.message}" }
                    toast("Periksa Koneksi Anda")
                }

            })


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
