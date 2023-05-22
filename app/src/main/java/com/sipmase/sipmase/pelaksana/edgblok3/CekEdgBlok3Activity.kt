package com.sipmase.sipmase.pelaksana.edgblok3

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sipmase.sipmase.R
import com.sipmase.sipmase.constant.Constant
import com.sipmase.sipmase.databinding.ActivityCekEdgBlok3Binding
import com.sipmase.sipmase.model.PostDataResponse
import com.sipmase.sipmase.model.edgblok3.EdgBlok3Model
import com.sipmase.sipmase.pelaksana.edgblok2.CekEdgBlok2Activity
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

class CekEdgBlok3Activity : AppCompatActivity(), AnkoLogger {
    lateinit var binding: ActivityCekEdgBlok3Binding
    var api = ApiClient.instance()
    lateinit var sessionManager: SessionManager
    lateinit var progressDialog: ProgressDialog

    val sheet= TtdEdgBlok3Fragment()

    companion object {
        var cekedgblok3: EdgBlok3Model? = null
    }

    var currentDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cek_edg_blok3)
        binding.lifecycleOwner = this
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        val gson = Gson()
        cekedgblok3 =
            gson.fromJson(
                intent.getStringExtra("cekedgblok3"),
                EdgBlok3Model::class.java
            )

        val sdf = SimpleDateFormat("yyyy-M-dd")
        currentDate = sdf.format(Date())
        binding.txttgl.text = currentDate

        binding.namak3.text = cekedgblok3!!.k3Nama
        binding.namaoperator.text = cekedgblok3!!.operatorNama
        binding.namaspv.text = cekedgblok3!!.supervisorNama
        if (cekedgblok3!!.supervisorTtd != null) {
            binding.btnSpv.visibility = View.GONE
            binding.ttdspv.visibility = View.VISIBLE
            Picasso.get()
                .load("${Constant.foto_url + "foto-edgblok3/"}${cekedgblok3!!.supervisorTtd}")
                .into(binding.ttdspv)
        } else {
            binding.btnSpv.visibility = View.VISIBLE
            binding.ttdspv.visibility = View.GONE
            binding.btnSpv.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "spv")
                bundle.putString("id", cekedgblok3!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager, "TtdEdgBlok3Fragment")
            }
        }
        if (cekedgblok3!!.k3Ttd != null) {
            binding.btnK3.visibility = View.GONE
            binding.ttdk3.visibility = View.VISIBLE
            Picasso.get().load("${Constant.foto_url + "foto-edgblok3/"}${cekedgblok3!!.k3Ttd}")
                .into(binding.ttdk3)
        } else {
            binding.btnK3.visibility = View.VISIBLE
            binding.ttdk3.visibility = View.GONE
            binding.btnK3.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "k3")
                bundle.putString("id", cekedgblok3!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager, "TtdEdgBlok3Fragment")
            }
        }
        if (cekedgblok3!!.operatorTtd != null) {
            binding.btnOperator.visibility = View.GONE
            binding.ttdoperator.visibility = View.VISIBLE
            Picasso.get()
                .load("${Constant.foto_url + "foto-edgblok3/"}${cekedgblok3!!.operatorTtd}")
                .into(binding.ttdoperator)
        } else {
            binding.btnOperator.visibility = View.VISIBLE
            binding.ttdoperator.visibility = View.GONE
            binding.btnOperator.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("judul", "operator")
                bundle.putString("id", cekedgblok3!!.id.toString())
                sheet.arguments = bundle

                sheet.show(supportFragmentManager, "TtdEdgBlok3Fragment")
            }
        }

        val low: Array<String> = resources.getStringArray(R.array.low)
        val openclose: Array<String> = resources.getStringArray(R.array.opncls)
        //=================parameter===================
        //oil preasure
        binding.opsb.setText(cekedgblok3!!.oilPressSebelumStart)
        binding.opss.setText(cekedgblok3!!.oilPressSesudahStart)
        //oil temperature
        binding.otsb.setText(cekedgblok3!!.oilTempSebelumStart)
        binding.otss.setText(cekedgblok3!!.oilTempSesudahStart)
        //            water coolant temp
        binding.ctsb.setText(cekedgblok3!!.waterCoolTempSebelumStart)
        binding.ctss.setText(cekedgblok3!!.waterCoolTempSebelumStart)
//            speed
        binding.ssb.setText(cekedgblok3!!.speedSebelumStart)
        binding.sss.setText(cekedgblok3!!.speedSesudahStart)
//            hour meter
        binding.hmsb.setText(cekedgblok3!!.hourMeterSebelumStart)
        binding.hmss.setText(cekedgblok3!!.hourMeterSesudahStart)
//            main line volt
        binding.mlvsb.setText(cekedgblok3!!.mlVoltageSebelumStart)
        binding.mlvss.setText(cekedgblok3!!.mlVoltageSesudahStart)
        //            main line freq
        binding.mlfsb.setText(cekedgblok3!!.mlFreqSebelumStart)
        binding.mlfss.setText(cekedgblok3!!.mlFreqSesudahStart)
//            gen break
        if (cekedgblok3!!.genBreakSebelumStart != null) {
            binding.gbsb.setSelection(openclose.indexOf(
                openclose.first { elem -> elem == cekedgblok3!!.genBreakSebelumStart }
            ))
        }
        if (cekedgblok3!!.genBreakSesudahStart != null) {
            binding.gbss.setSelection(openclose.indexOf(
                openclose.first { elem -> elem == cekedgblok3!!.genBreakSesudahStart }
            ))
        }
//            gen vol
        binding.gvsb.setText(cekedgblok3!!.genVolSebelumStart)
        binding.gvss.setText(cekedgblok3!!.genVolSesudahStart)
//            gen freq
        binding.gfsb.setText(cekedgblok3!!.genFreqSebelumStart)
        binding.gfss.setText(cekedgblok3!!.genFreqSesudahStart)
//            load cur
        binding.lcsb.setText(cekedgblok3!!.loadCurSebelumStart)
        binding.lcss.setText(cekedgblok3!!.loadCurSesudahStart)
        //            daya
        binding.dsb.setText(cekedgblok3!!.dayaSebelumStart)
        binding.dss.setText(cekedgblok3!!.dayaSesudahStart)
        //            cos
        binding.csb.setText(cekedgblok3!!.cosSebelumStart)
        binding.css.setText(cekedgblok3!!.cosSesudahStart)
//            bat charg vol
        binding.bcvsb.setText(cekedgblok3!!.batChargeVolSebelumStart)
        binding.bcvss.setText(cekedgblok3!!.batChargeVolSesudahStart)
        //            hour
        binding.hsb.setText(cekedgblok3!!.hourSebelumStart)
        binding.hss.setText(cekedgblok3!!.hourSesudahStart)
//            lube oil
        binding.lolsb.selectedItem.toString()
        if (cekedgblok3!!.lubeOilSebelumStart != null) {
            binding.lolsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok3!!.lubeOilSebelumStart }
            ))
        }
        binding.lolss.selectedItem.toString()
        if (cekedgblok3!!.lubeOilSesudahStart != null) {
            binding.lolss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok3!!.lubeOilSesudahStart }
            ))
        }
//            accu lev
        binding.alsb.selectedItem.toString()
        if (cekedgblok3!!.accuSebelumStart != null) {
            binding.alsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok3!!.accuSebelumStart }
            ))
        }
        binding.alss.selectedItem.toString()
        if (cekedgblok3!!.accuSesudahStart != null) {
            binding.alss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok3!!.accuSesudahStart }
            ))
        }
//            radiator lev
        binding.rlsb.selectedItem.toString()
        if (cekedgblok3!!.radiatorSebelumStart != null) {
            binding.rlsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok3!!.radiatorSebelumStart }
            ))
        }
        binding.rlss.selectedItem.toString()
        if (cekedgblok3!!.radiatorSesudahStart != null) {
            binding.rlss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok3!!.radiatorSesudahStart }
            ))
        }
//            fuel oil level
        binding.folsb.selectedItem.toString()
        if (cekedgblok3!!.fuelOilSebelumStart != null) {
            binding.folsb.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok3!!.fuelOilSebelumStart }
            ))
        }
        binding.folss.selectedItem.toString()
        if (cekedgblok3!!.fuelOilSesudahStart != null) {
            binding.folss.setSelection(low.indexOf(
                low.first { elem -> elem == cekedgblok3!!.fuelOilSesudahStart }
            ))
        }
        //            temp bearing
        binding.tbgsb.setText(cekedgblok3!!.tempBearingGenSebelumStart)
        binding.tbgss.setText(cekedgblok3!!.tempBearingGenSesudahStart)
//            temp win u
        binding.twusb.setText(cekedgblok3!!.tampWindingUSebelumStart)
        binding.twuss.setText(cekedgblok3!!.tampWindingUSesudahStart)
//            temp win v
         binding.twvsb.setText(cekedgblok3!!.tempWindingVSebelumStart)
         binding.twvss.setText(cekedgblok3!!.tempWindingVSesudahStart)
//            tyemp win w
        binding.twwsb.setText(cekedgblok3!!.tempWindingWSebelumStart)
         binding.twwss.setText(cekedgblok3!!.tempWindingWSesudahStart)
//            radiator fan
         binding.brfsb.setText(cekedgblok3!!.beltRadiatorFanSebelumStart)
        binding.brfss.setText(cekedgblok3!!.beltRadiatorFanSesudahStart)

        binding.ket.setText(cekedgblok3!!.catatan)

        binding.btnSubmit.setOnClickListener {
            val body_tw: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekedgblok3!!.tw
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
                cekedgblok3!!.tahun
            )

            val body_id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekedgblok3!!.id}"
            )

            val sdf = SimpleDateFormat("yyyy-M-dd ")
            val tanggal_cek = sdf.format(Date())

            val body_tanggal_cek: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${tanggal_cek}"
            )
            //=================parameter===================
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
            //oil temp
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

            //water coolant
            val txtpWtSbs = binding.ctsb.text.toString().trim()
            val txtpWtSds = binding.ctss.text.toString().trim()
            val body_txtpWtSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWtSbs
            )
            val body_txtpFtSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWtSds
            )
            //            speed
            val txtpSSbs = binding.ssb.text.toString().trim()
            val txtspSSds = binding.sss.text.toString().trim()
            val body_txtpSSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpSSbs
            )
            val body_txtspSSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspSSds
            )
            //            hour meter
            val txtpHmSbs = binding.hmsb.text.toString().trim()
            val txtspHmSds = binding.hmss.text.toString().trim()
            val body_txtpHmSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpHmSbs
            )
            val body_txtspHmSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspHmSds
            )
//            main line vol
            val txtpMlvSbs = binding.mlvsb.text.toString().trim()
            val txtspMlvSds = binding.mlvss.text.toString().trim()
            val body_txtpMlvSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpMlvSbs
            )
            val body_txtspMlvSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspMlvSds
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
//             gen break
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
            //            load curr
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

            //            cos
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
            //            Hour
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
//            lube oil
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
//            temp bearing
            val txtpTbgSbs = binding.tbgsb.text.toString().trim()
            val txtpTbgSds = binding.tbgss.text.toString().trim()
            val body_txtpTbgSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbgSbs
            )
            val body_txtpTbgSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbgSds
            )
//            temp win u
            val txtpTbuSbs = binding.twusb.text.toString().trim()
            val txtpTbuSds = binding.twuss.text.toString().trim()
            val body_txtpTbuSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbuSbs
            )
            val body_txtpTbuSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbuSds
            )
//            temp win v
            val txtpTbvSbs = binding.twvsb.text.toString().trim()
            val txtpTbvSds = binding.twvss.text.toString().trim()
            val body_txtpTbvSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbvSbs
            )
            val body_txtpTbvSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbvSds
            )
//            tyemp win w
            val txtpTbwSbs = binding.twwsb.text.toString().trim()
            val txtpTbwSds = binding.twwss.text.toString().trim()
            val body_txtpTbwSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbwSbs
            )
            val body_txtpTbwSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbwSds
            )
//            radiator fan
            val txtpBrfSbs = binding.brfsb.text.toString().trim()
            val txtpBrfSds = binding.brfss.text.toString().trim()
            val body_txtpBrfSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpBrfSbs
            )
            val body_txtpBrfSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpBrfSds
            )

            val catatan = binding.ket.text.toString().trim()
            val body_catatan: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                catatan
            )
            if (txtpOpSbs.isNotEmpty() &&
                txtpOpSds.isNotEmpty() &&
                txtpOtSbs.isNotEmpty() &&
                txtpOtSds.isNotEmpty() &&
                txtpWtSbs.isNotEmpty() &&
                txtpWtSds.isNotEmpty() &&
                txtspSSds.isNotEmpty() &&
                txtspSSds.isNotEmpty() &&
                txtpHmSbs.isNotEmpty() &&
                txtspHmSds.isNotEmpty() &&
                txtpMlvSbs.isNotEmpty() &&
                txtspMlvSds.isNotEmpty() &&
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
                txtpCSbs.isNotEmpty() &&
                txtspCSds.isNotEmpty() &&
                txtpBcvSbs.isNotEmpty() &&
                txtspBcvSds.isNotEmpty() &&
                txtpHSbs.isNotEmpty() &&
                txtspHSds.isNotEmpty() &&
                txtpTbgSbs.isNotEmpty() &&
                txtpTbgSds.isNotEmpty() &&
                txtpTbuSbs.isNotEmpty() &&
                txtpTbuSbs.isNotEmpty() &&
                txtpTbvSbs.isNotEmpty() &&
                txtpTbvSds.isNotEmpty() &&
                txtpTbwSbs.isNotEmpty() &&
                txtpTbwSds.isNotEmpty() &&
                txtpBrfSbs.isNotEmpty() &&
                txtpBrfSds.isNotEmpty() &&
                catatan.isNotEmpty()
            ) {
                loading(true)
                api.update_edgblok3(
                    body_id,
                    body_tw,
                    body_tahun,
                    body_tanggal_cek,
                    body_shift,
                    body_is_status,
                    body_txtpOpSbs,
                    body_txtpOpSds,
                    body_txtpOtSbs,
                    body_txtpOtSds,
                    body_txtpWtSbs,
                    body_txtpFtSds,
                    body_txtpSSbs,
                    body_txtspSSds,
                    body_txtpHmSbs,
                    body_txtspHmSds,
                    body_txtpMlvSbs,
                    body_txtspMlvSds,
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
                    body_txtpTbgSbs,
                    body_txtpTbgSds,
                    body_txtpTbuSbs,
                    body_txtpTbuSds,
                    body_txtpTbvSbs,
                    body_txtpTbvSds,
                    body_txtpTbwSbs,
                    body_txtpTbwSds,
                    body_txtpBrfSbs,
                    body_txtpBrfSds,
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
                cekedgblok3!!.tw
            )

            val body_shift: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                sessionManager.getNama().toString()
            )

            val body_is_status: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"), "${cekedgblok3!!.isStatus}"
            )

            val body_tahun: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                cekedgblok3!!.tahun
            )

            val body_id: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${cekedgblok3!!.id}"
            )

            val sdf = SimpleDateFormat("yyyy-M-dd ")
            val tanggal_cek = sdf.format(Date())

            val body_tanggal_cek: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                "${tanggal_cek}"
            )
            //=================parameter===================
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
            //oil temp
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

            //water coolant
            val txtpWtSbs = binding.ctsb.text.toString().trim()
            val txtpWtSds = binding.ctss.text.toString().trim()
            val body_txtpWtSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWtSbs
            )
            val body_txtpFtSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpWtSds
            )
            //            speed
            val txtpSSbs = binding.ssb.text.toString().trim()
            val txtspSSds = binding.sss.text.toString().trim()
            val body_txtpSSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpSSbs
            )
            val body_txtspSSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspSSds
            )
            //            hour meter
            val txtpHmSbs = binding.hmsb.text.toString().trim()
            val txtspHmSds = binding.hmss.text.toString().trim()
            val body_txtpHmSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpHmSbs
            )
            val body_txtspHmSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspHmSds
            )
//            main line vol
            val txtpMlvSbs = binding.mlvsb.text.toString().trim()
            val txtspMlvSds = binding.mlvss.text.toString().trim()
            val body_txtpMlvSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpMlvSbs
            )
            val body_txtspMlvSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtspMlvSds
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
//             gen break
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
            //            load curr
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

            //            cos
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
            //            Hour
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
//            lube oil
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
//            temp bearing
            val txtpTbgSbs = binding.tbgsb.text.toString().trim()
            val txtpTbgSds = binding.tbgss.text.toString().trim()
            val body_txtpTbgSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbgSbs
            )
            val body_txtpTbgSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbgSds
            )
//            temp win u
            val txtpTbuSbs = binding.twusb.text.toString().trim()
            val txtpTbuSds = binding.twuss.text.toString().trim()
            val body_txtpTbuSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbuSbs
            )
            val body_txtpTbuSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbuSds
            )
//            temp win v
            val txtpTbvSbs = binding.twvsb.text.toString().trim()
            val txtpTbvSds = binding.twvss.text.toString().trim()
            val body_txtpTbvSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbvSbs
            )
            val body_txtpTbvSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbvSds
            )
//            tyemp win w
            val txtpTbwSbs = binding.twwsb.text.toString().trim()
            val txtpTbwSds = binding.twwss.text.toString().trim()
            val body_txtpTbwSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbwSbs
            )
            val body_txtpTbwSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpTbwSds
            )
//            radiator fan
            val txtpBrfSbs = binding.brfsb.text.toString().trim()
            val txtpBrfSds = binding.brfss.text.toString().trim()
            val body_txtpBrfSbs: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpBrfSbs
            )
            val body_txtpBrfSds: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                txtpBrfSds
            )

            val catatan = binding.ket.text.toString().trim()
            val body_catatan: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                catatan
            )
                loading(true)
                api.update_edgblok3(
                    body_id,
                    body_tw,
                    body_tahun,
                    body_tanggal_cek,
                    body_shift,
                    body_is_status,
                    body_txtpOpSbs,
                    body_txtpOpSds,
                    body_txtpOtSbs,
                    body_txtpOtSds,
                    body_txtpWtSbs,
                    body_txtpFtSds,
                    body_txtpSSbs,
                    body_txtspSSds,
                    body_txtpHmSbs,
                    body_txtspHmSds,
                    body_txtpMlvSbs,
                    body_txtspMlvSds,
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
                    body_txtpTbgSbs,
                    body_txtpTbgSds,
                    body_txtpTbuSbs,
                    body_txtpTbuSds,
                    body_txtpTbvSbs,
                    body_txtpTbvSds,
                    body_txtpTbwSbs,
                    body_txtpTbwSds,
                    body_txtpBrfSbs,
                    body_txtpBrfSds,
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
        cekedgblok3 = null
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