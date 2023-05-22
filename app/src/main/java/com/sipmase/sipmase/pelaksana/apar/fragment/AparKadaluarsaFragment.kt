package com.sipmase.sipmase.pelaksana.apar.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.apar.ItemAparKadaluarsaAdapter
import com.sipmase.sipmase.databinding.FragmentAparKadaluarsaBinding
import com.sipmase.sipmase.model.AparModel
import com.sipmase.sipmase.model.AparResponse
import com.sipmase.sipmase.pelaksana.apar.CekAparKadaluarsaActivity
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AparKadaluarsaFragment : Fragment(), AnkoLogger {

    lateinit var binding: FragmentAparKadaluarsaBinding
    var api = ApiClient.instance()
    private lateinit var mAdapter: ItemAparKadaluarsaAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_apar_kadaluarsa, container, false)
        binding.lifecycleOwner = this

        aparkadaluarsa()

        progressDialog = ProgressDialog(requireActivity())
        return binding.root
    }


    fun aparkadaluarsa() {
        binding.rvitemapar.layoutManager = LinearLayoutManager(requireContext())
        binding.rvitemapar.setHasFixedSize(true)
        (binding.rvitemapar.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        api.apar_kadaluarsa()
            .enqueue(object : Callback<AparResponse> {
                override fun onResponse(
                    call: Call<AparResponse>,
                    response: Response<AparResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val notesList = mutableListOf<AparModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = ItemAparKadaluarsaAdapter(notesList, requireContext())
                                binding.rvitemapar.adapter = mAdapter
                                mAdapter.setDialog(object : ItemAparKadaluarsaAdapter.Dialog {
                                    override fun onClick(
                                        position: Int,
                                        note: AparModel
                                    ) {
                                        val builder = AlertDialog.Builder(requireActivity())
                                        builder.setMessage("Cek APAR ? ")
                                        builder.setPositiveButton("Ya") { dialog, which ->
                                            val gson = Gson()
                                            val noteJson = gson.toJson(note)
                                            startActivity<CekAparKadaluarsaActivity>("cekapar" to noteJson)
                                        }


                                        builder.setNegativeButton("Tidak") { dialog, which ->

                                        }

                                        builder.show()

                                    }

                                })
                                mAdapter.notifyDataSetChanged()
                            }
                        } else {
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        info { "dinda ${e.message}" }
                    }
                }

                override fun onFailure(call: Call<AparResponse>, t: Throwable) {
                    info { "dinda ${t.message}" }
                }

            })
    }
}