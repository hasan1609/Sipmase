package com.sipmase.sipmase.admin.kebisingan.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.adapter.hydrant.ItemHydrantAdminAdapter
import com.sipmase.sipmase.adapter.kebisingan.ItemKebisinganAdminAdapter
import com.sipmase.sipmase.admin.hydrant.fragment.BottomSheetAddItemHydrantFragment
import com.sipmase.sipmase.databinding.FragmentItemHydrantBinding
import com.sipmase.sipmase.databinding.FragmentItemKebisinganBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.kebisingan.KebisinganModel
import com.sipmase.sipmase.model.kebisingan.KebisinganResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemKebisinganFragment : Fragment(), AnkoLogger {

    lateinit var binding: FragmentItemKebisinganBinding
    lateinit var mAdapter: ItemKebisinganAdminAdapter
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    val sheet = BottomSheetAddItemKebisinganFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_kebisingan, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        binding.btnTambahItemKebisinngan.setOnClickListener {
            sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddItemKebisinganFragment")
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
        getkebisingan()
    }

    fun getkebisingan(){
        binding.rvitemkebisingan.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvitemkebisingan.setHasFixedSize(true)
        (binding.rvitemkebisingan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getkebisingan()
            .enqueue(object : Callback<KebisinganResponse> {
                override fun onResponse(
                    call: Call<KebisinganResponse>,
                    response: Response<KebisinganResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<KebisinganModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = ItemKebisinganAdminAdapter(notesList, requireActivity())
                                binding.rvitemkebisingan.adapter = mAdapter
                                mAdapter.setDialog(object : ItemKebisinganAdminAdapter.Dialog{
                                    override fun onClick(position: Int, note : KebisinganModel) {
                                        val bundle = Bundle()
                                        bundle.putString("id", note.id.toString())
                                        bundle.putString("kode",note.kode)
                                        bundle.putString("lokasi", note.lokasi)
                                        sheet.arguments = bundle

                                        sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddItemKebisinganFragment")
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

                override fun onFailure(call: Call<KebisinganResponse>, t: Throwable) {
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