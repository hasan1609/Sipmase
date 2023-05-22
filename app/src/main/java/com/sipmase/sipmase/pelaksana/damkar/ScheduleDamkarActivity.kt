package com.sipmase.sipmase.pelaksana.damkar

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.ambulance.ScheduleAmbulancePelaksanaAdapter
import com.sipmase.sipmase.databinding.ActivityScheduleDamkarBinding
import com.sipmase.sipmase.model.ambulance.AmbulanceModel
import com.sipmase.sipmase.model.ambulance.AmbulanceResponse
import com.sipmase.sipmase.model.damkar.DamkarModel
import com.sipmase.sipmase.model.damkar.DamkarResponse
import com.sipmase.sipmase.pelaksana.ambulance.CekAmbulanceActivity
import com.sipmase.sipmase.webservice.ApiClient
import com.sipmat.sipmat.adapter.damkar.ScheduleDamkarPelaksanaAdapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleDamkarActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityScheduleDamkarBinding
    var api = ApiClient.instance()
    private lateinit var mAdapter: ScheduleDamkarPelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_damkar)
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
        api.damkar_pelaksana()
            .enqueue(object : Callback<DamkarResponse> {
                override fun onResponse(
                    call: Call<DamkarResponse>,
                    response: Response<DamkarResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<DamkarModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        ScheduleDamkarPelaksanaAdapter(notesList, this@ScheduleDamkarActivity)
                                    binding.rvscheduleapat.adapter = mAdapter
                                    mAdapter.setDialog(object : ScheduleDamkarPelaksanaAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: DamkarModel
                                        ) {
                                            val builder =
                                                AlertDialog.Builder(this@ScheduleDamkarActivity)
                                            builder.setMessage("Cek Ambulance ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekDamkarActivity>("cekdamkar" to noteJson)
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

                override fun onFailure(call: Call<DamkarResponse>, t: Throwable) {
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