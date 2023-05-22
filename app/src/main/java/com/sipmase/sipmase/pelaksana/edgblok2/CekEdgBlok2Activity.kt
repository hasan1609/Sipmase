package com.sipmase.sipmase.pelaksana.edgblok2

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.constant.Constant
import com.sipmase.sipmase.databinding.ActivityCekEdgBlok2Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.edgblok2.EdgBlok2Model
import com.sipmase.sipmase.pelaksana.edgblok1.CekEdgBlok1Activity
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

class CekEdgBlok2Activity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekEdgBlok2Binding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog

    val sheet= TtdEdgBlok2Fragment()

    companion object {
        var cekedgblok2: EdgBlok2Model? = null
    }

    var currentDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_edg_blok2)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekedgblok2 =
            gson.fromJson(
                intent.getStringExtra("cekedgblok2"),
                EdgBlok2Model::class.java
            )

        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = currentDate

        binding.namak3.text = cekedgblok2!!.k3Nama
        binding.namaoperator.text = cekedgblok2!!.operatorNama
        binding.namaspv.text = cekedgblok2!!.supervisorNama
        if(cekedgblok2!!.supervisorTtd != null){
            binding.btnSpv.visibility = View.GONE
            binding.ttdspv.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-edgblok2/"}${cekedgblok2!!.supervisorTtd}").into(binding.ttdspv)
        }else{
            binding.btnSpv.visibility = View.VISIBLE
            binding.ttdspv.visibility = View.GONE
            binding.btnSpv.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "spv")
                bundle.putString("id", cekedgblok2!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdEdgBlok2Fragment")
            }
        }
        if(cekedgblok2!!.k3Ttd != null){
            binding.btnK3.visibility = View.GONE
            binding.ttdk3.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-edgblok2/"}${cekedgblok2!!.k3Ttd}").into(binding.ttdk3)
        }else{
            binding.btnK3.visibility = View.VISIBLE
            binding.ttdk3.visibility = View.GONE
            binding.btnK3.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "k3")
                bundle.putString("id", cekedgblok2!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdEdgBlok2Fragment")
            }
        }
        if(cekedgblok2!!.operatorTtd != null){
            binding.btnOperator.visibility = View.GONE
            binding.ttdoperator.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url+"foto-edgblok2/"}${cekedgblok2!!.operatorTtd}").into(binding.ttdoperator)
        }else{
            binding.btnOperator.visibility = View.VISIBLE
            binding.ttdoperator.visibility = View.GONE
            binding.btnOperator.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "operator")
                bundle.putString("id", cekedgblok2!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager,"TtdEdgBlok2Fragment")
            }
        }


        if(cekedgblok2!!.isStatus == 1){
            binding.btnDraft.visibility = View.GONE
            binding.btnSubmit.visibility = View.GONE
        }else if(cekedgblok2!!.isStatus == 3){
            binding.btnDraft.visibility = View.GONE
        }
        if(cekedgblok2!!.shift != null){
            binding.txtshift.text = cekedgblok2!!.shift
        }else{
            binding.txtshift.text = sessionManager.getNama()
        }

        val low: Array<String> = resources.getStringArray(R.array.low)
        val openclose: Array<String> = resources.getStringArray(R.array.opncls)

        //=================parameter===================
        //waktu pencatatan
        binding.wpsb.setText(cekedgblok2!!.pWaktuPencatatanSebelumStart)
         binding.wpss.setText(cekedgblok2!!.pWaktuPencatatanSesudahStart)
        //oil preasure
         binding.opsb.setText(cekedgblok2!!.pOilPressureSebelumStart)
         binding.opss.setText(cekedgblok2!!.pOilPressureSesudahStart)
        //fuel preasure
         binding.fpsb.setText(cekedgblok2!!.pFuelPressureSebelumStart)
         binding.fpss.setText(cekedgblok2!!.pFuelPressureSesudahStart)
        //fuel temperature
         binding.ftsb.setText(cekedgblok2!!.pFuelTempratureSebelumStart)
         binding.ftss.setText(cekedgblok2!!.pFuelTempratureSesudahStart)
        //            coolant pressure
        binding.cpsb.setText(cekedgblok2!!.pCoolantPressureSebelumStart)
         binding.cpss.setText(cekedgblok2!!.pCoolantPressureSesudahStart)
        //            coolant temp
         binding.ctsb.setText(cekedgblok2!!.pCoolantTempratureSebelumStart)
         binding.ctss.setText(cekedgblok2!!.pCoolantTempratureSesudahStart)
//            speed
         binding.ssb.setText(cekedgblok2!!.pSpeedSebelumStart)
         binding.sss.setText(cekedgblok2!!.pSpeedSesudahStart)
//            inlent temp
        binding.itsb.setText(cekedgblok2!!.pInletTempratureSebelumStart)
         binding.itss.setText(cekedgblok2!!.pInletTempratureSesudahStart)
//            turbo pres
         binding.tpsb.setText(cekedgblok2!!.pTurboPleasureSebelumStart)
         binding.tpss.setText(cekedgblok2!!.pTurboPleasureSesudahStart)
//            gen break
        if (cekedgblok2!!.pGeneratorBreakerSebelumStart != null) {
            binding.gbsb.setSelection(openclose.indexOf(
                openclose.first { elem -> elem == cekedgblok2!!.pGeneratorBreakerSebelumStart }
            ))
        }
        if (cekedgblok2!!.pGeneratorBreakerSesudahStart != null) {
            binding.gbss.setSelection(openclose.indexOf(
                openclose.first { elem -> elem == cekedgblok2!!.pGeneratorBreakerSesudahStart }
            ))
        }
//            gen vol
         binding.gvsb.setText(cekedgblok2!!.pGeneratorVoltageSebelumStart)
         binding.gvss.setText(cekedgblok2!!.pGeneratorVoltageSesudahStart)
//            gen freq
        binding.gfsb.setText(cekedgblok2!!.pGeneratorFrequencySebelumStart)
        binding.gfss.setText(cekedgblok2!!.pGeneratorFrequencySesudahStart)
        //            gen curr
        binding.gcsb.setText(cekedgblok2!!.pGeneratorCurrentSebelumStart)
        binding.gcss.setText(cekedgblok2!!.pGeneratorCurrentSesudahStart)
//            load
        binding.lsb.setText(cekedgblok2!!.pLoadSebelumStart)
         binding.lss.setText(cekedgblok2!!.pLoadSesudahStart)
//            bat charg vol
         binding.bcvsb.setText(cekedgblok2!!.pBatteryChargeVoltaseSebelumStart)
         binding.bcvss.setText(cekedgblok2!!.pBatteryChargeVoltaseSesudahStart)
//            lube oil
         binding.losb.selectedItem.toString()
        if (cekedgblok2!!.pLubeOilLevelSebelumStart != null) {
            binding.losb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok2!!.pLubeOilLevelSebelumStart }
            ))
        }
         binding.loss.selectedItem.toString()
        if (cekedgblok2!!.pLubeOilLevelSesudahStart != null) {
            binding.loss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok2!!.pLubeOilLevelSesudahStart }
            ))
        }
//            accu lev
         binding.alsb.selectedItem.toString()
        if (cekedgblok2!!.pAccuLevelSebelumStart != null) {
            binding.alsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok2!!.pAccuLevelSebelumStart }
            ))
        }
        binding.alss.selectedItem.toString()
        if (cekedgblok2!!.pAccuLevelSesudahStart != null) {
            binding.alss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok2!!.pAccuLevelSesudahStart }
            ))
        }
//            radiator lev
         binding.rlsb.selectedItem.toString()
        if (cekedgblok2!!.pRadiatorLevelSebelumStart != null) {
            binding.rlsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok2!!.pRadiatorLevelSebelumStart }
            ))
        }
         binding.rlss.selectedItem.toString()
        if (cekedgblok2!!.pRadiatorLevelSesudahStart != null) {
            binding.rlss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok2!!.pRadiatorLevelSesudahStart }
            ))
        }
//            fuel oil level
         binding.folsb.selectedItem.toString()
        if (cekedgblok2!!.pFuelOilLevelSebelumStart != null) {
            binding.folsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok2!!.pFuelOilLevelSebelumStart }
            ))
        }
         binding.folss.selectedItem.toString()
        if (cekedgblok2!!.pFuelOilLevelSesudahStart != null) {
            binding.folss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok2!!.pFuelOilLevelSesudahStart }
            ))
        }

        binding.ket.setText(cekedgblok2!!.catatan)


        binding.btnSubmit.setOnClickListener {
            val body_tw: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekedgblok2!!.tw.toString()
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
                cekedgblok2!!.tahun
            )

            val body_id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekedgblok2!!.id}"
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
            //fuel preasure
            val txtpFpSbs = binding.fpsb.text.toString().trim()
            val txtpFpSds = binding.fpss.text.toString().trim()
            val body_txtpFpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpFpSbs
            )
            val body_txtpFpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpFpSds
            )

            //fuel temperature
            val txtpFtSbs = binding.ftsb.text.toString().trim()
            val txtpFtSds = binding.ftss.text.toString().trim()
            val body_txtpFtSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpFtSbs
            )
            val body_txtpFtSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpFtSds
            )
            //            coolant pressure
            val txtpCpSbs = binding.cpsb.text.toString().trim()
            val txtpCpSds = binding.cpss.text.toString().trim()
            val body_txtpCpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpCpSbs
            )
            val body_txtpCpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpCpSds
            )
            //            coolant temp
            val txtpCtSbs = binding.ctsb.text.toString().trim()
            val txtpCtSds = binding.ctss.text.toString().trim()
            val body_txtpCtSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpCtSbs
            )
            val body_txtpCtSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpCtSds
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
//            inlent temp
            val txtpItSbs = binding.itsb.text.toString().trim()
            val txtspItSds = binding.itss.text.toString().trim()
            val body_txtpItSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpItSbs
            )
            val body_txtspItSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspItSds
            )
//            turbo pres
            val txtpTpSbs = binding.tpsb.text.toString().trim()
            val txtspTpSds = binding.tpss.text.toString().trim()
            val body_txtpTpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTpSbs
            )
            val body_txtspTpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspTpSds
            )
//            gen break
            val gb_sebelum = binding.gbsb.selectedItem.toString()
            val gb_sesudah = binding.gbss.selectedItem.toString()
            val body_txtpGbSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                gb_sebelum
            )
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
            //            gen curr
            val txtpGcSbs = binding.gcsb.text.toString().trim()
            val txtspGcSds = binding.gcss.text.toString().trim()
            val body_txtpGcSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpGcSbs
            )
            val body_txtspGcSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspGcSds
            )
//            load
            val txtpLcSbs = binding.lsb.text.toString().trim()
            val txtspLcSds = binding.lss.text.toString().trim()
            val body_txtpLcSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpLcSbs
            )
            val body_txtspLcSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspLcSds
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
//            lube oil
            val lube_oil_sebelum = binding.losb.selectedItem.toString()
            val lube_oil_sesudah = binding.loss.selectedItem.toString()
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
                txtpsSbs.isNotEmpty() &&
                txtpssSds.isNotEmpty() &&
                txtpGvSbs.isNotEmpty() &&
                txtspGvSds.isNotEmpty() &&
                txtpGfSbs.isNotEmpty() &&
                txtspGfSds.isNotEmpty() &&
                txtpLcSbs.isNotEmpty() &&
                txtspLcSds.isNotEmpty() &&
                txtpBcvSbs.isNotEmpty() &&
                catatan.isNotEmpty()
            ) {
                loading(true)
                api.update_edgblok2(
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
                    body_txtpFpSbs,
                    body_txtpFpSds,
                    body_txtpFtSbs,
                    body_txtpFtSds,
                    body_txtpCpSbs,
                    body_txtpCpSds,
                    body_txtpCtSbs,
                    body_txtpCtSds,
                    body_txtpsSbs,
                    body_txtpsSds,
                    body_txtpItSbs,
                    body_txtspItSds,
                    body_txtpTpSbs,
                    body_txtspTpSds,
                    body_txtpGbSbs,
                    body_txtspGbSds,
                    body_txtpGvSbs,
                    body_txtspGvSds,
                    body_txtpGfSbs,
                    body_txtspGfSds,
                    body_txtpGcSbs,
                    body_txtspGcSds,
                    body_txtpLcSbs,
                    body_txtspLcSds,
                    body_txtpBcvSbs,
                    body_txtspBcvSds,
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
                cekedgblok2!!.tw.toString()
            )

            val body_shift: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                sessionManager.getNama().toString()
            )

            val body_is_status: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekedgblok2!!.isStatus}"
            )

            val body_tahun: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekedgblok2!!.tahun
            )

            val body_id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekedgblok2!!.id}"
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
            //fuel preasure
            val txtpFpSbs = binding.fpsb.text.toString().trim()
            val txtpFpSds = binding.fpss.text.toString().trim()
            val body_txtpFpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpFpSbs
            )
            val body_txtpFpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpFpSds
            )

            //fuel temperature
            val txtpFtSbs = binding.ftsb.text.toString().trim()
            val txtpFtSds = binding.ftss.text.toString().trim()
            val body_txtpFtSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpFtSbs
            )
            val body_txtpFtSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpFtSds
            )
            //            coolant pressure
            val txtpCpSbs = binding.cpsb.text.toString().trim()
            val txtpCpSds = binding.cpss.text.toString().trim()
            val body_txtpCpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpCpSbs
            )
            val body_txtpCpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpCpSds
            )
            //            coolant temp
            val txtpCtSbs = binding.ctsb.text.toString().trim()
            val txtpCtSds = binding.ctss.text.toString().trim()
            val body_txtpCtSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpCtSbs
            )
            val body_txtpCtSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpCtSds
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
//            inlent temp
            val txtpItSbs = binding.itsb.text.toString().trim()
            val txtspItSds = binding.itss.text.toString().trim()
            val body_txtpItSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpItSbs
            )
            val body_txtspItSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspItSds
            )
//            turbo pres
            val txtpTpSbs = binding.tpsb.text.toString().trim()
            val txtspTpSds = binding.tpss.text.toString().trim()
            val body_txtpTpSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTpSbs
            )
            val body_txtspTpSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspTpSds
            )
//            gen break
            val gb_sebelum = binding.gbsb.selectedItem.toString()
            val gb_sesudah = binding.gbss.selectedItem.toString()
            val body_txtpGbSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                gb_sebelum
            )
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
            //            gen curr
            val txtpGcSbs = binding.gcsb.text.toString().trim()
            val txtspGcSds = binding.gcss.text.toString().trim()
            val body_txtpGcSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpGcSbs
            )
            val body_txtspGcSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspGcSds
            )
//            load
            val txtpLcSbs = binding.lsb.text.toString().trim()
            val txtspLcSds = binding.lss.text.toString().trim()
            val body_txtpLcSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpLcSbs
            )
            val body_txtspLcSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspLcSds
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
//            lube oil
            val lube_oil_sebelum = binding.losb.selectedItem.toString()
            val lube_oil_sesudah = binding.loss.selectedItem.toString()
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
                api.update_edgblok2(
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
                    body_txtpFpSbs,
                    body_txtpFpSds,
                    body_txtpFtSbs,
                    body_txtpFtSds,
                    body_txtpCpSbs,
                    body_txtpCpSds,
                    body_txtpCtSbs,
                    body_txtpCtSds,
                    body_txtpsSbs,
                    body_txtpsSds,
                    body_txtpItSbs,
                    body_txtspItSds,
                    body_txtpTpSbs,
                    body_txtspTpSds,
                    body_txtpGbSbs,
                    body_txtspGbSds,
                    body_txtpGvSbs,
                    body_txtspGvSds,
                    body_txtpGfSbs,
                    body_txtspGfSds,
                    body_txtpGcSbs,
                    body_txtspGcSds,
                    body_txtpLcSbs,
                    body_txtspLcSds,
                    body_txtpBcvSbs,
                    body_txtspBcvSds,
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
        cekedgblok2 = null
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