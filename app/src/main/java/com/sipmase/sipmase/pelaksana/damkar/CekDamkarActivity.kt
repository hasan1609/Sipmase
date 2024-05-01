package com.sipmase.sipmase.pelaksana.damkar

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.databinding.ActivityCekDamkarBinding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.damkar.DamkarModel
import com.sipmase.sipmase.model.postdata.UpdateDamkar
import com.sipmase.sipmase.session.SessionManager
import com.sipmase.sipmase.webservice.ApiClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CekDamkarActivity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekDamkarBinding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog

    companion object {
        var cekdamkar: DamkarModel? = null
    }

    var currentDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_damkar)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekdamkar =
            gson.fromJson(
                intent.getStringExtra("cekdamkar"),
                DamkarModel::class.java
            )

        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = cekdamkar!!.tanggalCek
        if(cekdamkar!!.isStatus == 1){
            binding.btnDraft.visibility = View.GONE
            binding.btnSubmit.visibility = View.GONE
        }else if(cekdamkar!!.isStatus == 3){
            binding.btnDraft.visibility = View.GONE
        }
        if(cekdamkar!!.shift != null){
            binding.txtshift.text = cekdamkar!!.shift
        }else{
            binding.txtshift.text = sessionManager.getNama()
        }

        val normal: Array<String> = resources.getStringArray(R.array.normal)
        val low: Array<String> = resources.getStringArray(R.array.arr1)

         binding.star.setText(cekdamkar!!.start)
         binding.stop.setText(cekdamkar!!.stop)
         binding.txtketerangan.setText(cekdamkar!!.catatan)
        if (cekdamkar!!.airAccu != null) {
            binding.airaccu.setSelection(normal.indexOf(
                normal.first { elem -> elem == cekdamkar!!.airAccu }
            ))
        }
        if (cekdamkar!!.levelAirRadiator != null) {
            binding.lvlAirRadiator.setSelection(normal.indexOf(
                normal.first { elem -> elem == cekdamkar!!.levelAirRadiator }
            ))
        }
        if (cekdamkar!!.tempraturMesin != null) {
            binding.tempMesin.setSelection(normal.indexOf(
                normal.first { elem -> elem == cekdamkar!!.tempraturMesin }
            ))
        }
        if (cekdamkar!!.levelOil != null) {
            binding.lvlOil.setSelection(normal.indexOf(
                normal.first { elem -> elem == cekdamkar!!.levelOil }
            ))
        }
        if (cekdamkar!!.filterSolar != null) {
            binding.filterSolar.setSelection(normal.indexOf(
                normal.first { elem -> elem == cekdamkar!!.filterSolar }
            ))
        }
        if (cekdamkar!!.levelMinyakRem != null) {
            binding.lvlMinyakRem.setSelection(normal.indexOf(
                normal.first { elem -> elem == cekdamkar!!.levelMinyakRem }
            ))
        }
        if (cekdamkar!!.suaraMesin != null) {
            binding.suaraMesin.setSelection(normal.indexOf(
                normal.first { elem -> elem == cekdamkar!!.suaraMesin }
            ))
        }
        if (cekdamkar!!.lampuDepan != null) {
            binding.lampuDepan.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuDepan }
            ))
        }
        if (cekdamkar!!.lampuBelakang != null) {
            binding.lampuBelakang.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuBelakang }
            ))
        }

        if (cekdamkar!!.levelAirRadiator != null) {
            binding.lampuRem.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuRem }
            ))
        }
        if (cekdamkar!!.lampuSeinKananDepan != null) {
            binding.kananDpn.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuSeinKananDepan }
            ))
        }
        if (cekdamkar!!.lampuSeinKiriDepan != null) {
            binding.kiriDepan.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuSeinKiriDepan }
            ))
        }
        if (cekdamkar!!.lampuSeinKananBelakang != null) {
            binding.kananBelakang.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuSeinKananBelakang }
            ))
        }
        if (cekdamkar!!.lampuSeinKiriBelakang != null) {
            binding.kiriBelakang.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuSeinKiriBelakang }
            ))
        }
        if (cekdamkar!!.lampuHazard != null) {
            binding.hazard.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuHazard }
            ))
        }
        if (cekdamkar!!.lampuSorot != null) {
            binding.sorot.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuSorot }
            ))
        }
        if (cekdamkar!!.lampuDalamDepan != null) {
            binding.dalamDepan.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuDalamDepan }
            ))
        }
        if (cekdamkar!!.lampuDalamTengah != null) {
            binding.dalamTengah.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuDalamTengah }
            ))
        }
        if (cekdamkar!!.lampuDalamBelakang != null) {
            binding.dalamBelakang.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.lampuDalamBelakang }
            ))
        }
        if (cekdamkar!!.wiper != null) {
            binding.wiper.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.wiper }
            ))
        }
        if (cekdamkar!!.spion != null) {
            binding.spion.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.spion }
            ))
        }
        if (cekdamkar!!.sirine != null) {
            binding.sirine.setSelection(low.indexOf(
                low.first { elem -> elem == cekdamkar!!.sirine }
            ))
        }

        binding.btnSubmit.setOnClickListener {
            val start = binding.star.text.toString()
            val stop = binding.stop.text.toString()
            val catatan = binding.txtketerangan.text.toString().trim()
            val accu = binding.airaccu.selectedItem
            val air_radiator = binding.lvlAirRadiator.selectedItem
            val temp_mesin = binding.tempMesin.selectedItem
            val lvl_oil = binding.lvlOil.selectedItem
            val filter_solar = binding.filterSolar.selectedItem
            val lvl_minyak_rem = binding.lvlMinyakRem.selectedItem
            val suara_mesin = binding.suaraMesin.selectedItem
            val lamp_depan = binding.lampuDepan.selectedItem
            val lamp_belakang = binding.lampuBelakang.selectedItem
            val lamp_rem = binding.lampuRem.selectedItem
            val lsknd = binding.kananDpn.selectedItem
            val lskrd = binding.kiriDepan.selectedItem
            val lsknb = binding.kananBelakang.selectedItem
            val lskrb = binding.kiriBelakang.selectedItem
            val hazard = binding.hazard.selectedItem
            val sorot = binding.sorot.selectedItem
            val ldd = binding.dalamDepan.selectedItem
            val ldt = binding.dalamTengah.selectedItem
            val ldb = binding.dalamBelakang.selectedItem
            val wiper = binding.wiper.selectedItem
            val spion = binding.spion.selectedItem
            val sirine = binding.sirine.selectedItem
            if (start.isNotEmpty() && stop.isNotEmpty() && catatan.isNotEmpty()) {
                loading(true)
                val updateDamkar = UpdateDamkar(
                    spion.toString(),
                    hazard.toString(),
                    cekdamkar!!.tanggalCek,
                    sessionManager.getNama(),
                    lamp_rem.toString(),
                    sorot.toString(),
                    ldt.toString(),
                    lvl_minyak_rem.toString(),
                    1,
                    lamp_belakang.toString(),
                    accu.toString(),
                    air_radiator.toString(),
                    lvl_oil.toString(),
                    cekdamkar!!.id,
                    lskrb.toString(),
                    suara_mesin.toString(),
                    lamp_depan.toString(),
                    start,
                    catatan,
                    ldb.toString(),
                    filter_solar.toString(),
                    lsknd.toString(),
                    stop,
                    lsknb.toString(),
                    wiper.toString(),
                    ldd.toString(),
                    temp_mesin.toString(),
                    lskrd.toString(),
                    sirine.toString()
                )
                api.update_damkarku(updateDamkar).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.sukses == 1) {
                                finish()
                                toast("Tunggu approve admin")
                            } else {
                                finish()
                                toast("silahkan coba lagi")
                            }
                        } else {
                            loading(false)
                            toast("Response gagal")
                        }
                    }

                    override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                        loading(false)
                        toast("Jaringan error")
                        info { "dinda errror ${t.message}" }
                    }

                })

            } else {
                toast("Jangan kosongi kolom")
            }

        }
        binding.btnDraft.setOnClickListener {
            val start = binding.star.text.toString()
            val stop = binding.stop.text.toString()
            val catatan = binding.txtketerangan.text.toString().trim()
            val accu = binding.airaccu.selectedItem
            val air_radiator = binding.lvlAirRadiator.selectedItem
            val temp_mesin = binding.tempMesin.selectedItem
            val lvl_oil = binding.lvlOil.selectedItem
            val filter_solar = binding.filterSolar.selectedItem
            val lvl_minyak_rem = binding.lvlMinyakRem.selectedItem
            val suara_mesin = binding.suaraMesin.selectedItem
            val lamp_depan = binding.lampuDepan.selectedItem
            val lamp_belakang = binding.lampuBelakang.selectedItem
            val lamp_rem = binding.lampuRem.selectedItem
            val lsknd = binding.kananDpn.selectedItem
            val lskrd = binding.kiriDepan.selectedItem
            val lsknb = binding.kananBelakang.selectedItem
            val lskrb = binding.kiriBelakang.selectedItem
            val hazard = binding.hazard.selectedItem
            val sorot = binding.sorot.selectedItem
            val ldd = binding.dalamDepan.selectedItem
            val ldt = binding.dalamTengah.selectedItem
            val ldb = binding.dalamDepan.selectedItem
            val wiper = binding.wiper.selectedItem
            val spion = binding.spion.selectedItem
            val sirine = binding.sirine.selectedItem
                loading(true)
                val updateDamkar = UpdateDamkar(
                    spion.toString(),
                    hazard.toString(),
                    cekdamkar!!.tanggalCek,
                    sessionManager.getNama(),
                    lamp_rem.toString(),
                    sorot.toString(),
                    ldt.toString(),
                    lvl_minyak_rem.toString(),
                    cekdamkar!!.isStatus,
                    lamp_belakang.toString(),
                    accu.toString(),
                    air_radiator.toString(),
                    lvl_oil.toString(),
                    cekdamkar!!.id,
                    lskrb.toString(),
                    suara_mesin.toString(),
                    lamp_depan.toString(),
                    start,
                    catatan,
                    ldb.toString(),
                    filter_solar.toString(),
                    lsknd.toString(),
                    stop,
                    lsknb.toString(),
                    wiper.toString(),
                    ldd.toString(),
                    temp_mesin.toString(),
                    lskrd.toString(),
                    sirine.toString()
                )
                api.update_damkarku(updateDamkar).enqueue(object :
                    Callback<PostDataResponse> {
                    override fun onResponse(
                        call: Call<PostDataResponse>,
                        response: Response<PostDataResponse>
                    ) {
                        if (response.isSuccessful) {
                            loading(false)
                            if (response.body()!!.sukses == 1) {
                                finish()
                                toast("Disimpan Sbagai Draft")
                            } else {
                                finish()
                                toast("silahkan coba lagi")
                            }
                        } else {
                            loading(false)
                            toast("Response gagal")
                        }
                    }

                    override fun onFailure(call: Call<PostDataResponse>, t: Throwable) {
                        loading(false)
                        toast("Jaringan error")
                        info { "dinda errror ${t.message}" }
                    }

                })

        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        cekdamkar = null
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