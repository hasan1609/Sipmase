package com.sipmase.sipmase.admin.pelaksana

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.pelaksana.PelaksanaAdapter
import com.sipmase.sipmase.databinding.ActivityPelaksanaBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.UsersModel
import com.sipmase.sipmase.model.UsersResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PelaksanaActivity : AppCompatActivity(), AnkoLogger {

    lateinit var binding: ActivityPelaksanaBinding
    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()
    private lateinit var mAdapter: PelaksanaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pelaksana)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(this)

        binding.btnTambahItemPelaksanan.setOnClickListener {
            startActivity<AddPelaksanaActivity>()
        }
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

    override fun onStart() {
        super.onStart()
        getusers()
    }

    fun getusers() {
        binding.rvitempelaksana.layoutManager = LinearLayoutManager(this)
        binding.rvitempelaksana.setHasFixedSize(true)
        (binding.rvitempelaksana.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getusers(1)
            .enqueue(object : Callback<UsersResponse> {
                override fun onResponse(
                    call: Call<UsersResponse>,
                    response: Response<UsersResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<UsersModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter =
                                    PelaksanaAdapter(notesList, this@PelaksanaActivity)
                                binding.rvitempelaksana.adapter = mAdapter
                                mAdapter.setDialog(object : PelaksanaAdapter.Dialog{
                                    override fun onClick(position: Int, note : UsersModel) {
                                        val builder = AlertDialog.Builder(this@PelaksanaActivity)
                                        builder.setMessage("Hapus Pelaksana ? ")
                                        builder.setPositiveButton("Hapus") { dialog, which ->
                                            loading(true)
                                            api.deleteusers(note.id!!).enqueue(object :
                                                Callback<PostDataResponse> {
                                                override fun onResponse(
                                                    call: Call<PostDataResponse>,
                                                    response: Response<PostDataResponse>
                                                ) {
                                                    try {
                                                        if (response.body()!!.sukses == 1) {
                                                            loading(false)
                                                            toast("Hapus user berhasil")
                                                        } else {
                                                            loading(false)
                                                            toast("Hapus user gagal")
                                                        }


                                                    } catch (e: Exception) {
                                                        progressDialog.dismiss()
                                                        info { "dinda ${e.message}${response.code()} " }
                                                        toast(e.message.toString())
                                                    }

                                                }

                                                override fun onFailure(
                                                    call: Call<PostDataResponse>,
                                                    t: Throwable
                                                ) {
                                                    loading(false)
                                                    toast("kesalahan jaringan")

                                                }

                                            })


                                        }


                                        builder.setNegativeButton("Batal") { dialog, which ->
                                            dialog.dismiss()
                                        }
                                        builder.setCancelable(true)

                                        builder.show()

                                    }

                                })
                                mAdapter.notifyDataSetChanged()
                            }
                        } else {
                            loading(false)
                            toast("Gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        loading(false)
                        info { "dinda ${e.message}" }
                        toast(e.toString())
                    }
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    loading(false)
                    toast("Periksa Koneksi Anda")
                    info { "dinda ${t.message}" }
                }

            })
    }
}