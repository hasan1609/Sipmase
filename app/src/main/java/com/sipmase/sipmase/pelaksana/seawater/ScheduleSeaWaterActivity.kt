package com.sipmase.sipmase.pelaksana.seawater

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
import com.sipmase.sipmase.databinding.ActivityScheduleSeaWaterBinding
import com.sipmase.sipmase.model.ffblok.FFBlokModel
import com.sipmase.sipmase.model.ffblok.FFBlokResponse
import com.sipmase.sipmase.model.seawater.SeaWaterModel
import com.sipmase.sipmase.model.seawater.SeaWaterResponse
import com.sipmase.sipmase.pelaksana.ffblok1.CekFfBlok1Activity
import com.sipmase.sipmase.webservice.ApiClient
import com.sipmat.sipmat.adapter.damkar.SchedleFfBlok1PelaksanaAdapter
import com.sipmat.sipmat.adapter.damkar.SchedleSeaWaterPelaksanaAdapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleSeaWaterActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityScheduleSeaWaterBinding
    var api = ApiClient.instance()
    private lateinit var mAdapter: SchedleSeaWaterPelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_sea_water)
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
        api.seawater_pelaksana()
            .enqueue(object : Callback<SeaWaterResponse> {
                override fun onResponse(
                    call: Call<SeaWaterResponse>,
                    response: Response<SeaWaterResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<SeaWaterModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        SchedleSeaWaterPelaksanaAdapter(
                                            notesList,
                                            this@ScheduleSeaWaterActivity
                                        )
                                    binding.rvscheduleapat.adapter = mAdapter
                                    mAdapter.setDialog(object :
                                        SchedleSeaWaterPelaksanaAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: SeaWaterModel
                                        ) {
                                            val builder =
                                                AlertDialog.Builder(this@ScheduleSeaWaterActivity)
                                            builder.setMessage("Cek Seawater ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekSeaWaterActivity>("cekseawater" to noteJson)
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

                override fun onFailure(call: Call<SeaWaterResponse>, t: Throwable) {
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