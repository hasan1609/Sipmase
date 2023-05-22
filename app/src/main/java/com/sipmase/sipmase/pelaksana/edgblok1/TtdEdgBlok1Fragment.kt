package com.sipmase.sipmase.pelaksana.edgblok1

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.FragmentBottomSheetExportBinding
import com.sipmase.sipmase.databinding.FragmentTtdBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.webservice.ApiClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class TtdEdgBlok1Fragment : BottomSheetDialogFragment(), AnkoLogger {

    lateinit var binding : FragmentTtdBinding
    private var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    var ttd_k3: MultipartBody.Part? = null
    var signaturePadSupervisor: MultipartBody.Part? = null
    var signaturePadOperator: MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ttd, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())

        if (arguments?.getString("judul") == "spv"){
            binding.judul.text = "Ttd Supervisor"
        } else if (arguments?.getString("judul") == "operator") {
            binding.judul.text = "Ttd Operator"
        }else if (arguments?.getString("judul") == "k3") {
            binding.judul.text = "Ttd K3"
        }

        binding.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
            }
            override fun onSigned() {
                //create a file to write bitmap data
                //create a file to write bitmap data
                val f: File = File(requireActivity().cacheDir, "foto")
                f.createNewFile()
                //Convert bitmap
                //ini bitmapnya
                val bitmap = binding.signaturePad.transparentSignatureBitmap
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0/*ignored for PNG*/, bos)
                val bitmapdata = bos.toByteArray()
                val foto = bitmapToBytes(bitmap)

                val reqFile = RequestBody.create(MediaType.parse("image/*"), foto)
                ttd_k3 = MultipartBody.Part.createFormData("k3_ttd", f.name, reqFile)
                signaturePadSupervisor = MultipartBody.Part.createFormData("supervisor_ttd", f.name, reqFile)
                signaturePadOperator = MultipartBody.Part.createFormData("operator_ttd", f.name, reqFile)
            }
            override fun onClear() {
            }
        })
        binding.btnsubmit.setOnClickListener {
            loading(true)
            val nama = binding.edtNama.text.toString()
            val requestid: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                arguments?.getString("id")
            )
            val requestnama: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                nama
            )
            if (arguments?.getString("judul") == "spv") {
                posspv(requestid, requestnama)
            } else if (arguments?.getString("judul") == "operator") {
                posOperator(requestid, requestnama)
            } else if (arguments?.getString("judul") == "k3") {
                postK3(requestid, requestnama)

            }
        }



        return binding.root
    }

    fun bitmapToBytes(photo: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding.edtNama.setText("")
        binding.signaturePad.clear()
    }

    fun postK3(requestid: RequestBody, requestnama: RequestBody){
        api.ttdk3_edgblok1(ttd_k3, requestnama, requestid).enqueue(object :
            Callback<PostDataResponse> {
            override fun onResponse(
                call: Call<PostDataResponse>,
                response: Response<PostDataResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        loading(false)
                        toast("Ttd telah Diupload")
                        dismiss()
                    }else{
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

    fun posspv(requestid: RequestBody, requestnama: RequestBody) {
        api.ttdspv_edgblok1(signaturePadSupervisor, requestnama, requestid).enqueue(object :
            Callback<PostDataResponse> {
            override fun onResponse(
                call: Call<PostDataResponse>,
                response: Response<PostDataResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        loading(false)
                        toast("Ttd telah Diupload")
                        dismiss()
                    }else{
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

    fun posOperator(requestid: RequestBody, requestnama: RequestBody) {
        api.ttdoperator_edgblok1(signaturePadOperator, requestnama, requestid).enqueue(object :
            Callback<PostDataResponse> {
            override fun onResponse(
                call: Call<PostDataResponse>,
                response: Response<PostDataResponse>
            ) {
                try {
                    if (response.isSuccessful){
                        loading(false)
                        toast("Ttd telah Diupload")
                        dismiss()
                    }else{
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

}