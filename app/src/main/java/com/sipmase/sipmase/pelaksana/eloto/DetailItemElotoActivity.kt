package com.sipmase.sipmase.pelaksana.eloto

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityDetailItemElotoBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.eloto.ElotoModel
import com.sipmase.sipmase.pelaksana.ambulance.CekAmbulanceActivity
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class DetailItemElotoActivity : AppCompatActivity(), AnkoLogger, BottomSheetFilePickerFragment.FilePickerListener {
    lateinit var binding: ActivityDetailItemElotoBinding
    var api = ApiClient.instance()
    lateinit var progressDialog: ProgressDialog
    var eloto: ElotoModel? = null
    private var selectedImageFile: File? = null
    private val PERMISSION_REQUEST_CODE = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICKER = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_item_eloto)
        binding.lifecycleOwner = this
        val gson = Gson()
        eloto =
            gson.fromJson(
                intent.getStringExtra("eloto"),
                ElotoModel::class.java
            )
        progressDialog = ProgressDialog(this)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = dateFormat.parse(eloto!!.createdAt.toString())
        val formattedDate = SimpleDateFormat("dd MMM yyyy, HH:mm:ss").format(date!!)
        binding.txtTgl.text =  formattedDate
        binding.txtTag.text = eloto!!.idTag.toString()
        binding.txtWo.setText(eloto!!.wo.toString())
        binding.txtPeralatan.setText(eloto!!.peralatan.toString())
        binding.txtLokasi.setText(eloto!!.lokasi.toString())
        binding.txtPosisi.setText(eloto!!.posisiAwal.toString())
        if (eloto!!.ket != null){
            binding.txtKet.setText(eloto!!.ket.toString())
        }
        if (eloto!!.isolasi == null){
            binding.crdIsolasi.visibility = View.GONE
        }else{
            binding.crdIsolasi.visibility = View.VISIBLE
            val dateisolasi = dateFormat.parse(eloto!!.isolasi!!.createdAt.toString())
            val formattedDatelisolasi = SimpleDateFormat("dd MMM yyyy, HH:mm:ss").format(dateisolasi!!)
            binding.txtTglIsolasi.text = formattedDatelisolasi
            binding.txtPicIsolasi.setText(eloto!!.isolasi!!.pic.toString())
            binding.txtPosisiIsolasi.setText(eloto!!.isolasi!!.posisiIsolasi.toString())
            val url = "https://sipmase.com/public/storage/eviden/"
            val foto = eloto!!.isolasi!!.eviden.toString()
            Picasso.get()
                .load(url+foto)
                .into(binding.foto)
        }

        if (eloto!!.penormalan == null){
            binding.crdPenormalan.visibility = View.GONE
        }else{
            binding.crdIsolasi.visibility = View.VISIBLE
            val datepenormalan = dateFormat.parse(eloto!!.penormalan!!.createdAt.toString())
            val formattedDatelisolasi = SimpleDateFormat("dd MMM yyyy, HH:mm:ss").format(datepenormalan!!)
            binding.txtTglPenormalan.text = formattedDatelisolasi
            binding.txtPicPenormalan.setText(eloto!!.penormalan!!.pic.toString())
            binding.txtPosisiPenormalan.setText(eloto!!.penormalan!!.posisiNormal.toString())
        }

        binding.addFoto.setOnClickListener {
            openImagePicker()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnHapus.setOnClickListener {
            val builder =
                AlertDialog.Builder(this)
            builder.setMessage("Hapus Loto ? ")
            builder.setPositiveButton("Ya") { dialog, which ->
                hapusEloto(binding.txtTag.text.toString())
            }


            builder.setNegativeButton("Tidak") { dialog, which ->

            }

            builder.show()
        }
        binding.btnSimpan.setOnClickListener {
            simpanLoto()
        }
    }

    private fun hapusEloto(tagId: String) {
        loading(true)
        api.hapuseloto(tagId).enqueue(object :
            Callback<PostDataResponse> {
            override fun onResponse(
                call: Call<PostDataResponse>,
                response: Response<PostDataResponse>
            ) {
                try {
                    if (response.body()!!.sukses == 1) {
                        loading(false)
                        toast("Hapus loto berhasil")
                        finish()
                    } else {
                        loading(false)
                        toast("Hapus loto gagal")
                    }
                } catch (e: Exception) {
                    loading(false)
                    info { "dinda ${e.message}${response.code()} " }
                    toast(e.message.toString())
                }
            }
            override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                loading(false)
                toast("Kesalahan Jaringan")
            }
        })
    }

    private fun simpanLoto() {
        val tag = binding.txtTag.text.toString()
        val wo = binding.txtWo.text.toString()
        val peralatan = binding.txtPeralatan.text.toString()
        val lokasi = binding.txtLokasi.text.toString()
        val posisi = binding.txtPosisi.text.toString()
        val ket = binding.txtKet.text.toString()

        if (tag.isNotEmpty() && wo.isNotEmpty() && peralatan.isNotEmpty() && lokasi.isNotEmpty() && posisi.isNotEmpty()){
            loading(true)
            api.updateeloto(tag, wo, peralatan, lokasi, posisi, ket).enqueue(object :
                Callback<PostDataResponse> {
                override fun onResponse(
                    call: Call<PostDataResponse>,
                    response: Response<PostDataResponse>
                ) {
                    try {
                        if (response.body()!!.sukses == 1) {
                            if (eloto!!.penormalan != null){
                                simpanPenormalan(tag)
                            }
                            if (eloto!!.isolasi != null) {
                                selectedImageFile?.let { file ->
                                    simpanIsolasiWithfoto(file, tag)
                                } ?: uploadIsolasi(tag)
                            }
                            loading(false)
                            toast("Update loto berhasil")
                            eloto = null
                            finish()
                        } else {
                            loading(false)
                            toast("Update loto gagal")
                        }
                    } catch (e: Exception) {
                        loading(false)
                        info { "dinda ${e.message}${response.code()} " }
                        toast(e.message.toString())
                    }
                }
                override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                    loading(false)
                    toast("Kesalahan Jaringan")
                }
            })
        }else{
            loading(false)
            toast("Harap Isi Semua Kolom")
        }
    }
    private fun simpanPenormalan(tagId: String) {
        var pic = binding.txtPicPenormalan.text.toString()
        var posisi = binding.txtPosisiPenormalan.text.toString()

            api.updatepenormalan(tagId ,pic, posisi).enqueue(object :
                Callback<PostDataResponse> {
                override fun onResponse(
                    call: Call<PostDataResponse>,
                    response: Response<PostDataResponse>
                ) {
                    try {
                        if (response.body()!!.sukses == 1) {
                            toast("Update Penormalan berhasil")
                            finish()
                        } else {
                            toast("Update Penormalan gagal")
                        }
                    } catch (e: Exception) {
                        info { "dinda ${e.message}${response.code()} " }
                        toast("oooo"+e.message.toString())
                    }
                }
                override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                    toast("Kesalahan Jaringan")
                }
            })
    }
    private fun uploadIsolasi(tagId: String) {
        val pic = binding.txtPicIsolasi.text.toString()
        val posisi = binding.txtPosisiIsolasi.text.toString()

        api.updateisolasi(tagId ,pic, posisi).enqueue(object :
            Callback<PostDataResponse> {
            override fun onResponse(
                call: Call<PostDataResponse>,
                response: Response<PostDataResponse>
            ) {
                try {
                    if (response.body()!!.sukses == 1) {
                        toast("Update Isolasi berhasil")
                        finish()
                    } else {
                        toast("Update Isolasi gagal")
                    }
                } catch (e: Exception) {
                    info { "dinda ${e.message}${response.code()} " }
                    toast(e.message.toString())
                }
            }
            override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                toast("Kesalahan Jaringan")
            }
        })
    }
    private fun simpanIsolasiWithfoto(file: File, idTag: String) {
        val pic = binding.txtPicIsolasi.text.toString()
        val posisi = binding.txtPosisiIsolasi.text.toString()

        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "eviden",
            file.name,
            requestBody
        )
        val tagIdBody: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            idTag.toString()
        )
        val picBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), pic)
        val posisiBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), posisi)

        loading(true)
        api.updateisolasiWithFoto(
            tagIdBody,
            picBody,
            posisiBody,
            imagePart
        ).enqueue(object : Callback<PostDataResponse> {
            override fun onResponse(
                call: Call<PostDataResponse>,
                response: Response<PostDataResponse>
            ) {
                if (response.isSuccessful) {
                    loading(false)
                    toast("Update Isolasi berhasil")
                    finish()
                } else {
                    loading(false)
                    toast("Update Isolasi gagal")
                }
            }

            override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                loading(false)
                toast("Terjadi kesalahan")
                Log.e("Addisolasi", "Error: ${t.localizedMessage}")
            }
        })
    }

    private fun openImagePicker() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            val bottomSheetFragment = BottomSheetFilePickerFragment()
            bottomSheetFragment.show(supportFragmentManager, "filePicker")
        }
    }

    override fun onFilePickerOptionSelected(option: Int) {
        when (option) {
            BottomSheetFilePickerFragment.OPTION_CAMERA -> {
                openCamera()
            }
            BottomSheetFilePickerFragment.OPTION_GALLERY -> {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICKER)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICKER -> {
                    val imageUri = data?.data
                    binding.foto.setImageURI(imageUri)
                    selectedImageFile = File(getRealPathFromURI(imageUri))
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    binding.foto.setImageBitmap(imageBitmap)
                    selectedImageFile = saveImageToInternalStorage(imageBitmap)
                }
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri?): String {
        var realPath = ""
        uri?.let {
            val cursor = contentResolver.query(it, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    realPath = it.getString(idx)
                }
            }
        }
        return realPath
    }

    private fun saveImageToInternalStorage(image: Bitmap): File {
        val fileDir = filesDir
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(fileDir, fileName)
        FileOutputStream(file).use { outputStream ->
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
        }
        return file
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