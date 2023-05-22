package com.sipmase.sipmase.admin.pencahayaan.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.pencahayaan.ItemPencahayaanAdminAdapater
import com.sipmase.sipmase.databinding.FragmentItemPencahayaanBinding
import com.sipmase.sipmase.model.pencahayaan.PencahayaanModel
import com.sipmase.sipmase.model.pencahayaan.PencahayaanResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemPencahayaanFragment : Fragment(), AnkoLogger {

    lateinit var binding: FragmentItemPencahayaanBinding
    lateinit var mAdapter: ItemPencahayaanAdminAdapater
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    val sheet = BottomSheetAddItemPencahayaanFragment()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_pencahayaan, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())
        binding.btnTambahItemPencahayaan.setOnClickListener {
            sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddItemPencahayaanFragment")
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

    override fun onStart() {
        super.onStart()
        getpencahayaan()
    }

    fun getpencahayaan(){
        binding.rvitempencahayaan.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvitempencahayaan.setHasFixedSize(true)
        (binding.rvitempencahayaan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getpencahayaan()
            .enqueue(object : Callback<PencahayaanResponse> {
                override fun onResponse(
                    call: Call<PencahayaanResponse>,
                    response: Response<PencahayaanResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<PencahayaanModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = ItemPencahayaanAdminAdapater(notesList, requireActivity())
                                binding.rvitempencahayaan.adapter = mAdapter
                                mAdapter.setDialog(object : ItemPencahayaanAdminAdapater.Dialog{
                                    override fun onClick(position: Int, note : PencahayaanModel) {
                                        val bundle = Bundle()
                                        bundle.putString("id", note.id.toString())
                                        bundle.putString("kode",note.kode)
                                        bundle.putString("lokasi", note.lokasi)
                                        sheet.arguments = bundle

                                        sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddItemPencahayaanFragment")


                                    }

                                })
                                mAdapter.notifyDataSetChanged()
                            }
                        } else {
                            loading(false)
                            toast("gagal mendapatkan response")
                        }
                    } catch (e: Exception) {
                        loading(false)
                        info { "dinda ${e.message}" }
                        toast(e.toString())
                    }
                }

                override fun onFailure(call: Call<PencahayaanResponse>, t: Throwable) {
                    info { "dinda ${t.message}" }
                    loading(false)
                    toast(t.toString())
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