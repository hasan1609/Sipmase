package com.sipmase.sipmase.pelaksana.hydrant

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.hydrant.ScheduleHydrantAdapter
import com.sipmase.sipmase.adapter.hydrant.ScheduleHydrantPelaksanaAdapter
import com.sipmase.sipmase.admin.hydrant.activity.DetailCekHydrantActivity
import com.sipmase.sipmase.databinding.ActivityScheduleHydrantBinding
import com.sipmase.sipmase.model.hydrant.ScheduleHydrantPelaksanaModel
import com.sipmase.sipmase.model.hydrant.ScheduleHydrantPelaksanaResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleHydrantActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityScheduleHydrantBinding
    var api = ApiClient.instance()
    private lateinit var mAdapter: ScheduleHydrantPelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_hydrant)
        binding.lifecycleOwner = this

        progressDialog = ProgressDialog(this)
    }

    override fun onStart() {
        super.onStart()
        gethydrantpelaksana()
    }


    fun gethydrantpelaksana() {
        binding.rvschedulehydrant.layoutManager = LinearLayoutManager(this)
        binding.rvschedulehydrant.setHasFixedSize(true)
        (binding.rvschedulehydrant.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getschedule_pelaksana_hydrant()
            .enqueue(object : Callback<ScheduleHydrantPelaksanaResponse> {
                override fun onResponse(
                    call: Call<ScheduleHydrantPelaksanaResponse>,
                    response: Response<ScheduleHydrantPelaksanaResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if(response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<ScheduleHydrantPelaksanaModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        ScheduleHydrantPelaksanaAdapter(notesList, this@ScheduleHydrantActivity)
                                    binding.rvschedulehydrant.adapter = mAdapter
                                    mAdapter.setDialog(object : ScheduleHydrantPelaksanaAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: ScheduleHydrantPelaksanaModel
                                        ) {
                                            val builder = AlertDialog.Builder(this@ScheduleHydrantActivity)
                                            builder.setMessage("Cek Hydrant ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekhydrantActivity>("cekhydrant" to noteJson)
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

                override fun onFailure(call: Call<ScheduleHydrantPelaksanaResponse>, t: Throwable) {
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