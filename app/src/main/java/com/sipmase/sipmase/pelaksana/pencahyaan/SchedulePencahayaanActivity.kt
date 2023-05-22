package com.sipmase.sipmase.pelaksana.pencahyaan

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.pencahayaan.SchedulePencahayaanAdapter
import com.sipmase.sipmase.adapter.pencahayaan.SchedulePencahayaanPelaksanaAdapter
import com.sipmase.sipmase.databinding.ActivitySchedulePencahayaanBinding
import com.sipmase.sipmase.model.pencahayaan.SchedulePencahayaanModel
import com.sipmase.sipmase.model.pencahayaan.SchedulePencahayaanResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SchedulePencahayaanActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivitySchedulePencahayaanBinding
    var api = ApiClient.instance()
    private lateinit var mAdapter: SchedulePencahayaanPelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_pencahayaan)
        binding.lifecycleOwner = this

        progressDialog = ProgressDialog(this)
    }

    override fun onStart() {
        super.onStart()
        getpencahayaanpelaksana()
    }


    fun getpencahayaanpelaksana() {
        binding.rvschedulepencahayaan.layoutManager = LinearLayoutManager(this)
        binding.rvschedulepencahayaan.setHasFixedSize(true)
        (binding.rvschedulepencahayaan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getschedule_pelaksana_pencahayaan()
            .enqueue(object : Callback<SchedulePencahayaanResponse> {
                override fun onResponse(
                    call: Call<SchedulePencahayaanResponse>,
                    response: Response<SchedulePencahayaanResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if(response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<SchedulePencahayaanModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        SchedulePencahayaanPelaksanaAdapter(notesList, this@SchedulePencahayaanActivity)
                                    binding.rvschedulepencahayaan.adapter = mAdapter
                                    mAdapter.setDialog(object : SchedulePencahayaanPelaksanaAdapter.Dialog {


                                        override fun onClick(
                                            position: Int,
                                            note: SchedulePencahayaanModel
                                        ) {
                                            val builder = AlertDialog.Builder(this@SchedulePencahayaanActivity)
                                            builder.setMessage("Cek pencahayaan ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekPencahayaanActivity>("cekpencahayaan" to noteJson)
                                            }


                                            builder.setNegativeButton("Tidak") { dialog, which ->

                                            }

                                            builder.show()

                                        }

                                    })
                                    mAdapter.notifyDataSetChanged()
                                }
                            }else{
                                binding.tvkosong.visibility = View.VISIBLE
                            }
                        } else {
                            loading(false)
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        loading(false)
                        info { "dinda ${e.message}" }
                    }
                }

                override fun onFailure(call: Call<SchedulePencahayaanResponse>, t: Throwable) {
                    loading(false)
                    info { "dinda ${t.message}" }
                }

            })


    }

    fun loading(status : Boolean){
        if (status){
            progressDialog.setTitle("Loading...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        }else{
            progressDialog.dismiss()
        }
    }

}