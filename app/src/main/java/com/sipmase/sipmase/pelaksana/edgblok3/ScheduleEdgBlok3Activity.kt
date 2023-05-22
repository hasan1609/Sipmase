package com.sipmase.sipmase.pelaksana.edgblok3

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityScheduleEdgBlok2Binding
import com.sipmase.sipmase.databinding.ActivityScheduleEdgBlok3Binding
import com.sipmase.sipmase.model.edgblok2.EdgBlok2Model
import com.sipmase.sipmase.model.edgblok2.EdgBlok2Response
import com.sipmase.sipmase.model.edgblok3.EdgBlok3Model
import com.sipmase.sipmase.model.edgblok3.EdgBlok3Response
import com.sipmase.sipmase.pelaksana.edgblok1.CekEdgBlok1Activity
import com.sipmase.sipmase.webservice.ApiClient
import com.sipmat.sipmat.adapter.damkar.SchedleEdgBlok2PelaksanaAdapter
import com.sipmat.sipmat.adapter.damkar.SchedleEdgBlok3PelaksanaAdapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleEdgBlok3Activity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityScheduleEdgBlok3Binding
    var api = ApiClient.instance()
    private lateinit var mAdapter: SchedleEdgBlok3PelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_edg_blok3)
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
        api.edgblok3_pelaksana()
            .enqueue(object : Callback<EdgBlok3Response> {
                override fun onResponse(
                    call: Call<EdgBlok3Response>,
                    response: Response<EdgBlok3Response>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<EdgBlok3Model>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        SchedleEdgBlok3PelaksanaAdapter(
                                            notesList,
                                            this@ScheduleEdgBlok3Activity
                                        )
                                    binding.rvscheduleapat.adapter = mAdapter
                                    mAdapter.setDialog(object :
                                        SchedleEdgBlok3PelaksanaAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: EdgBlok3Model
                                        ) {
                                            val builder =
                                                AlertDialog.Builder(this@ScheduleEdgBlok3Activity)
                                            builder.setMessage("Cek Edg Blok 3 ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekEdgBlok3Activity>("cekedgblok3" to noteJson)
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

                override fun onFailure(call: Call<EdgBlok3Response>, t: Throwable) {
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
