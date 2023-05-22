package com.sipmase.sipmase.pelaksana.edgblok2

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityScheduleEdgBlok1Binding
import com.sipmase.sipmase.databinding.ActivityScheduleEdgBlok2Binding
import com.sipmase.sipmase.model.edgblok1.EdgBlokModel
import com.sipmase.sipmase.model.edgblok1.EdgBlokResponse
import com.sipmase.sipmase.model.edgblok2.EdgBlok2Model
import com.sipmase.sipmase.model.edgblok2.EdgBlok2Response
import com.sipmase.sipmase.pelaksana.edgblok1.CekEdgBlok1Activity
import com.sipmase.sipmase.webservice.ApiClient
import com.sipmat.sipmat.adapter.damkar.EdgBlok1PelaksanaAdapter
import com.sipmat.sipmat.adapter.damkar.SchedleEdgBlok2PelaksanaAdapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleEdgBlok2Activity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityScheduleEdgBlok2Binding
    var api = ApiClient.instance()
    private lateinit var mAdapter: SchedleEdgBlok2PelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_edg_blok2)
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
        api.edgblok2_pelaksana()
            .enqueue(object : Callback<EdgBlok2Response> {
                override fun onResponse(
                    call: Call<EdgBlok2Response>,
                    response: Response<EdgBlok2Response>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<EdgBlok2Model>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        SchedleEdgBlok2PelaksanaAdapter(notesList, this@ScheduleEdgBlok2Activity)
                                    binding.rvscheduleapat.adapter = mAdapter
                                    mAdapter.setDialog(object : SchedleEdgBlok2PelaksanaAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: EdgBlok2Model
                                        ) {
                                            val builder =
                                                AlertDialog.Builder(this@ScheduleEdgBlok2Activity)
                                            builder.setMessage("Cek Edg Blok 2 ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekEdgBlok2Activity>("cekedgblok2" to noteJson)
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

                override fun onFailure(call: Call<EdgBlok2Response>, t: Throwable) {
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