package com.sipmase.sipmase.pelaksana.kebisingan

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.kebisingan.ScheduleKebisinganAdapter
import com.sipmase.sipmase.adapter.kebisingan.ScheduleKebisinganPelaksanaAdapter
import com.sipmase.sipmase.admin.kebisingan.activity.DetailCekKebisinganActivity
import com.sipmase.sipmase.databinding.ActivityScheduleKebisinganBinding
import com.sipmase.sipmase.model.kebisingan.ScheduleKebisinganModel
import com.sipmase.sipmase.model.kebisingan.ScheduleKebisinganResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleKebisinganActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityScheduleKebisinganBinding
    var api = ApiClient.instance()
    private lateinit var mAdapter: ScheduleKebisinganPelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_kebisingan)
        binding.lifecycleOwner = this

        progressDialog = ProgressDialog(this)
    }

    override fun onStart() {
        super.onStart()
        getapatpelaksana()
    }


    fun getapatpelaksana() {
        binding.rvschedulekebisingan.layoutManager = LinearLayoutManager(this)
        binding.rvschedulekebisingan.setHasFixedSize(true)
        (binding.rvschedulekebisingan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getschedule_pelaksana_kebisingan()
            .enqueue(object : Callback<ScheduleKebisinganResponse> {
                override fun onResponse(
                    call: Call<ScheduleKebisinganResponse>,
                    response: Response<ScheduleKebisinganResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if(response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<ScheduleKebisinganModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        ScheduleKebisinganPelaksanaAdapter(notesList, this@ScheduleKebisinganActivity)
                                    binding.rvschedulekebisingan.adapter = mAdapter
                                    mAdapter.setDialog(object : ScheduleKebisinganPelaksanaAdapter.Dialog {


                                        override fun onClick(
                                            position: Int,
                                            note: ScheduleKebisinganModel
                                        ) {
                                            val builder = AlertDialog.Builder(this@ScheduleKebisinganActivity)
                                            builder.setMessage("Cek kebisingan ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekKebisinganActivity>("cekkebisingan" to noteJson)
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

                override fun onFailure(call: Call<ScheduleKebisinganResponse>, t: Throwable) {
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