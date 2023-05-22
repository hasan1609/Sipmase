package com.sipmase.sipmase.admin.apat.fragment

import android.app.ProgressDialog
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

class BottomSheetExportApatFragment : BottomSheetDialogFragment(), AnkoLogger {

    lateinit var binding : FragmentBottomSheetExportBinding
    private var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    private lateinit var selectedRadioButton: RadioButton
    var body:MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_export, container, false)
        binding.lifecycleOwner = this
        progressDialog = ProgressDialog(requireActivity())


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
                body = MultipartBody.Part.createFormData("foto", f.name, reqFile)
            }
            override fun onClear() {
            }
        })

        binding.btnsubmit.setOnClickListener {
            //data
            val selectedRadioButtonId: Int = binding.rbtw.checkedRadioButtonId
            selectedRadioButton = find(selectedRadioButtonId)
            val tw: String = selectedRadioButton.text.toString().trim()
            val tahun = binding.edtTahun.text.toString()
            val jabatan = binding.edtJabatan.text.toString()
            val nama = binding.edtNama.text.toString()

            loading(true)

            val requesttahun: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                tahun
            )
            val requestjabatan: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                jabatan
            )
            val requestnama: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                nama
            )

            val requesttw: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                tw
            )

            if (selectedRadioButtonId != -1 && tahun.isNotEmpty() && jabatan.isNotEmpty() && nama.isNotEmpty()){
                api.apat_pdf(body,requesttw,requesttahun,requestjabatan,requestnama).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        try {
                            if (response.isSuccessful){
                                loading(false)
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(response.body()!!.data.toString()))
                                startActivity(browserIntent)
                                dismiss()
                            }else{
                                val gson = Gson()
                                val type = object : TypeToken<PostDataResponse>() {}.type
                                var errorResponse: PostDataResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
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

            }else{
                toast("jangan kosommgi kolom")
                loading(false)
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

}