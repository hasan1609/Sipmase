package com.sipmase.sipmase.admin.hydrant.fragment

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
import com.sipmase.sipmase.adapter.hydrant.ItemHydrantAdminAdapter
import com.sipmase.sipmase.databinding.FragmentItemHydrantBinding
import com.sipmase.sipmase.model.hydrant.HydrantModel
import com.sipmase.sipmase.model.hydrant.ItemHydrantResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ItemHydrantFragment : Fragment(), AnkoLogger {

    lateinit var binding: FragmentItemHydrantBinding
    lateinit var mAdapter: ItemHydrantAdminAdapter
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    val sheet = BottomSheetAddItemHydrantFragment()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_hydrant, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        binding.btnTambahItemHydrant.setOnClickListener {
            sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddItemHydrantFragment")
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
        gethydrant()
    }

    fun gethydrant(){
        binding.rvitemhydrant.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvitemhydrant.setHasFixedSize(true)
        (binding.rvitemhydrant.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.itemhydrant()
            .enqueue(object : Callback<ItemHydrantResponse> {
                override fun onResponse(
                    call: Call<ItemHydrantResponse>,
                    response: Response<ItemHydrantResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<HydrantModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = ItemHydrantAdminAdapter(notesList, requireActivity())
                                binding.rvitemhydrant.adapter = mAdapter
                                mAdapter.setDialog(object : ItemHydrantAdminAdapter.Dialog{
                                    override fun onClick(position: Int, note : HydrantModel) {
                                        val bundle = Bundle()
                                        bundle.putString("id", note.id.toString())
                                        bundle.putString("kode",note.kode)
                                        bundle.putString("lokasi", note.lokasi)
                                        bundle.putString("nobox", note.noBox)
                                        sheet.arguments = bundle

                                        sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddItemHydrantFragment")

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

                override fun onFailure(call: Call<ItemHydrantResponse>, t: Throwable) {
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