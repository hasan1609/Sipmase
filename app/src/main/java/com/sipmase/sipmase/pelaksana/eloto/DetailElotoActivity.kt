package com.sipmase.sipmase.pelaksana.eloto

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.eloto.DetailElotoAdapter
import com.sipmase.sipmase.databinding.ActivityDetailElotoBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.eloto.*
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailElotoActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var binding: ActivityDetailElotoBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    private lateinit var mAdapter: DetailElotoAdapter
    var dataEloto: GroupedDataEloto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_eloto)
        binding.lifecycleOwner = this
        val gson = Gson()
        dataEloto =
            gson.fromJson(
                intent.getStringExtra("dataeloto"),
                GroupedDataEloto::class.java
            )
        binding.txtTahun.text = dataEloto!!.data.toString()
        progressDialog = ProgressDialog(this)
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mAdapter.getFilter().filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter.getFilter().filter(newText)
                return true
            }
        })
        binding.btnExportEloto.setOnClickListener {
            exportPdf(dataEloto!!.tw.toString(), dataEloto!!.tahun.toString())
        }


    }

    private fun exportPdf(tw: String, tahun: String) {
        api.eloto_pdf(tw, tahun).enqueue(object :
            Callback<PostDataResponse> {
            override fun onResponse(
                call: Call<PostDataResponse>,
                response: Response<PostDataResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        loading(false)
                        if(response.body()!!.sukses == 1) {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(response.body()!!.data.toString())
                            )
                            startActivity(browserIntent)
                        }else{
                            toast("Tidak ada data yang selesai")
                        }
                    }else{
                        val gson = Gson()
                        val type = object : TypeToken<PostDataResponse>() {}.type
                        val errorResponse: PostDataResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                        info { "dinda ${errorResponse}" }
                        loading(false)
                        toast("kesalahan response")
                    }

                }catch (e :Exception){
                    loading(false)
                    info { "dinda e ${e.message}" }
                    toast("Kesalahan Server")
                }
            }

            override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                info { "dinda failure ${t.message}" }
                loading(false)
                toast(t.message.toString())
            }

        })
    }

    override fun onStart() {
        super.onStart()
        getdataeloto(dataEloto!!.tw.toString(), dataEloto!!.tahun.toString())
    }
    fun getdataeloto(tw: String, tahun: String) {
        binding.rvEloto.layoutManager = LinearLayoutManager(this)
        binding.rvEloto.setHasFixedSize(true)
        (binding.rvEloto.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.data_eloto(tw, tahun)
            .enqueue(object : Callback<ResponseEloto> {
                override fun onResponse(
                    call: Call<ResponseEloto>,
                    response: Response<ResponseEloto>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.data!!.isNotEmpty()) {
                                val notesList = mutableListOf<ElotoModel>()
                                val data = response.body()
                                for (hasil in data!!.data!!) {
                                    notesList.add(hasil!!)
                                    info(notesList)
                                    mAdapter =
                                        DetailElotoAdapter(notesList, this@DetailElotoActivity)
                                    binding.rvEloto.adapter = mAdapter
                                    mAdapter.setOnPenormalanClickListener(object : DetailElotoAdapter.OnPenormalanClickListener{
                                        override fun onPenormalanClick(
                                            position: Int,
                                            note: ElotoModel
                                        ) {
                                            val gson = Gson()
                                            val noteJson = gson.toJson(note)
                                            startActivity<TambahPenormalanActivity>("penormalan" to noteJson)
                                        }
                                    })
                                    mAdapter.setOnIsolasiClickListener(object : DetailElotoAdapter.OnIsolasiClickListener{
                                        override fun onIsolasiClick(
                                            position: Int,
                                            note: ElotoModel
                                        ) {
                                            val gson = Gson()
                                            val noteJson = gson.toJson(note)
                                            startActivity<TambahIsolasiActivity>("isolasi" to noteJson)
                                        }
                                    })
                                    mAdapter.setDialog(object : DetailElotoAdapter.Dialog{
                                        override fun onClick(
                                            position: Int,
                                            note: ElotoModel
                                        ) {
                                            val gson = Gson()
                                            val noteJson = gson.toJson(note)
                                            startActivity<DetailItemElotoActivity>("eloto" to noteJson)
                                        }
                                    })
                                    mAdapter.notifyDataSetChanged()
                                }
                            } else {
                                binding.tvkosong.visibility = View.VISIBLE
                                finish()
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

                override fun onFailure(call: Call<ResponseEloto>, t: Throwable) {
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