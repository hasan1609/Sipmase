package com.sipmase.sipmase.pelaksana.edgblok1

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityScheduleDamkarBinding
import com.sipmase.sipmase.databinding.ActivityScheduleEdgBlok1Binding
import com.sipmase.sipmase.model.damkar.DamkarModel
import com.sipmase.sipmase.model.damkar.DamkarResponse
import com.sipmase.sipmase.model.edgblok1.EdgBlokModel
import com.sipmase.sipmase.model.edgblok1.EdgBlokResponse
import com.sipmase.sipmase.pelaksana.damkar.CekDamkarActivity
import com.sipmase.sipmase.webservice.ApiClient
import com.sipmat.sipmat.adapter.damkar.EdgBlok1PelaksanaAdapter
import com.sipmat.sipmat.adapter.damkar.ScheduleDamkarPelaksanaAdapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleEdgBlok1Activity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityScheduleEdgBlok1Binding
    var api = ApiClient.instance()
    private lateinit var mAdapter: EdgBlok1PelaksanaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_edg_blok1)
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
        api.edgblok1_pelaksana()
            .enqueue(object : Callback<EdgBlokResponse> {
                override fun onResponse(
                    call: Call<EdgBlokResponse>,
                    response: Response<EdgBlokResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<EdgBlokModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter =
                                        EdgBlok1PelaksanaAdapter(notesList, this@ScheduleEdgBlok1Activity)
                                    binding.rvscheduleapat.adapter = mAdapter
                                    mAdapter.setDialog(object : EdgBlok1PelaksanaAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: EdgBlokModel
                                        ) {
                                            val builder =
                                                AlertDialog.Builder(this@ScheduleEdgBlok1Activity)
                                            builder.setMessage("Cek Edg Blok 1 ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekEdgBlok1Activity>("cekedgblok1" to noteJson)
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

                override fun onFailure(call: Call<EdgBlokResponse>, t: Throwable) {
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