package com.sipmase.sipmase.pelaksana.ffblok1

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityScheduleEdgBlok3Binding
import com.sipmase.sipmase.databinding.ActivityScheduleFfBlok1Binding
import com.sipmase.sipmase.model.edgblok3.EdgBlok3Model
import com.sipmase.sipmase.model.edgblok3.EdgBlok3Response
import com.sipmase.sipmase.model.ffblok.FFBlokModel
import com.sipmase.sipmase.model.ffblok.FFBlokResponse
import com.sipmase.sipmase.pelaksana.edgblok3.CekEdgBlok3Activity
import com.sipmase.sipmase.webservice.ApiClient
import com.sipmat.sipmat.adapter.damkar.SchedleEdgBlok3PelaksanaAdapter
import com.sipmat.sipmat.adapter.damkar.SchedleFfBlok1PelaksanaAdapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFfBlok1Activity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityScheduleFfBlok1Binding
    var api = ApiClient.instance()
    private lateinit var mAdapter: SchedleFfBlok1PelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_ff_blok1)
        binding.lifecycleOwner = this

        progressDialog = ProgressDialog(this)
    }

    override fun onStart() {
        super.onStart()
        getschedule()
    }


    fun getschedule() {
        binding.rvscheduleapat.layoutManager = LinearLayoutManager(this)
        binding.rvscheduleapat.setHasFixedSize(true)
        (binding.rvscheduleapat.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.ffblok_pelaksana()
            .enqueue(object : Callback<FFBlokResponse> {
                override fun onResponse(
                    call: Call<FFBlokResponse>,
                    response: Response<FFBlokResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<FFBlokModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        SchedleFfBlok1PelaksanaAdapter(
                                            notesList,
                                            this@ScheduleFfBlok1Activity
                                        )
                                    binding.rvscheduleapat.adapter = mAdapter
                                    mAdapter.setDialog(object :
                                        SchedleFfBlok1PelaksanaAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: FFBlokModel
                                        ) {
                                            val builder =
                                                AlertDialog.Builder(this@ScheduleFfBlok1Activity)
                                            builder.setMessage("Cek FF Blok 1 ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekFfBlok1Activity>("cekffblok1" to noteJson)
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
                        toast("kesalahan server")
                    }
                }

                override fun onFailure(call: Call<FFBlokResponse>, t: Throwable) {
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