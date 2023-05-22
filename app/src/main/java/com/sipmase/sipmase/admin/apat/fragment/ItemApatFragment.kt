package com.sipmase.sipmase.admin.apat.fragment

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
import com.sipmase.sipmase.adapter.apat.ItemApatAdminAdapter
import com.sipmase.sipmase.databinding.FragmentItemApatBinding
import com.sipmase.sipmase.model.apat.ApatModel
import com.sipmase.sipmase.model.apat.ApatResponse
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemApatFragment : Fragment(), AnkoLogger {

    lateinit var binding: FragmentItemApatBinding
    lateinit var mAdapter: ItemApatAdminAdapter
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    val sheet = BottomSheetAddItemApatFragment()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_apat, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        binding.btnTambahItemApat.setOnClickListener {
            sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddItemApatFragment")
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

    fun getapat(){
        binding.rvitemapat.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvitemapat.setHasFixedSize(true)
        (binding.rvitemapat.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        loading(true)
        api.getapat()
            .enqueue(object : Callback<ApatResponse> {
                override fun onResponse(
                    call: Call<ApatResponse>,
                    response: Response<ApatResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            loading(false)
                            val notesList = mutableListOf<ApatModel>()
                            val data = response.body()
                            for (hasil in data!!.data!!) {
                                notesList.add(hasil)
                                mAdapter = ItemApatAdminAdapter(notesList, requireActivity())
                                binding.rvitemapat.adapter = mAdapter
                                mAdapter.setDialog(object : ItemApatAdminAdapter.Dialog{
                                    override fun onClick(position: Int, note : ApatModel) {
                                        val bundle = Bundle()
                                        bundle.putString("id", note.id.toString())
                                        bundle.putString("kode",note.kode)
                                        bundle.putString("lokasi", note.lokasi)
                                        bundle.putString("nobak", note.noBak)
                                        sheet.arguments = bundle

                                        sheet.show(requireActivity().supportFragmentManager, "BottomSheetAddItemApatFragment")

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

                override fun onFailure(call: Call<ApatResponse>, t: Throwable) {
                    loading(false)
                    info { "dinda ${t.message}" }
                    toast(t.toString())
                }

            })



    }

    override fun onStart() {
        super.onStart()
        getapat()
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