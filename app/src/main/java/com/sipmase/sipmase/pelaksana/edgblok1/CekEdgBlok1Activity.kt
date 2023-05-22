package com.sipmase.sipmase.pelaksana.edgblok1

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.constant.Constant
import com.sipmase.sipmase.databinding.ActivityCekEdgBlok1Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.edgblok1.EdgBlokModel
import com.sipmase.sipmase.pelaksana.seawater.CekSeaWaterActivity
import com.sipmase.sipmase.session.SessionManager
import com.sipmase.sipmase.webservice.ApiClient
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CekEdgBlok1Activity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekEdgBlok1Binding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog

    val sheet= TtdEdgBlok1Fragment()

    companion object {
        var cekedgblok1: EdgBlokModel? = null
    }

    var currentDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_edg_blok1)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekedgblok1 =
            gson.fromJson(
                intent.getStringExtra("cekedgblok1"),
                EdgBlokModel::class.java
            )

        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = currentDate

        binding.namak3.text = cekedgblok1!!.k3Nama
        binding.namaoperator.text = cekedgblok1!!.operatorNama
        binding.namaspv.text = cekedgblok1!!.supervisorNama
        if(cekedgblok1!!.supervisorTtd != null){
            binding.btnSpv.visibility = View.GONE
            binding.ttdspv.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-edgblok1/"}${cekedgblok1!!.supervisorTtd}").into(binding.ttdspv)
        }else{
            binding.btnSpv.visibility = View.VISIBLE
            binding.ttdspv.visibility = View.GONE
            binding.btnSpv.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "spv")
                bundle.putString("id", cekedgblok1!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdEdgBlok1Fragment")
            }
        }
        if(cekedgblok1!!.k3Ttd != null){
            binding.btnK3.visibility = View.GONE
            binding.ttdk3.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-edgblok1/"}${cekedgblok1!!.k3Ttd}").into(binding.ttdk3)
        }else{
            binding.btnK3.visibility = View.VISIBLE
            binding.ttdk3.visibility = View.GONE
            binding.btnK3.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "k3")
                bundle.putString("id", cekedgblok1!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdEdgBlok1Fragment")
            }
        }
        if(cekedgblok1!!.operatorTtd != null){
            binding.btnOperator.visibility = View.GONE
            binding.ttdoperator.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-edgblok1/"}${cekedgblok1!!.operatorTtd}").into(binding.ttdoperator)
        }else{
            binding.btnOperator.visibility = View.VISIBLE
            binding.ttdoperator.visibility = View.GONE
            binding.btnOperator.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "operator")
                bundle.putString("id", cekedgblok1!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdEdgBlok1Fragment")
            }
        }


        if(cekedgblok1!!.isStatus == 1){
            binding.btnDraft.visibility = View.GONE
            binding.btnSubmit.visibility = View.GONE
        }else if(cekedgblok1!!.isStatus == 3){
            binding.btnDraft.visibility = View.GONE
        }
        if(cekedgblok1!!.shift != null){
            binding.txtshift.text = cekedgblok1!!.shift
        }else{
            binding.txtshift.text = sessionManager.getNama()
        }

        val low: Array<String> = resources.getStringArray(R.array.low)
        val openclose: Array<String> = resources.getStringArray(R.array.opncls)

        //=================parameter===================
        //waktu pencatatan
        binding.wpsb.setText(cekedgblok1!!.pWaktuPencatatanSebelumStart)
        binding.wpss.setText(cekedgblok1!!.pWaktuPencatatanSesudahStart)
        //oil preasure
        binding.opsb.setText(cekedgblok1!!.pOilPressureSebelumStart)
        binding.opss.setText(cekedgblok1!!.pOilPressureSesudahStart)
        //Oil temperature
        binding.otsb.setText(cekedgblok1!!.pOilTempratureSebelumStart)
        binding.otss.setText(cekedgblok1!!.pOilTempratureSesudahStart)
//            water temp
        binding.wtsb.setText(cekedgblok1!!.pWaterTempratureSebelumStart)
        binding.wtss.setText(cekedgblok1!!.pWaterTempratureSesudahStart)
//            speed
        binding.ssb.setText(cekedgblok1!!.pSpeedSebelumStart)
        binding.sss.setText(cekedgblok1!!.pSpeedSesudahStart)
//            hor meter 1
        binding.hm1sb.setText(cekedgblok1!!.pHourMeter1SebelumStart)
        binding.hm1ss.setText(cekedgblok1!!.pHourMeter1SesudahStart)
//            hour meter 2
        binding.hm2sb.setText(cekedgblok1!!.pHourMeter2SebelumStart)
        binding.hm2ss.setText(cekedgblok1!!.pHourMeter2SesudahStart)
//            main line vol
        binding.mlvsb.setText(cekedgblok1!!.pMainLineVoltageSebelumStart)
        binding.mlvss.setText(cekedgblok1!!.pMainLineVoltageSesudahStart)
//            main line freq
        binding.mlfsb.setText(cekedgblok1!!.pMainLineFrequencySebelumStart)
        binding.mlfss.setText(cekedgblok1!!.pMainLineFrequencySesudahStart)
//            gen break
        if (cekedgblok1!!.pGeneratorBreakerSebelumStart != null) {
            binding.gbsb.setSelection(openclose.indexOf(
                openclose.first { elem -> elem == cekedgblok1!!.pGeneratorBreakerSebelumStart }
            ))
        }
        if (cekedgblok1!!.pGeneratorBreakerSesudahStart != null) {
            binding.gbss.setSelection(openclose.indexOf(
                openclose.first { elem -> elem == cekedgblok1!!.pGeneratorBreakerSesudahStart }
            ))
        }
//            gen vol
         binding.gvsb.setText(cekedgblok1!!.pGeneratorVoltageSebelumStart)
         binding.gvss.setText(cekedgblok1!!.pGeneratorVoltageSesudahStart)
//            gen freq
         binding.gfsb.setText(cekedgblok1!!.pGeneratorFrequencySebelumStart)
         binding.gfss.setText(cekedgblok1!!.pGeneratorFrequencySesudahStart)
//            load cur
        binding.lcsb.setText(cekedgblok1!!.pLoadCurrentSebelumStart)
         binding.lcss.setText(cekedgblok1!!.pLoadCurrentSesudahStart)
//            daya
         binding.dsb.setText(cekedgblok1!!.pDayaSebelumStart)
         binding.dss.setText(cekedgblok1!!.pDayaSesudahStart)
//            COS
        binding.csb.setText(cekedgblok1!!.pCosSebelumStart)
         binding.css.setText(cekedgblok1!!.pCosSesudahStart)
//            bat charg vol
         binding.bcvsb.setText(cekedgblok1!!.pBatteryChargeVoltaseSebelumStart)
        binding.bcvss.setText(cekedgblok1!!.pBatteryChargeVoltaseSesudahStart)
//            hour
         binding.hsb.setText(cekedgblok1!!.pHourSebelumStart)
        binding.hss.setText(cekedgblok1!!.pHourSesudahStart)
//            lube oil lvl
        if (cekedgblok1!!.pLubeOilLevelSebelumStart != null) {
            binding.lolsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok1!!.pLubeOilLevelSebelumStart }
            ))
        }
        if (cekedgblok1!!.pLubeOilLevelSesudahStart != null) {
            binding.lolss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok1!!.pLubeOilLevelSesudahStart }
            ))
        }
//            accu lev
        if (cekedgblok1!!.pAccuLevelSebelumStart != null) {
            binding.alsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok1!!.pAccuLevelSebelumStart }
            ))
        }
        if (cekedgblok1!!.pAccuLevelSesudahStart != null) {
            binding.alss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok1!!.pAccuLevelSesudahStart }
            ))
        }
//            radiator lev
        if (cekedgblok1!!.pRadiatorLevelSebelumStart != null) {
            binding.rlsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok1!!.pRadiatorLevelSebelumStart }
            ))
        }
        if (cekedgblok1!!.pRadiatorLevelSesudahStart != null) {
            binding.rlss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok1!!.pRadiatorLevelSesudahStart }
            ))
        }
//            fuel oil level
        if (cekedgblok1!!.pFuelOilLevelSebelumStart != null) {
            binding.folsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok1!!.pFuelOilLevelSebelumStart }
            ))
        }
        if (cekedgblok1!!.pFuelOilLevelSesudahStart != null) {
            binding.folss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok1!!.pFuelOilLevelSesudahStart }
            ))
        }

        binding.ket.setText(cekedgblok1!!.catatan)

        binding.btnSubmit.setOnClickListener {
            val body_tw: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekedgblok1!!.tw.toString()
            )

            val body_shift: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                sessionManager.getNama().toString()
            )

            val body_is_status: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), "1"
            )

            val body_tahun: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekedgblok1!!.tahun
            )

            val body_id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekedgblok1!!.id}"
            )

            val sdf = SimpleDateFormat("yyyy-M-dd ")
            val tanggal_cek = sdf.format(Date())

            val body_tanggal_cek: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${tanggal_cek}"
            )
            //=================parameter===================
            //waktu pencatatan
            val txtpWpSbs = binding.wpsb.text.toString().trim()
            val txtpWpSds = binding.wpss.text.toString().trim()
            val body_txtpWpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWpSbs
            )
            val body_txtpWpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWpSds
            )
            //oil preasure
            val txtpOpSbs = binding.opsb.text.toString().trim()
            val txtpOpSds = binding.opss.text.toString().trim()
            val body_txtpOpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpOpSbs
            )
            val body_txtpOpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpOpSds
            )
            //Oil temperature
            val txtpOtSbs = binding.otsb.text.toString().trim()
            val txtpOtSds = binding.otss.text.toString().trim()
            val body_txtpOtSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpOtSbs
            )
            val body_txtpOtSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpOtSds
            )
//            water temp
            val txtpWtSbs = binding.wtsb.text.toString().trim()
            val txtpWtSds = binding.wtss.text.toString().trim()
            val body_txtpWtSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWtSbs
            )
            val body_txtpWtSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWtSds
            )
//            speed
            val txtpsSbs = binding.ssb.text.toString().trim()
            val txtpssSds = binding.sss.text.toString().trim()
            val body_txtpsSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpsSbs
            )
            val body_txtpsSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpssSds
            )
//            hor meter 1
            val txtphm1Sbs = binding.hm1sb.text.toString().trim()
            val txtphm1Sds = binding.hm1ss.text.toString().trim()
            val body_txthm1Sbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtphm1Sbs
            )
            val body_txthm1Sds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtphm1Sds
            )
//            hour meter 2
            val txtphm2Sbs = binding.hm2sb.text.toString().trim()
            val txtphm2Sds = binding.hm2ss.text.toString().trim()
            val body_txtphm2Sbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtphm2Sbs
            )
            val body_txtphm2Sds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtphm2Sds
            )
//            main line vol
            val txtpMlvSbs = binding.mlvsb.text.toString().trim()
            val txtpMlvSds = binding.mlvss.text.toString().trim()
            val body_txtpMlvSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpMlvSbs
            )
            val body_txtpMlvSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpMlvSds
            )
//            main line freq
            val txtpMlfSbs = binding.mlfsb.text.toString().trim()
            val txtspMlfSds = binding.mlfss.text.toString().trim()
            val body_txtpMlfSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpMlfSbs
            )
            val body_txtspMlfSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspMlfSds
            )
//            gen break
            val gb_sebelum = binding.gbsb.selectedItem.toString()
            val body_txtpGbSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                gb_sebelum
            )
            val gb_sesudah = binding.gbss.selectedItem.toString()
            val body_txtspGbSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                gb_sesudah
            )
//            gen vol
            val txtpGvSbs = binding.gvsb.text.toString().trim()
            val txtspGvSds = binding.gvss.text.toString().trim()
            val body_txtpGvSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpGvSbs
            )
            val body_txtspGvSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspGvSds
            )
//            gen freq
            val txtpGfSbs = binding.gfsb.text.toString().trim()
            val txtspGfSds = binding.gfss.text.toString().trim()
            val body_txtpGfSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpGfSbs
            )
            val body_txtspGfSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspGfSds
            )
//            load cur
            val txtpLcSbs = binding.lcsb.text.toString().trim()
            val txtspLcSds = binding.lcss.text.toString().trim()
            val body_txtpLcSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpLcSbs
            )
            val body_txtspLcSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspLcSds
            )
//            daya
            val txtpDSbs = binding.dsb.text.toString().trim()
            val txtspDSds = binding.dss.text.toString().trim()
            val body_txtpDSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpDSbs
            )
            val body_txtspDSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspDSds
            )
//            COS
            val txtpCSbs = binding.csb.text.toString().trim()
            val txtspCSds = binding.css.text.toString().trim()
            val body_txtpCSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpCSbs
            )
            val body_txtspCSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspCSds
            )
//            bat charg vol
            val txtpBcvSbs = binding.bcvsb.text.toString().trim()
            val txtspBcvSds = binding.bcvss.text.toString().trim()
            val body_txtpBcvSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpBcvSbs
            )
            val body_txtspBcvSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspBcvSds
            )
//            hour
            val txtpHSbs = binding.hsb.text.toString().trim()
            val txtspHSds = binding.hss.text.toString().trim()
            val body_txtpHSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpHSbs
            )
            val body_txtspHSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspHSds
            )
//            lube oil lvl
            val lube_oil_sebelum = binding.lolsb.selectedItem.toString()
            val lube_oil_sesudah = binding.lolss.selectedItem.toString()
            val body_txtpLoSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                lube_oil_sebelum
            )
            val body_txtspLoSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                lube_oil_sesudah
            )
//            accu lev
            val accu_level_sebelum = binding.alsb.selectedItem.toString()
            val accu_level_sesudah = binding.alss.selectedItem.toString()
            val body_txtpAlSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                accu_level_sebelum
            )
            val body_txtspAlSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                accu_level_sesudah
            )
//            radiator lev
            val radiator_sebelum = binding.rlsb.selectedItem.toString()
            val radiator_sesudah = binding.rlss.selectedItem.toString()
            val body_txtpRlSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                radiator_sebelum
            )
            val body_txtspRlSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                radiator_sesudah
            )
//            fuel oil level
            val fuel_oil_sebelum = binding.folsb.selectedItem.toString()
            val fuel_oil_sesudah = binding.folss.selectedItem.toString()
            val body_txtpFolSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                fuel_oil_sebelum
            )
            val body_txtspFolSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                fuel_oil_sesudah
            )

            val catatan = binding.ket.text.toString().trim()
            val body_catatan: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                catatan
            )
            if (txtpWpSbs.isNotEmpty() &&
                txtpWpSds.isNotEmpty() &&
                txtpOpSbs.isNotEmpty() &&
                txtpOpSds.isNotEmpty() &&
                txtpOtSbs.isNotEmpty() &&
                txtpOtSds.isNotEmpty() &&
                txtpWtSbs.isNotEmpty() &&
                txtpWtSds.isNotEmpty() &&
                txtpsSbs.isNotEmpty() &&
                txtpssSds.isNotEmpty() &&
                txtphm1Sbs.isNotEmpty() &&
                txtphm1Sds.isNotEmpty() &&
                txtphm2Sbs.isNotEmpty() &&
                txtphm2Sds.isNotEmpty() &&
                txtpMlvSbs.isNotEmpty() &&
                txtpMlvSds.isNotEmpty() &&
                txtpMlfSbs.isNotEmpty() &&
                txtspMlfSds.isNotEmpty() &&
                txtpGvSbs.isNotEmpty() &&
                txtspGvSds.isNotEmpty() &&
                txtpGfSbs.isNotEmpty() &&
                txtspGfSds.isNotEmpty() &&
                txtpLcSbs.isNotEmpty() &&
                txtspLcSds.isNotEmpty() &&
                txtpDSbs.isNotEmpty() &&
                txtspDSds.isNotEmpty() &&
                txtpBcvSbs.isNotEmpty() &&
                txtspBcvSds.isNotEmpty() &&
                txtpHSbs.isNotEmpty() &&
                txtspHSds.isNotEmpty() &&
                catatan.isNotEmpty()

            ) {
                loading(true)
                api.update_edgblok1(
                    body_id,
                    body_tw,
                    body_tahun,
                    body_tanggal_cek,
                    body_shift,
                    body_is_status,
                    body_txtpWpSbs,
                    body_txtpWpSds,
                    body_txtpOpSbs,
                    body_txtpOpSds,
                    body_txtpOtSbs,
                    body_txtpOtSds,
                    body_txtpWtSbs,
                    body_txtpWtSds,
                    body_txtpsSbs,
                    body_txtpsSds,
                    body_txthm1Sbs,
                    body_txthm1Sds,
                    body_txtphm2Sbs,
                    body_txtphm2Sds,
                    body_txtpMlvSbs,
                    body_txtpMlvSds,
                    body_txtpMlfSbs,
                    body_txtspMlfSds,
                    body_txtpGbSbs,
                    body_txtspGbSds,
                    body_txtpGvSbs,
                    body_txtspGvSds,
                    body_txtpGfSbs,
                    body_txtspGfSds,
                    body_txtpLcSbs,
                    body_txtspLcSds,
                    body_txtpDSbs,
                    body_txtspDSds,
                    body_txtpCSbs,
                    body_txtspCSds,
                    body_txtpBcvSbs,
                    body_txtspBcvSds,
                    body_txtpHSbs,
                    body_txtspHSds,
                    body_txtpLoSbs,
                    body_txtspLoSds,
                    body_txtpAlSbs,
                    body_txtspAlSds,
                    body_txtpRlSbs,
                    body_txtspRlSds,
                    body_txtpFolSbs,
                    body_txtspFolSds,
                    body_catatan,
                ).enqueue(object :
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
            val body_tw: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekedgblok1!!.tw.toString()
            )

            val body_shift: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                sessionManager.getNama().toString()
            )

            val body_is_status: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekedgblok1!!.isStatus}"
            )

            val body_tahun: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekedgblok1!!.tahun
            )

            val body_id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekedgblok1!!.id}"
            )

            val sdf = SimpleDateFormat("yyyy-M-dd ")
            val tanggal_cek = sdf.format(Date())

            val body_tanggal_cek: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${tanggal_cek}"
            )
            //=================parameter===================
            //waktu pencatatan
            val txtpWpSbs = binding.wpsb.text.toString().trim()
            val txtpWpSds = binding.wpss.text.toString().trim()
            val body_txtpWpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWpSbs
            )
            val body_txtpWpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWpSds
            )
            //oil preasure
            val txtpOpSbs = binding.opsb.text.toString().trim()
            val txtpOpSds = binding.opss.text.toString().trim()
            val body_txtpOpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpOpSbs
            )
            val body_txtpOpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpOpSds
            )
            //Oil temperature
            val txtpOtSbs = binding.otsb.text.toString().trim()
            val txtpOtSds = binding.otss.text.toString().trim()
            val body_txtpOtSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpOtSbs
            )
            val body_txtpOtSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpOtSds
            )
//            water temp
            val txtpWtSbs = binding.wtsb.text.toString().trim()
            val txtpWtSds = binding.wtss.text.toString().trim()
            val body_txtpWtSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWtSbs
            )
            val body_txtpWtSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWtSds
            )
//            speed
            val txtpsSbs = binding.ssb.text.toString().trim()
            val txtpssSds = binding.sss.text.toString().trim()
            val body_txtpsSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpsSbs
            )
            val body_txtpsSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpssSds
            )
//            hor meter 1
            val txtphm1Sbs = binding.hm1sb.text.toString().trim()
            val txtphm1Sds = binding.hm1ss.text.toString().trim()
            val body_txthm1Sbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtphm1Sbs
            )
            val body_txthm1Sds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtphm1Sds
            )
//            hour meter 2
            val txtphm2Sbs = binding.hm2sb.text.toString().trim()
            val txtphm2Sds = binding.hm2ss.text.toString().trim()
            val body_txtphm2Sbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtphm2Sbs
            )
            val body_txtphm2Sds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtphm2Sds
            )
//            main line vol
            val txtpMlvSbs = binding.mlvsb.text.toString().trim()
            val txtpMlvSds = binding.mlvss.text.toString().trim()
            val body_txtpMlvSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpMlvSbs
            )
            val body_txtpMlvSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpMlvSds
            )
//            main line freq
            val txtpMlfSbs = binding.mlfsb.text.toString().trim()
            val txtspMlfSds = binding.mlfss.text.toString().trim()
            val body_txtpMlfSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpMlfSbs
            )
            val body_txtspMlfSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspMlfSds
            )
//            gen break
            val gb_sebelum = binding.gbsb.selectedItem.toString()
            val body_txtpGbSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                gb_sebelum
            )
            val gb_sesudah = binding.gbss.selectedItem.toString()
            val body_txtspGbSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                gb_sesudah
            )
//            gen vol
            val txtpGvSbs = binding.gvsb.text.toString().trim()
            val txtspGvSds = binding.gvss.text.toString().trim()
            val body_txtpGvSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpGvSbs
            )
            val body_txtspGvSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspGvSds
            )
//            gen freq
            val txtpGfSbs = binding.gfsb.text.toString().trim()
            val txtspGfSds = binding.gfss.text.toString().trim()
            val body_txtpGfSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpGfSbs
            )
            val body_txtspGfSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspGfSds
            )
//            load cur
            val txtpLcSbs = binding.lcsb.text.toString().trim()
            val txtspLcSds = binding.lcss.text.toString().trim()
            val body_txtpLcSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpLcSbs
            )
            val body_txtspLcSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspLcSds
            )
//            daya
            val txtpDSbs = binding.dsb.text.toString().trim()
            val txtspDSds = binding.dss.text.toString().trim()
            val body_txtpDSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpDSbs
            )
            val body_txtspDSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspDSds
            )
//            COS
            val txtpCSbs = binding.csb.text.toString().trim()
            val txtspCSds = binding.css.text.toString().trim()
            val body_txtpCSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpCSbs
            )
            val body_txtspCSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspCSds
            )
//            bat charg vol
            val txtpBcvSbs = binding.bcvsb.text.toString().trim()
            val txtspBcvSds = binding.bcvss.text.toString().trim()
            val body_txtpBcvSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpBcvSbs
            )
            val body_txtspBcvSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspBcvSds
            )
//            hour
            val txtpHSbs = binding.hsb.text.toString().trim()
            val txtspHSds = binding.hss.text.toString().trim()
            val body_txtpHSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpHSbs
            )
            val body_txtspHSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspHSds
            )
//            lube oil lvl
            val lube_oil_sebelum = binding.lolsb.selectedItem.toString()
            val lube_oil_sesudah = binding.lolss.selectedItem.toString()
            val body_txtpLoSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                lube_oil_sebelum
            )
            val body_txtspLoSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                lube_oil_sesudah
            )
//            accu lev
            val accu_level_sebelum = binding.alsb.selectedItem.toString()
            val accu_level_sesudah = binding.alss.selectedItem.toString()
            val body_txtpAlSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                accu_level_sebelum
            )
            val body_txtspAlSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                accu_level_sesudah
            )
//            radiator lev
            val radiator_sebelum = binding.rlsb.selectedItem.toString()
            val radiator_sesudah = binding.rlss.selectedItem.toString()
            val body_txtpRlSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                radiator_sebelum
            )
            val body_txtspRlSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                radiator_sesudah
            )
//            fuel oil level
            val fuel_oil_sebelum = binding.folsb.selectedItem.toString()
            val fuel_oil_sesudah = binding.folss.selectedItem.toString()
            val body_txtpFolSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                fuel_oil_sebelum
            )
            val body_txtspFolSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                fuel_oil_sesudah
            )

            val catatan = binding.ket.text.toString().trim()
            val body_catatan: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                catatan
            )
                loading(true)
                api.update_edgblok1(
                    body_id,
                    body_tw,
                    body_tahun,
                    body_tanggal_cek,
                    body_shift,
                    body_is_status,
                    body_txtpWpSbs,
                    body_txtpWpSds,
                    body_txtpOpSbs,
                    body_txtpOpSds,
                    body_txtpOtSbs,
                    body_txtpOtSds,
                    body_txtpWtSbs,
                    body_txtpWtSds,
                    body_txtpsSbs,
                    body_txtpsSds,
                    body_txthm1Sbs,
                    body_txthm1Sds,
                    body_txtphm2Sbs,
                    body_txtphm2Sds,
                    body_txtpMlvSbs,
                    body_txtpMlvSds,
                    body_txtpMlfSbs,
                    body_txtspMlfSds,
                    body_txtpGbSbs,
                    body_txtspGbSds,
                    body_txtpGvSbs,
                    body_txtspGvSds,
                    body_txtpGfSbs,
                    body_txtspGfSds,
                    body_txtpLcSbs,
                    body_txtspLcSds,
                    body_txtpDSbs,
                    body_txtspDSds,
                    body_txtpCSbs,
                    body_txtspCSds,
                    body_txtpBcvSbs,
                    body_txtspBcvSds,
                    body_txtpHSbs,
                    body_txtspHSds,
                    body_txtpLoSbs,
                    body_txtspLoSds,
                    body_txtpAlSbs,
                    body_txtspAlSds,
                    body_txtpRlSbs,
                    body_txtspRlSds,
                    body_txtpFolSbs,
                    body_txtspFolSds,
                    body_catatan,
                ).enqueue(object :
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

    override fun onStart() {
        super.onStart()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        cekedgblok1 = null
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


    fun bitmapToBytes(photo: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

}


