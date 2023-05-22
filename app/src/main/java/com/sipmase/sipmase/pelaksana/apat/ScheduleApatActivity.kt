package com.sipmase.sipmase.pelaksana.apat

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.apat.ScheduleApatAdapter
import com.sipmase.sipmase.adapter.apat.ScheduleApatPelaksanaAdapter
import com.sipmase.sipmase.databinding.ActivityScheduleApatBinding
import com.sipmase.sipmase.model.apat.HasilApatModel
import com.sipmase.sipmase.model.apat.HasilApatResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleApatActivity : AppCompatActivity(), AnkoLogger {

    lateinit var binding: ActivityScheduleApatBinding
    var api = ApiClient.instance()
    private lateinit var mAdapter: ScheduleApatPelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_apat)
        binding.lifecycleOwner = this

        progressDialog = ProgressDialog(this)
    }

    override fun onStart() {
        super.onStart()
        getapatpelaksana()
    }


    fun getapatpelaksana() {
        binding.rvscheduleapat.layoutManager = LinearLayoutManager(this)
        binding.rvscheduleapat.setHasFixedSize(true)
        (binding.rvscheduleapat.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getschedule_pelaksana_apat()
            .enqueue(object : Callback<HasilApatResponse> {
                override fun onResponse(
                    call: Call<HasilApatResponse>,
                    response: Response<HasilApatResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<HasilApatModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        ScheduleApatPelaksanaAdapter(notesList, this@ScheduleApatActivity)
                                    binding.rvscheduleapat.adapter = mAdapter
                                    mAdapter.setDialog(object : ScheduleApatPelaksanaAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: HasilApatModel
                                        ) {
                                            val builder =
                                                AlertDialog.Builder(this@ScheduleApatActivity)
                                            builder.setMessage("Cek APAT ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekApatActivity>("cekapat" to noteJson)
                                            }


                                            builder.setNegativeButton("Tidak") { dialog, which ->

                                            }

                                            builder.show()

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
                        }
                    } catch (e: Exception) {
                        loading(false)
                        info { "dinda ${e.message}" }
                    }
                }

                override fun onFailure(call: Call<HasilApatResponse>, t: Throwable) {
                    loading(false)
                    info { "dinda ${t.message}" }
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