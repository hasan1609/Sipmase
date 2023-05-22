package com.sipmase.sipmase.pelaksana.ffblok2

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityScheduleFfBlok1Binding
import com.sipmase.sipmase.databinding.ActivityScheduleFfBlok2Binding
import com.sipmase.sipmase.model.ffblok.FFBlokModel
import com.sipmase.sipmase.model.ffblok.FFBlokResponse
import com.sipmase.sipmase.model.ffblok2.FFBlok2Model
import com.sipmase.sipmase.model.ffblok2.FFBlok2Response
import com.sipmase.sipmase.pelaksana.ffblok1.CekFfBlok1Activity
import com.sipmase.sipmase.webservice.ApiClient
import com.sipmat.sipmat.adapter.damkar.SchedleFfBlok1PelaksanaAdapter
import com.sipmat.sipmat.adapter.damkar.SchedleFfBlok2PelaksanaAdapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFfBlok2Activity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityScheduleFfBlok2Binding
    var api = ApiClient.instance()
    private lateinit var mAdapter: SchedleFfBlok2PelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_ff_blok2)
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
        api.ffblok2_pelaksana()
            .enqueue(object : Callback<FFBlok2Response> {
                override fun onResponse(
                    call: Call<FFBlok2Response>,
                    response: Response<FFBlok2Response>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<FFBlok2Model>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        SchedleFfBlok2PelaksanaAdapter(
                                            notesList,
                                            this@ScheduleFfBlok2Activity
                                        )
                                    binding.rvscheduleapat.adapter = mAdapter
                                    mAdapter.setDialog(object :
                                        SchedleFfBlok2PelaksanaAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: FFBlok2Model
                                        ) {
                                            val builder =
                                                AlertDialog.Builder(this@ScheduleFfBlok2Activity)
                                            builder.setMessage("Cek FF Blok 2 ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekFfBlok2Activity>("cekffblok2" to noteJson)
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

                override fun onFailure(call: Call<FFBlok2Response>, t: Throwable) {
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