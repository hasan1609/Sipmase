package com.sipmase.sipmase.admin.apar.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.apar.ItemAparAdminAdapter
import com.sipmase.sipmase.databinding.FragmentItemAparBinding
import com.sipmase.sipmase.model.AparModel
import com.sipmase.sipmase.model.AparResponse
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemAparFragment : Fragment(), AnkoLogger {

    private lateinit var mAdapter: ItemAparAdminAdapter
    lateinit var binding: FragmentItemAparBinding
    lateinit var progressDialog: ProgressDialog
    var api = ApiClient.instance()
    val sheet = BottomSheetAddItemAparFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_apar, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(activity)

        binding.btnTambahItemApar.setOnClickListener {
            sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddItemAparFragment")
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

        return binding.root
    }

    fun getapar(){
        loading(true)
        binding.rvitemapar.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvitemapar.setHasFixedSize(true)
        (binding.rvitemapar.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        api.getapar()
            .enqueue(object : Callback<AparResponse> {
                override fun onResponse(
                    call: Call<AparResponse>,
                    response: Response<AparResponse>
                ) {
                    try {
                        loading(false)
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<AparModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = ItemAparAdminAdapter(notesList, requireActivity())
                                binding.rvitemapar.adapter = mAdapter
                                mAdapter.setDialog(object : ItemAparAdminAdapter.Dialog{
                                    override fun onClick(position: Int, note : AparModel) {
                                        val bundle = Bundle()
                                        bundle.putString("jenis", note.jenis)
                                        bundle.putString("id", note.id.toString())
                                        bundle.putString("kode",note.kode)
                                        bundle.putString("lokasi", note.lokasi)
                                        bundle.putString("tanggal", note.tglPengisian)
                                        sheet.arguments = bundle

                                        sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddItemFragment")

                                    }

                                })
                                mAdapter.notifyDataSetChanged()
                            }
                        } else {
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        loading(false)
                        info { "dinda ${e.message}" }
                        toast("Periksa Koneksi Anda")
                    }
                }

                override fun onFailure(call: Call<AparResponse>, t: Throwable) {
                    loading(false)
                    toast("Periksa Koneksi Anda")
                    info { "dinda ${t.message}" }
                }

            })



    }

    override fun onStart() {
        super.onStart()
        getapar()
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