package com.sipmase.sipmase.pelaksana.apar.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.apar.ScheduleAparAdapter
import com.sipmase.sipmase.adapter.apar.ScheduleAparPelaksanaAdapter
import com.sipmase.sipmase.databinding.FragmentScheduleAparPelaksanaBinding
import com.sipmase.sipmase.model.ScheduleAparPelaksanaModel
import com.sipmase.sipmase.model.ScheduleAparPelaksanaResponse
import com.sipmase.sipmase.model.ScheduleModel
import com.sipmase.sipmase.model.ScheduleResponse
import com.sipmase.sipmase.pelaksana.apar.CekAparActivity
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleAparPelaksanaFragment : Fragment(), AnkoLogger {

    lateinit var binding: FragmentScheduleAparPelaksanaBinding
    var api = ApiClient.instance()
    private lateinit var mAdapter: ScheduleAparPelaksanaAdapter
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_apar_pelaksana, container, false)
        binding.lifecycleOwner = this

        progressDialog = ProgressDialog(requireActivity())
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        getaparpelaksana()
    }

    fun getaparpelaksana() {
        binding.rvscheduleapar.layoutManager = LinearLayoutManager(requireContext())
        binding.rvscheduleapar.setHasFixedSize(true)
        (binding.rvscheduleapar.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getschedule_pelaksana()
            .enqueue(object : Callback<ScheduleAparPelaksanaResponse> {
                override fun onResponse(
                    call: Call<ScheduleAparPelaksanaResponse>,
                    response: Response<ScheduleAparPelaksanaResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<ScheduleAparPelaksanaModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil)
                                    mAdapter = ScheduleAparPelaksanaAdapter(notesList, requireContext())
                                    binding.rvscheduleapar.adapter = mAdapter
                                    mAdapter.setDialog(object : ScheduleAparPelaksanaAdapter.Dialog {
                                        override fun onClick(
                                            position: Int,
                                            note: ScheduleAparPelaksanaModel
                                        ) {
                                            val builder = AlertDialog.Builder(requireActivity())
                                            builder.setMessage("Cek APAR ? ")
                                            builder.setPositiveButton("Ya") { dialog, which ->
                                                val gson = Gson()
                                                val noteJson = gson.toJson(note)
                                                startActivity<CekAparActivity>("cekapar" to noteJson)
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

                override fun onFailure(call: Call<ScheduleAparPelaksanaResponse>, t: Throwable) {
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