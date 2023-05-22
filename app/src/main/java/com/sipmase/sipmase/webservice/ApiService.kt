package com.sipmase.sipmase.webservice

import com.sipmase.sipmase.model.*
import com.sipmase.sipmase.model.ambulance.AmbulanceResponse
import com.sipmase.sipmase.model.apar.CekAparModel
import com.sipmase.sipmase.model.apat.ApatModel
import com.sipmase.sipmase.model.apat.ApatPickResponse
import com.sipmase.sipmase.model.apat.ApatResponse
import com.sipmase.sipmase.model.apat.HasilApatResponse
import com.sipmase.sipmase.model.damkar.DamkarResponse
import com.sipmase.sipmase.model.edgblok1.EdgBlokResponse
import com.sipmase.sipmase.model.edgblok2.EdgBlok2Response
import com.sipmase.sipmase.model.edgblok3.EdgBlok3Response
import com.sipmase.sipmase.model.ffblok.FFBlokResponse
import com.sipmase.sipmase.model.ffblok2.FFBlok2Response
import com.sipmase.sipmase.model.hydrant.HydrantPickResponse
import com.sipmase.sipmase.model.hydrant.ItemHydrantResponse
import com.sipmase.sipmase.model.hydrant.ScheduleHydrantPelaksanaResponse
import com.sipmase.sipmase.model.kebisingan.*
import com.sipmase.sipmase.model.pencahayaan.*
import com.sipmase.sipmase.model.postdata.*
import com.sipmase.sipmase.model.seawater.HasilSeaWaterResponse
import com.sipmase.sipmase.model.seawater.PostSeaWaterSchedule
import com.sipmase.sipmase.model.seawater.SeaWaterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.sql.Array

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("token_id") token_id: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("role") role: Int
    ): Call<RegisterResponse>


    @GET("getusers")
    fun getusers(
        @Query("role") role: Int
    ): Call<UsersResponse>

    @FormUrlEncoded
    @POST("deleteusers")
    fun deleteusers(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    //=========================APAR==============================
    @FormUrlEncoded
    @POST("apar")
    fun tambahapar(
        @Field("kode") kode: String,
        @Field("jenis") jenis: String,
        @Field("lokasi") lokasi: String,
        @Field("tgl_pengisian") tgl_pengisian: String
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("updateapar")
    fun updateapar(
        @Field("id") id: Int,
        @Field("kode") kode: String,
        @Field("jenis") jenis: String,
        @Field("lokasi") lokasi: String,
        @Field("tgl_pengisian") tgl_pengisian: String
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("updatekadaluarsa")
    fun updatekuapar(
        @Field("kode") kode: String,
        @Field("jenis") jenis: String,
        @Field("lokasi") lokasi: String,
        @Field("tgl_pengisian") tgl_pengisian: String
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("deleteapar")
    fun hapusapar(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @GET("apar")
    fun getapar(): Call<AparResponse>

    @GET("apar_kadaluarsa")
    fun apar_kadaluarsa(): Call<AparResponse>

    @GET("apar")
    fun getapar_pick(): Call<AparPickResponse>

    //Schedule
    @FormUrlEncoded
    @POST("schedule_apar")
    fun schedule_apar(
        @Field("kode_apar") kode_apar: String,
        @Field("tw") tw: String,
        @Field("tahun") tahun: String,
        @Field("tanggal_cek") tanggal_cek: String
    ): Call<PostDataResponse>

    //Hapus Schedule
    @FormUrlEncoded
    @POST("hapus_schedule_apar")
    fun hapus_schedule_apar(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @GET("getschedule")
    fun getapar_pick(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<ScheduleAparPelaksanaResponse>

    @GET("gethasil")
    fun getapar_hasil(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<ScheduleAparPelaksanaResponse>

    @GET("getschedule_pelaksana")
    fun getschedule_pelaksana(): Call<ScheduleAparPelaksanaResponse>

    @GET("cekapar")
    fun cekapar(@Query("kode") kode: String): Call<CekAparModel>

    @Headers("Content-Type: application/json")
    @POST("update_schedule_apar/{id}")
    fun update_schedule_apar(
        @Path("id") id :Int?,
        @Body post: PostScheduleApar
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("acc_apar")
    fun acc_apar(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_apar")
    fun return_apar(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @Multipart
    @POST("apar_pdf")
    fun create_pdf(
        @Part image: MultipartBody.Part?,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("jabatan") jabatan: RequestBody,
        @Part("nama") nama: RequestBody,
    ): Call<PostDataResponse>


    //==========================APAAT =======================
    //update apat
    @FormUrlEncoded
    @POST("apat")
    fun apat(
        @Field("kode") kode: String,
        @Field("no_bak") no_bak: String,
        @Field("lokasi") lokasi: String,
    ): Call<PostDataResponse>

    //update apat
    @FormUrlEncoded
    @POST("updateapat")
    fun updateapat(
        @Field("id") id: Int,
        @Field("kode") kode: String,
        @Field("no_bak") no_bak: String,
        @Field("lokasi") lokasi: String,
    ): Call<PostDataResponse>

    @GET("getapat")
    fun getapat(): Call<ApatResponse>

    @GET("getapat")
    fun getapat_pick(): Call<ApatPickResponse>

    @FormUrlEncoded
    @POST("deleteapat")
    fun hapusapat(
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //==========================Schedule APAAT =======================
    //Schedule
    @FormUrlEncoded
    @POST("schedule_apat")
    fun schedule_apat(
        @Field("kode_apat") kode_apat: String,
        @Field("tw") tw: String,
        @Field("tahun") tahun: String,
        @Field("tanggal_cek") tanggal_cek: String
    ): Call<PostDataResponse>

    @Headers("Content-Type: application/json")
    @POST("update_schedule_apat")
    fun update_schedule_apat(@Body post: UpdateScheduleApat): Call<PostDataResponse>

    @GET("cekapat")
    fun cekapat(@Query("kode") kode: String): Call<ApatModel>

    @GET("getschedule_apat")
    fun getschedule_apat(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<HasilApatResponse>

    @GET("getschedule_pelaksana_apat")
    fun getschedule_pelaksana_apat(): Call<HasilApatResponse>

    @GET("gethasil_apat")
    fun getapat_hasil(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<HasilApatResponse>

    //Hapus Schedule
    @FormUrlEncoded
    @POST("hapus_schedule_apat")
    fun hapus_schedule_apat(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("acc_apat")
    fun acc_apat(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_apat")
    fun return_apat(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @Multipart
    @POST("apat_pdf")
    fun apat_pdf(
        @Part image: MultipartBody.Part?,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("jabatan") jabatan: RequestBody,
        @Part("nama") nama: RequestBody,
    ): Call<PostDataResponse>

    //==========================Hydrant =======================
    @GET("gethydrant")
    fun itemhydrant(): Call<ItemHydrantResponse>

    @GET("gethydrant")
    fun gethydrant_pick(): Call<HydrantPickResponse>

    //Post Hydrant
    @FormUrlEncoded
    @POST("hydrant")
    fun hydrant(
        @Field("kode") kode: String,
        @Field("no_box") no_box: String,
        @Field("lokasi") lokasi: String,
    ): Call<PostDataResponse>
    //Update Hydrant
    @FormUrlEncoded
    @POST("updatehydrant")
    fun updatehydrant(
        @Field("kode") kode: String,
        @Field("no_box") no_box: String,
        @Field("lokasi") lokasi: String,
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //delete
    @FormUrlEncoded
    @POST("deletehydrant")
    fun deletehydrant(
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //==========================End Hydrant =======================
    //==========================Schedule Hydrant =======================
    @GET("getschedule_hydrant")
    fun getschedule_hydrant(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<ScheduleHydrantPelaksanaResponse>

    //Tambah Schedule
    @FormUrlEncoded
    @POST("schedule_hydrant")
    fun schedule_hydrant(
        @Field("kode_hydrant") kode_hydrant: String,
        @Field("tw") tw: String,
        @Field("tahun") tahun: String,
        @Field("tanggal_cek") tanggal_cek: String
    ): Call<PostDataResponse>

    //Hapus Schedule
    @FormUrlEncoded
    @POST("hapus_schedule_hydrant")
    fun hapus_schedule_hydrant(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @GET("getschedule_pelaksana_hydrant")
    fun getschedule_pelaksana_hydrant(): Call<ScheduleHydrantPelaksanaResponse>

    @Headers("Content-Type: application/json")
    @POST("update_schedule_hydrant")
    fun update_schedule_hydrant(@Body post: UpdateScheduleHydrant): Call<PostDataResponse>

    @GET("gethasil_hydrant")
    fun gethasil_hydrant(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<ScheduleHydrantPelaksanaResponse>

    @FormUrlEncoded
    @POST("acc_hydrant")
    fun acc_hydrant(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_hydrant")
    fun return_hydrant(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @Multipart
    @POST("hydrant_pdf")
    fun hydrant_pdf(
        @Part image: MultipartBody.Part?,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("jabatan") jabatan: RequestBody,
        @Part("nama") nama: RequestBody,
    ): Call<PostDataResponse>
    //==========================End Schedule Hydrant =======================

    //==========================Kebisingan =======================
    @GET("getkebisingan")
    fun getkebisingan(): Call<KebisinganResponse>

    @GET("getkebisingan")
    fun getkebisingan_pick(): Call<KebisinganPickResponse>

    //delete
    @FormUrlEncoded
    @POST("deletekebisingan")
    fun deletekebisingan(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    //Post
    @FormUrlEncoded
    @POST("kebisingan")
    fun kebisingan(
        @Field("kode") kode: String,
        @Field("lokasi") lokasi: String,
    ): Call<PostDataResponse>

    //Update
    @FormUrlEncoded
    @POST("updatekebisingan")
    fun updatekebisingan(
        @Field("lokasi") lokasi: String,
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //==========================End Kebisingan =======================

    //==========================Schedule Kebisingan =======================
    @GET("getschedule_kebisingan")
    fun getschedule_kebisingan(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<ScheduleKebisinganResponse>


    //Hapus Schedule
    @FormUrlEncoded
    @POST("hapus_schedule_kebisingan")
    fun hapus_schedule_kebisingan(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    //Tambah Schedule
    @Headers("Content-Type: application/json")
    @POST("schedule_kebisingan")
    fun schedule_kebisingan(@Body post: PostScheduleKebisingan): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("acc_kebisingan")
    fun acc_kebisingan(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_kebisingan")
    fun return_kebisingan(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @GET("getschedule_pelaksana_kebisingan")
    fun getschedule_pelaksana_kebisingan(): Call<ScheduleKebisinganResponse>

    @Headers("Content-Type: application/json")
    @POST("update_schedule_kebisingan")
    fun update_schedule_kebisingan(@Body post: UpdateScheduleKebisingan): Call<PostDataResponse>

    @GET("gethasil_kebisingan")
    fun gethasil_kebisingan(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<ScheduleKebisinganResponse>

    @Multipart
    @POST("kebisingan_pdf")
    fun kebisingan_pdf(
        @Part image: MultipartBody.Part?,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("jabatan") jabatan: RequestBody,
        @Part("nama") nama: RequestBody,
    ): Call<PostDataResponse>
    //========================== ENDSchedule Kebisingan =======================
    //========================== Pencahayaan =======================
    @GET("getpencahayaan")
    fun getpencahayaan(): Call<PencahayaanResponse>

    //dropwdown pencahayaan
    @GET("getpencahayaan")
    fun getpencahayaan_pick(): Call<PencahayaanPickResponse>

    //delete
    @FormUrlEncoded
    @POST("deletepencahayaan")
    fun deletepencahayaan(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    //Post
    @FormUrlEncoded
    @POST("pencahayaan")
    fun pencahayaan(
        @Field("kode") kode: String,
        @Field("lokasi") lokasi: String,
    ): Call<PostDataResponse>

    //Update
    @FormUrlEncoded
    @POST("updatepencahayaan")
    fun updatepencahayaan(
        @Field("lokasi") lokasi: String,
        @Field("id") id: Int
    ): Call<PostDataResponse>


    //========================== ENDPencahayaan =======================

    //==========================Schedule Pencahayaan =======================
    @GET("getschedule_pencahayaan")
    fun getschedule_pencahayaan(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<SchedulePencahayaanResponse>

    //Tambah Schedule
    @Headers("Content-Type: application/json")
    @POST("schedule_pencahayaan")
    fun schedule_pencahayaan(@Body post: PostSchedulePencahayaanResponse): Call<PostDataResponse>

    //Hapus Schedule
    @FormUrlEncoded
    @POST("hapus_schedule_pencahayaan")
    fun hapus_schedule_pencahayaan(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    //Return Pencahayaan
    @FormUrlEncoded
    @POST("return_pencahayaan")
    fun return_pencahayaan(
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //ACC Pencahayaan
    @FormUrlEncoded
    @POST("acc_pencahayaan")
    fun acc_pencahayaan(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @GET("gethasil_pencahayaan")
    fun gethasil_pencahayaan(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<SchedulePencahayaanResponse>

    @Multipart
    @POST("pencahayaan_pdf")
    fun pencahayaan_pdf(
        @Part image: MultipartBody.Part?,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("jabatan") jabatan: RequestBody,
        @Part("nama") nama: RequestBody,
    ): Call<PostDataResponse>

    @GET("getschedule_pelaksana_pencahayaan")
    fun getschedule_pelaksana_pencahayaan(): Call<SchedulePencahayaanResponse>

    @Headers("Content-Type: application/json")
    @POST("update_schedule_pencahayaan")
    fun update_schedule_pencahayaan(@Body post: UpdateSchedulePencahayaan): Call<PostDataResponse>

    //==========================END Schedule Pencahayaan =======================
    //==========================Schedule SeaWater =======================
    @Headers("Content-Type: application/json")
    @POST("seawater")
    fun schedule_seawater(@Body post: PostSeaWaterSchedule): Call<PostDataResponse>

    @GET("hasil_seawater")
    fun gethasil_seawater(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<SeaWaterResponse>

    @GET("seawater")
    fun get_seawater(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<SeaWaterResponse>

    //Hapus SeaWater
    @FormUrlEncoded
    @POST("hapus_seawater")
    fun hapus_seawater(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @GET("seawater_pelaksana")
    fun seawater_pelaksana(): Call<SeaWaterResponse>


    @Multipart
    @POST("update_seawater")
    fun update_seawater(
        @Part("id") id: RequestBody,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("shift") shift: RequestBody,
        @Part("is_status") is_status: RequestBody,
        @Part("tanggal_pemeriksa") tanggal_pemeriksa: RequestBody,
        @Part("md_waktu_pencatatan_sebelum_start") md_waktu_pencatatan_sebelum_start: RequestBody,
        @Part("md_waktu_pencatatan_sesudah_start") md_waktu_pencatatan_sesudah_start: RequestBody,
        @Part("md_discharge_press_sebelum_start") md_discharge_press_sebelum_start: RequestBody,
        @Part("md_discharge_press_sesudah_start") md_discharge_press_sesudah_start: RequestBody,
        @Part("md_suction_press_sebelum_start") md_suction_press_sebelum_start: RequestBody,
        @Part("md_suction_press_sesudah_start") md_suction_press_sesudah_start: RequestBody,
        @Part("md_lube_oil_level_sebelum_start") md_lube_oil_level_sebelum_start: RequestBody,
        @Part("md_lube_oil_level_sesudah_start") md_lube_oil_level_sesudah_start: RequestBody,
        @Part("de_waktu_pencatatan_sebelum_start") de_waktu_pencatatan_sebelum_start: RequestBody,
        @Part("de_waktu_pencatatan_sesudah_start") de_waktu_pencatatan_sesudah_start: RequestBody,
        @Part("de_engine_operating_hours_sebelum_start") de_engine_operating_hours_sebelum_start: RequestBody,
        @Part("de_engine_operating_hours_sesudah_start") de_engine_operating_hours_sesudah_start: RequestBody,
        @Part("de_battery_I_II_sebelum_start") de_battery_I_II_sebelum_start: RequestBody,
        @Part("de_battery_I_II_sesudah_start") de_battery_I_II_sesudah_start: RequestBody,
        @Part("de_battery_I_II_2_sebelum_start") de_battery_I_II_2_sebelum_start: RequestBody,
        @Part("de_battery_I_II_2_sesudah_start") de_battery_I_II_2_sesudah_start: RequestBody,
        @Part("de_oil_level_dipstick_sebelum_start") de_oil_level_dipstick_sebelum_start: RequestBody,
        @Part("de_oil_level_dipstick_sesudah_start") de_oil_level_dipstick_sesudah_start: RequestBody,
        @Part("de_fuel_level_sebelum_start") de_fuel_level_sebelum_start: RequestBody,
        @Part("de_fuel_level_sesudah_start") de_fuel_level_sesudah_start: RequestBody,
        @Part("de_engine_coolant_level_sebelum_start") de_engine_coolant_level_sebelum_start: RequestBody,
        @Part("de_engine_coolant_level_sesudah_start") de_engine_coolant_level_sesudah_start: RequestBody,
        @Part("de_oil_pressure_sebelum_start") de_oil_pressure_sebelum_start: RequestBody,
        @Part("de_oil_pressure_sesudah_start") de_oil_pressure_sesudah_start: RequestBody,
        @Part("de_air_filter_sebelum_start") de_air_filter_sebelum_start: RequestBody,
        @Part("de_air_filter_sesudah_start") de_air_filter_sesudah_start: RequestBody,
        @Part("de_exthoust_system_sebelum_start") de_exthoust_system_sebelum_start: RequestBody,
        @Part("de_exthoust_system_sesudah_start") de_exthoust_system_sesudah_start: RequestBody,
        @Part("de_cooling_water_press_sebelum_start") de_cooling_water_press_sebelum_start: RequestBody,
        @Part("de_cooling_water_press_sesudah_start") de_cooling_water_press_sesudah_start: RequestBody,
        @Part("de_coolant_temperature_sebelum_start") de_coolant_temperature_sebelum_start: RequestBody,
        @Part("de_coolant_temperature_sesudah_start") de_coolant_temperature_sesudah_start: RequestBody,
        @Part("de_speed_sebelum_start") de_speed_sebelum_start: RequestBody,
        @Part("de_speed_sesudah_start") de_speed_sesudah_start: RequestBody,
        @Part("de_suction_press_sebelum_start") de_suction_press_sebelum_start: RequestBody,
        @Part("de_suction_press_sesudah_start") de_suction_press_sesudah_start: RequestBody,
        @Part("de_discharge_press_sebelum_start") de_discharge_press_sebelum_start: RequestBody,
        @Part("de_discharge_press_sesudah_start") de_discharge_press_sesudah_start: RequestBody,
        @Part("de_fuel_temperature_sebelum_start") de_fuel_temperature_sebelum_start: RequestBody,
        @Part("de_fuel_temperature_sesudah_start") de_fuel_temperature_sesudah_start: RequestBody,
        @Part("de_engine_torque_sebelum_start") de_engine_torque_sebelum_start: RequestBody,
        @Part("de_engine_torque_sesudah_start") de_engine_torque_sesudah_start: RequestBody,
        @Part("de_persen_load_sebelum_start") de_persen_load_sebelum_start: RequestBody,
        @Part("de_persen_load_sesudah_start") de_persen_load_sesudah_start: RequestBody,
        @Part("de_flow_sea_water_sebelum_start") de_flow_sea_water_sebelum_start: RequestBody,
        @Part("de_flow_sea_water_sesudah_start") de_flow_sea_water_sesudah_start: RequestBody,
        @Part("de_press_by_pass_sebelum_start") de_press_by_pass_sebelum_start: RequestBody,
        @Part("de_press_by_pass_sesudah_start") de_press_by_pass_sesudah_start: RequestBody,

        @Part("catatan") catatan: RequestBody,

    ): Call<PostDataResponse>


    @FormUrlEncoded
    @POST("acc_seawater")
    fun acc_seawater(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_seawater")
    fun return_seawater(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("seawater_pdf")
    fun seawater_pdf(
        @Field("id") id: Int): Call<PostDataResponse>
    //==========================END Schedule SeaWater =======================
    //==========================Schedule FFBLOCK =======================
    @GET("ffblok")
    fun get_ffblok(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<FFBlokResponse>

    //Hapus SeaWater
    @FormUrlEncoded
    @POST("hapus_ffblok")
    fun hapus_ffblok(
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //tambah schedule
    @FormUrlEncoded
    @POST("schedule_ffblok")
    fun schedule_ffblok(
        @Field("hari") hari: String,
        @Field("tw") tw: String,
        @Field("tahun") tahun: String
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_ffblok")
    fun return_ffblok(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("acc_ffblok")
    fun acc_ffblok(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("ffblok_pdf")
    fun ffblok_pdf(
        @Field("id") id: Int): Call<PostDataResponse>

    @GET("hasil_ffblok1")
    fun gethasil_ffblok(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<FFBlokResponse>

    @GET("ffblok_pelaksana")
    fun ffblok_pelaksana(): Call<FFBlokResponse>

    @Multipart
    @POST("update_ffblok")
    fun update_ffblok(
        @Part("id") id: RequestBody,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("tanggal_pemeriksa") tanggal_pemeriksa: RequestBody,
        @Part("shift") shift: RequestBody,
        @Part("is_status") is_status: RequestBody,

        @Part("sp_waktu_pencatatan_sebelum_start") sp_waktu_pencatatan_sebelum_start: RequestBody,
        @Part("sp_waktu_pencatatan_sesudah_start") sp_waktu_pencatatan_sesudah_start: RequestBody,
        @Part("sp_suction_press_sebelum_start") sp_suction_press_sebelum_start: RequestBody,
        @Part("sp_suction_press_sesudah_start") sp_suction_press_sesudah_start: RequestBody,
        @Part("sp_discharge_press_sebelum_start") sp_discharge_press_sebelum_start: RequestBody,
        @Part("sp_discharge_press_sesudah_start") sp_discharge_press_sesudah_start: RequestBody,
        @Part("sp_fuel_level_sebelum_start") sp_fuel_level_sebelum_start: RequestBody,
        @Part("sp_fuel_level_sesudah_start") sp_fuel_level_sesudah_start: RequestBody,
        @Part("sp_auto_start_sebelum_start") sp_auto_start_sebelum_start: RequestBody,
        @Part("sp_auto_start_sesudah_start") sp_auto_start_sesudah_start: RequestBody,

        @Part("md_waktu_pencatatan_sebelum_start") md_waktu_pencatatan_sebelum_start: RequestBody,
        @Part("md_waktu_pencatatan_sesudah_start") md_waktu_pencatatan_sesudah_start: RequestBody,
        @Part("md_suction_press_sebelum_start") md_suction_press_sebelum_start: RequestBody,
        @Part("md_suction_press_sesudah_start") md_suction_press_sesudah_start: RequestBody,
        @Part("md_discharge_press_sebelum_start") md_discharge_press_sebelum_start: RequestBody,
        @Part("md_discharge_press_sesudah_start") md_discharge_press_sesudah_start: RequestBody,
        @Part("md_full_level_sebelum_start") md_full_level_sebelum_start: RequestBody,
        @Part("md_full_level_sesudah_start") md_full_level_sesudah_start: RequestBody,
        @Part("md_auto_start_sebelum_start") md_auto_start_sebelum_start: RequestBody,
        @Part("md_auto_start_sesudah_start") md_auto_start_sesudah_start: RequestBody,

        @Part("de_waktu_pencatatan_sebelum_start") de_waktu_pencatatan_sebelum_start: RequestBody,
        @Part("de_waktu_pencatatan_sesudah_start") de_waktu_pencatatan_sesudah_start: RequestBody,
        @Part("de_lube_oil_press_sebelum_start") de_lube_oil_press_sebelum_start: RequestBody,
        @Part("de_lube_oil_press_sesudah_start") de_lube_oil_press_sesudah_start: RequestBody,
        @Part("de_battery_voltage_sebelum_start") de_battery_voltage_sebelum_start: RequestBody,
        @Part("de_battery_voltage_sesudah_start") de_battery_voltage_sesudah_start: RequestBody,
        @Part("de_battery_ampere_sebelum_start") de_battery_ampere_sebelum_start: RequestBody,
        @Part("de_battery_ampere_sesudah_start") de_battery_ampere_sesudah_start: RequestBody,
        @Part("de_battery_level_sebelum_start") de_battery_level_sebelum_start: RequestBody,
        @Part("de_battery_level_sesudah_start") de_battery_level_sesudah_start: RequestBody,
        @Part("de_fuel_level_sebelum_start") de_fuel_level_sebelum_start: RequestBody,
        @Part("de_fuel_level_sesudah_start") de_fuel_level_sesudah_start: RequestBody,
        @Part("de_lube_oil_level_sebelum_start") de_lube_oil_level_sebelum_start: RequestBody,
        @Part("de_lube_oil_level_sesudah_start") de_lube_oil_level_sesudah_start: RequestBody,
        @Part("de_water_cooler_level_sebelum_start") de_water_cooler_level_sebelum_start: RequestBody,
        @Part("de_water_cooler_level_sesudah_start") de_water_cooler_level_sesudah_start: RequestBody,
        @Part("de_speed_sebelum_start") de_speed_sebelum_start: RequestBody,
        @Part("de_speed_sesudah_start") de_speed_sesudah_start: RequestBody,
        @Part("de_suction_press_sebelum_start") de_suction_press_sebelum_start: RequestBody,
        @Part("de_suction_press_sesudah_start") de_suction_press_sesudah_start: RequestBody,
        @Part("de_discharge_press_sebelum_start") de_discharge_press_sebelum_start: RequestBody,
        @Part("de_discharge_press_sesudah_start") de_discharge_press_sesudah_start: RequestBody,
        @Part("de_auto_start_sebelum_start") de_auto_start_sebelum_start: RequestBody,
        @Part("de_auto_start_sesudah_start") de_auto_start_sesudah_start: RequestBody,

        @Part("catatan") catatan: RequestBody,
//        @Part("k3_nama") k3_nama: RequestBody,
//        @Part k3_ttd: MultipartBody.Part?,
//        @Part("operator_nama") operator_nama: RequestBody,
//        @Part  operator_ttd: MultipartBody.Part?,
//        @Part("supervisor_nama") supervisor_nama: RequestBody,
//        @Part supervisor_ttd: MultipartBody.Part?
    ): Call<PostDataResponse>
    //==========================END Schedule FFBLOCK =======================
    //==========================Schedule FFBLOCK2 =======================
    @GET("ffblok2")
    fun get_ffblok2(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<FFBlok2Response>

    //Hapus SeaWater
    @FormUrlEncoded
    @POST("hapus_ffblok2")
    fun hapus_ffblok2(
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //tambah schedule
    @FormUrlEncoded
    @POST("schedule_ffblok2")
    fun schedule_ffblok2(
        @Field("hari") hari: String,
        @Field("tw") tw: String,
        @Field("tahun") tahun: String
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_ffblok2")
    fun return_ffblok2(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("acc_ffblok2")
    fun acc_ffblok2(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("ffblok2_pdf")
    fun ffblok2_pdf(
        @Field("id") id: Int): Call<PostDataResponse>

    @GET("hasil_ffblok2")
    fun gethasil_ffblok2(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<FFBlok2Response>

    @GET("ffblok2_pelaksana")
    fun ffblok2_pelaksana(): Call<FFBlok2Response>

    @Multipart
    @POST("update_ffblok2")
    fun update_ffblok2(
        @Part("id") id: RequestBody,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("tanggal_pemeriksa") tanggal_pemeriksa: RequestBody,
        @Part("shift") shift: RequestBody,
        @Part("is_status") is_status: RequestBody,

        @Part("sp_waktu_pencatatan_sebelum_start") sp_waktu_pencatatan_sebelum_start: RequestBody,
        @Part("sp_waktu_pencatatan_sesudah_start") sp_waktu_pencatatan_sesudah_start: RequestBody,
        @Part("sp_suction_press_sebelum_start") sp_suction_press_sebelum_start: RequestBody,
        @Part("sp_suction_press_sesudah_start") sp_suction_press_sesudah_start: RequestBody,
        @Part("sp_discharge_press_sebelum_start") sp_discharge_press_sebelum_start: RequestBody,
        @Part("sp_discharge_press_sesudah_start") sp_discharge_press_sesudah_start: RequestBody,
        @Part("sp_auto_start_sebelum_start") sp_auto_start_sebelum_start: RequestBody,
        @Part("sp_auto_start_sesudah_start") sp_auto_start_sesudah_start: RequestBody,
        @Part("sp_auto_stop_sebelum_start") sp_auto_stop_sebelum_start: RequestBody,
        @Part("sp_auto_stop_sesudah_start") sp_auto_stop_sesudah_start: RequestBody,

        @Part("md_waktu_pencatatan_sebelum_start") md_waktu_pencatatan_sebelum_start: RequestBody,
        @Part("md_waktu_pencatatan_sesudah_start") md_waktu_pencatatan_sesudah_start: RequestBody,
        @Part("md_suction_press_sebelum_start") md_suction_press_sebelum_start: RequestBody,
        @Part("md_suction_press_sesudah_start") md_suction_press_sesudah_start: RequestBody,
        @Part("md_discharge_press_sebelum_start") md_discharge_press_sebelum_start: RequestBody,
        @Part("md_discharge_press_sesudah_start") md_discharge_press_sesudah_start: RequestBody,
        @Part("md_full_level_sebelum_start") md_full_level_sebelum_start: RequestBody,
        @Part("md_full_level_sesudah_start") md_full_level_sesudah_start: RequestBody,
        @Part("md_auto_start_sebelum_start") md_auto_start_sebelum_start: RequestBody,
        @Part("md_auto_start_sesudah_start") md_auto_start_sesudah_start: RequestBody,

        @Part("de_waktu_pencatatan_sebelum_start") de_waktu_pencatatan_sebelum_start: RequestBody,
        @Part("de_waktu_pencatatan_sesudah_start") de_waktu_pencatatan_sesudah_start: RequestBody,
        @Part("de_lube_oil_press_sebelum_start") de_lube_oil_press_sebelum_start: RequestBody,
        @Part("de_lube_oil_press_sesudah_start") de_lube_oil_press_sesudah_start: RequestBody,
        @Part("de_battery_voltage_sebelum_start") de_battery_voltage_sebelum_start: RequestBody,
        @Part("de_battery_voltage_sesudah_start") de_battery_voltage_sesudah_start: RequestBody,
        @Part("de_battery_ampere_sebelum_start") de_battery_ampere_sebelum_start: RequestBody,
        @Part("de_battery_ampere_sesudah_start") de_battery_ampere_sesudah_start: RequestBody,
        @Part("de_battery_level_sebelum_start") de_battery_level_sebelum_start: RequestBody,
        @Part("de_battery_level_sesudah_start") de_battery_level_sesudah_start: RequestBody,
        @Part("de_fuel_level_sebelum_start") de_fuel_level_sebelum_start: RequestBody,
        @Part("de_fuel_level_sesudah_start") de_fuel_level_sesudah_start: RequestBody,
        @Part("de_lube_oil_level_sebelum_start") de_lube_oil_level_sebelum_start: RequestBody,
        @Part("de_lube_oil_level_sesudah_start") de_lube_oil_level_sesudah_start: RequestBody,
        @Part("de_water_cooler_level_sebelum_start") de_water_cooler_level_sebelum_start: RequestBody,
        @Part("de_water_cooler_level_sesudah_start") de_water_cooler_level_sesudah_start: RequestBody,
        @Part("de_speed_sebelum_start") de_speed_sebelum_start: RequestBody,
        @Part("de_speed_sesudah_start") de_speed_sesudah_start: RequestBody,
        @Part("de_suction_press_sebelum_start") de_suction_press_sebelum_start: RequestBody,
        @Part("de_suction_press_sesudah_start") de_suction_press_sesudah_start: RequestBody,
        @Part("de_discharge_press_sebelum_start") de_discharge_press_sebelum_start: RequestBody,
        @Part("de_discharge_press_sesudah_start") de_discharge_press_sesudah_start: RequestBody,
        @Part("de_auto_start_sebelum_start") de_auto_start_sebelum_start: RequestBody,
        @Part("de_auto_start_sesudah_start") de_auto_start_sesudah_start: RequestBody,

        @Part("catatan") catatan: RequestBody,
    ): Call<PostDataResponse>
    //==========================END Schedule FFBLOCK2 =======================
    //==========================Schedule EDGBLOK1 =======================
    @GET("edgblok1")
    fun get_edgblok1(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<EdgBlokResponse>

    //Hapus SeaWater
    @FormUrlEncoded
    @POST("hapus_edgblok1")
    fun hapus_edgblok1(
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //tambah schedule
    @FormUrlEncoded
    @POST("schedule_edgblok1")
    fun schedule_edgblok1(
        @Field("hari") hari: String,
        @Field("tw") tw: String,
        @Field("tahun") tahun: String
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_edgblok1")
    fun return_edgblok1(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("acc_edgblok1")
    fun acc_edgblok1(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("edgblok1_pdf")
    fun edgblok1_pdf(
        @Field("id") id: Int): Call<PostDataResponse>

    @GET("hasil_edgblok1")
    fun gethasil_edgblok1(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<EdgBlokResponse>

    @GET("edgblok1_pelaksana")
    fun edgblok1_pelaksana(): Call<EdgBlokResponse>

    @Multipart
    @POST("update_edgblok1")
    fun update_edgblok1(
        @Part("id") id: RequestBody,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("tanggal_pemeriksa") tanggal_pemeriksa: RequestBody,
        @Part("shift") shift: RequestBody,
        @Part("is_status") is_status: RequestBody,

        @Part("p_waktu_pencatatan_sebelum_start") sp_waktu_pencatatan_sebelum_start: RequestBody,
        @Part("p_waktu_pencatatan_sesudah_start") sp_waktu_pencatatan_sesudah_start: RequestBody,
        @Part("p_oil_pressure_sebelum_start") p_oil_pressure_sebelum_start: RequestBody,
        @Part("p_oil_pressure_sesudah_start") p_oil_pressure_sesudah_start: RequestBody,
        @Part("p_oil_temprature_sebelum_start") p_oil_temprature_sebelum_start: RequestBody,
        @Part("p_oil_temprature_sesudah_start") p_oil_temprature_sesudah_start: RequestBody,
        @Part("p_water_temprature_sebelum_start") p_water_temprature_sebelum_start: RequestBody,
        @Part("p_water_temprature_sesudah_start") p_water_temprature_sesudah_start: RequestBody,
        @Part("p_speed_sebelum_start") p_speed_sebelum_start: RequestBody,
        @Part("p_speed_sesudah_start") p_speed_sesudah_start: RequestBody,
        @Part("p_hour_meter_1_sebelum_start") p_hour_meter_1_sebelum_start: RequestBody,
        @Part("p_hour_meter_1_sesudah_start") p_hour_meter_1_sesudah_start: RequestBody,
        @Part("p_hour_meter_2_sebelum_start") p_hour_meter_2_sebelum_start: RequestBody,
        @Part("p_hour_meter_2_sesudah_start") p_hour_meter_2_sesudah_start: RequestBody,
        @Part("p_main_line_voltage_sebelum_start") p_main_line_voltage_sebelum_start: RequestBody,
        @Part("p_main_line_voltage_sesudah_start") p_main_line_voltage_sesudah_start: RequestBody,
        @Part("p_main_line_frequency_sebelum_start") p_main_line_frequency_sebelum_start: RequestBody,
        @Part("p_main_line_frequency_sesudah_start") p_main_line_frequency_sesudah_start: RequestBody,
        @Part("p_generator_breaker_sebelum_start") p_generator_breaker_sebelum_start: RequestBody,
        @Part("p_generator_breaker_sesudah_start") p_generator_breaker_sesudah_start: RequestBody,
        @Part("p_generator_voltage_sebelum_start") p_generator_voltage_sebelum_start: RequestBody,
        @Part("p_generator_voltage_sesudah_start") p_generator_voltage_sesudah_start: RequestBody,
        @Part("p_generator_frequency_sebelum_start") p_generator_frequency_sebelum_start: RequestBody,
        @Part("p_generator_frequency_sesudah_start") p_generator_frequency_sesudah_start: RequestBody,
        @Part("p_load_current_sebelum_start") p_load_current_sebelum_start: RequestBody,
        @Part("p_load_current_sesudah_start") p_load_current_sesudah_start: RequestBody,
        @Part("p_daya_sebelum_start") p_daya_sebelum_start: RequestBody,
        @Part("p_daya_sesudah_start") p_daya_sesudah_start: RequestBody,
        @Part("p_cos_sebelum_start") p_cos_sebelum_start: RequestBody,
        @Part("p_cos_sesudah_start") p_cos_sesudah_start: RequestBody,
        @Part("p_battery_charge_voltase_sebelum_start") p_battery_charge_voltase_sebelum_start: RequestBody,
        @Part("p_battery_charge_voltase_sesudah_start") p_battery_charge_voltase_sesudah_start: RequestBody,
        @Part("p_hour_sebelum_start") p_hour_sebelum_start: RequestBody,
        @Part("p_hour_sesudah_start") p_hour_sesudah_start: RequestBody,
        @Part("p_lube_oil_level_sebelum_start") p_lube_oil_level_sebelum_start: RequestBody,
        @Part("p_lube_oil_level_sesudah_start") p_lube_oil_level_sesudah_start: RequestBody,
        @Part("p_accu_level_sebelum_start") p_accu_level_sebelum_start: RequestBody,
        @Part("p_accu_level_sesudah_start") p_accu_level_sesudah_start: RequestBody,
        @Part("p_radiator_level_sebelum_start") p_radiator_level_sebelum_start: RequestBody,
        @Part("p_radiator_level_sesudah_start") p_radiator_level_sesudah_start: RequestBody,
        @Part("p_fuel_oil_level_sebelum_start") p_fuel_oil_level_sebelum_start: RequestBody,
        @Part("p_fuel_oil_level_sesudah_start") p_fuel_oil_level_sesudah_start: RequestBody,

        @Part("catatan") catatan: RequestBody,
    ): Call<PostDataResponse>
    //==========================END Schedule EDGBLOK1 =======================
    //==========================Schedule EDGBLOK2 =======================
    @GET("edgblok2")
    fun get_edgblok2(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<EdgBlok2Response>

    //Hapus SeaWater
    @FormUrlEncoded
    @POST("hapus_edgblok2")
    fun hapus_edgblok2(
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //tambah schedule
    @FormUrlEncoded
    @POST("schedule_edgblok2")
    fun schedule_edgblok2(
        @Field("hari") hari: String,
        @Field("tw") tw: String,
        @Field("tahun") tahun: String
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_edgblok2")
    fun return_edgblok2(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("acc_edgblok2")
    fun acc_edgblok2(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("edgblok2_pdf")
    fun edgblok2_pdf(
        @Field("id") id: Int): Call<PostDataResponse>

    @GET("hasil_edgblok2")
    fun gethasil_edgblok2(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<EdgBlok2Response>

    @GET("edgblok2_pelaksana")
    fun edgblok2_pelaksana(): Call<EdgBlok2Response>

    @Multipart
    @POST("update_edgblok2")
    fun update_edgblok2(
        @Part("id") id: RequestBody,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("tanggal_pemeriksa") tanggal_pemeriksa: RequestBody,
        @Part("shift") shift: RequestBody,
        @Part("is_status") is_status: RequestBody,

        @Part("p_waktu_pencatatan_sebelum_start") sp_waktu_pencatatan_sebelum_start: RequestBody,
        @Part("p_waktu_pencatatan_sesudah_start") sp_waktu_pencatatan_sesudah_start: RequestBody,
        @Part("p_oil_pressure_sebelum_start") p_oil_pressure_sebelum_start: RequestBody,
        @Part("p_oil_pressure_sesudah_start") p_oil_pressure_sesudah_start: RequestBody,
        @Part("p_fuel_pressure_sebelum_start") p_fuel_pressure_sebelum_start: RequestBody,
        @Part("p_fuel_pressure_sesudah_start") p_fuel_pressure_sesudah_start: RequestBody,
        @Part("p_fuel_temprature_sebelum_start") p_fuel_temprature_sebelum_start: RequestBody,
        @Part("p_fuel_temprature_sesudah_start") p_fuel_temprature_sesudah_start: RequestBody,
        @Part("p_coolant_pressure_sebelum_start") p_coolant_pressure_sebelum_start: RequestBody,
        @Part("p_coolant_pressure_sesudah_start") p_coolant_pressure_sesudah_start: RequestBody,
        @Part("p_coolant_temprature_sebelum_start") p_coolant_temprature_sebelum_start: RequestBody,
        @Part("p_coolant_temprature_sesudah_start") p_coolant_temprature_sesudah_start: RequestBody,
        @Part("p_speed_sebelum_start") p_speed_sebelum_start: RequestBody,
        @Part("p_speed_sesudah_start") p_speed_sesudah_start: RequestBody,
        @Part("p_inlet_temprature_sebelum_start") p_inlet_temprature_sebelum_start: RequestBody,
        @Part("p_inlet_temprature_sesudah_start") p_inlet_temprature_sesudah_start: RequestBody,
        @Part("p_turbo_pleasure_sebelum_start") p_turbo_pleasure_sebelum_start: RequestBody,
        @Part("p_turbo_pleasure_sesudah_start") p_turbo_pleasure_sesudah_start: RequestBody,
        @Part("p_generator_breaker_sebelum_start") p_generator_breaker_sebelum_start: RequestBody,
        @Part("p_generator_breaker_sesudah_start") p_generator_breaker_sesudah_start: RequestBody,
        @Part("p_generator_voltage_sebelum_start") p_generator_voltage_sebelum_start: RequestBody,
        @Part("p_generator_voltage_sesudah_start") p_generator_voltage_sesudah_start: RequestBody,
        @Part("p_generator_frequency_sebelum_start") p_generator_frequency_sebelum_start: RequestBody,
        @Part("p_generator_frequency_sesudah_start") p_generator_frequency_sesudah_start: RequestBody,
        @Part("p_generator_current_sebelum_start") p_generator_current_sebelum_start: RequestBody,
        @Part("p_generator_current_sesudah_start") p_generator_current_sesudah_start: RequestBody,
        @Part("p_load_sebelum_start") p_load_current_sebelum_start: RequestBody,
        @Part("p_load_sesudah_start") p_load_current_sesudah_start: RequestBody,
        @Part("p_battery_charge_voltase_sebelum_start") p_battery_charge_voltase_sebelum_start: RequestBody,
        @Part("p_battery_charge_voltase_sesudah_start") p_battery_charge_voltase_sesudah_start: RequestBody,
        @Part("p_lube_oil_level_sebelum_start") p_lube_oil_level_sebelum_start: RequestBody,
        @Part("p_lube_oil_level_sesudah_start") p_lube_oil_level_sesudah_start: RequestBody,
        @Part("p_accu_level_sebelum_start") p_accu_level_sebelum_start: RequestBody,
        @Part("p_accu_level_sesudah_start") p_accu_level_sesudah_start: RequestBody,
        @Part("p_radiator_level_sebelum_start") p_radiator_level_sebelum_start: RequestBody,
        @Part("p_radiator_level_sesudah_start") p_radiator_level_sesudah_start: RequestBody,
        @Part("p_fuel_oil_level_sebelum_start") p_fuel_oil_level_sebelum_start: RequestBody,
        @Part("p_fuel_oil_level_sesudah_start") p_fuel_oil_level_sesudah_start: RequestBody,

        @Part("catatan") catatan: RequestBody,

    ): Call<PostDataResponse>
    //==========================END Schedule EDGBLOK2 =======================
    //==========================Schedule EDGBLOK3 =======================
    @GET("edgblok3")
    fun get_edgblok3(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<EdgBlok3Response>

    //Hapus SeaWater
    @FormUrlEncoded
    @POST("hapus_edgblok3")
    fun hapus_edgblok3(
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //tambah schedule
    @FormUrlEncoded
    @POST("schedule_edgblok3")
    fun schedule_edgblok3(
        @Field("hari") hari: String,
        @Field("tw") tw: String,
        @Field("tahun") tahun: String
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_edgblok3")
    fun return_edgblok3(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("acc_edgblok3")
    fun acc_edgblok3(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("edgblok3_pdf")
    fun edgblok3_pdf(
        @Field("id") id: Int): Call<PostDataResponse>

    @GET("hasil_edgblok3")
    fun gethasil_edgblok3(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<EdgBlok3Response>

    @GET("edgblok3_pelaksana")
    fun edgblok3_pelaksana(): Call<EdgBlok3Response>

    @Multipart
    @POST("update_edgblok3")
    fun update_edgblok3(
        @Part("id") id: RequestBody,
        @Part("tw") tw: RequestBody,
        @Part("tahun") tahun: RequestBody,
        @Part("tanggal_pemeriksa") tanggal_pemeriksa: RequestBody,
        @Part("shift") shift: RequestBody,
        @Part("is_status") is_status: RequestBody,

        @Part("oil_press_sebelum_start") oil_press_sebelum_start: RequestBody,
        @Part("oil_press_sesudah_start") oil_press_sesudah_start: RequestBody,
        @Part("oil_temp_sebelum_start") oil_temp_sebelum_start: RequestBody,
        @Part("oil_temp_sesudah_start") oil_temp_sesudah_start: RequestBody,
        @Part("water_cool_temp_sebelum_start") water_cool_temp_sebelum_start: RequestBody,
        @Part("water_cool_temp_sesudah_start") water_cool_temp_sesudah_start: RequestBody,
        @Part("speed_sebelum_start") speed_sebelum_start: RequestBody,
        @Part("speed_sesudah_start") speed_sesudah_start: RequestBody,
        @Part("hour_meter_sebelum_start") hour_meter_sebelum_start: RequestBody,
        @Part("hour_meter_sesudah_start") hour_meter_sesudah_start: RequestBody,
        @Part("ml_voltage_sebelum_start") ml_voltage_sebelum_start: RequestBody,
        @Part("ml_voltage_sesudah_start") ml_voltage_sesudah_start: RequestBody,
        @Part("ml_freq_sebelum_start") ml_freq_sebelum_start: RequestBody,
        @Part("ml_freq_sesudah_start") ml_freq_sesudah_start: RequestBody,
        @Part("gen_break_sebelum_start") gen_break_sebelum_start: RequestBody,
        @Part("gen_break_sesudah_start") gen_break_sesudah_start: RequestBody,
        @Part("gen_vol_sebelum_start") gen_vol_sebelum_start: RequestBody,
        @Part("gen_vol_sesudah_start") gen_vol_sesudah_start: RequestBody,
        @Part("gen_freq_sebelum_start") gen_freq_sebelum_start: RequestBody,
        @Part("gen_freq_sesudah_start") gen_freq_sesudah_start: RequestBody,
        @Part("load_cur_sebelum_start") load_cur_sebelum_start: RequestBody,
        @Part("load_cur_sesudah_start") load_cur_sesudah_start: RequestBody,
        @Part("daya_sebelum_start") daya_sebelum_start: RequestBody,
        @Part("daya_sesudah_start") daya_sesudah_start: RequestBody,
        @Part("cos_sebelum_start") cos_sebelum_start: RequestBody,
        @Part("cos_sesudah_start") cos_sesudah_start: RequestBody,
        @Part("bat_charge_vol_sebelum_start") bat_charge_vol_sebelum_start: RequestBody,
        @Part("bat_charge_vol_sesudah_start") bat_charge_vol_sesudah_start: RequestBody,
        @Part("hour_sebelum_start") hour_sebelum_start: RequestBody,
        @Part("hour_sesudah_start") hour_sesudah_start: RequestBody,
        @Part("lube_oil_sebelum_start") lube_oil_sebelum_start: RequestBody,
        @Part("lube_oil_sesudah_start") lube_oil_sesudah_start: RequestBody,
        @Part("accu_sebelum_start") accu_sebelum_start: RequestBody,
        @Part("accu_sesudah_start") accu_sesudah_start: RequestBody,
        @Part("radiator_sebelum_start") radiator_sebelum_start: RequestBody,
        @Part("radiator_sesudah_start") radiator_sesudah_start: RequestBody,
        @Part("fuel_oil_sebelum_start") fuel_oil_sebelum_start: RequestBody,
        @Part("fuel_oil_sesudah_start") fuel_oil_sesudah_start: RequestBody,
        @Part("temp_bearing_gen_sebelum_start") temp_bearing_gen_sebelum_start: RequestBody,
        @Part("temp_bearing_gen_sesudah_start") temp_bearing_gen_sesudah_start: RequestBody,
        @Part("tamp_winding_u_sebelum_start") tamp_winding_u_sebelum_start: RequestBody,
        @Part("tamp_winding_u_sesudah_start") tamp_winding_u_sesudah_start: RequestBody,
        @Part("temp_winding_v_sebelum_start") temp_winding_v_sebelum_start: RequestBody,
        @Part("temp_winding_v_sesudah_start") temp_winding_v_sesudah_start: RequestBody,
        @Part("temp_winding_w_sebelum_start") temp_winding_w_sebelum_start: RequestBody,
        @Part("temp_winding_w_sesudah_start") temp_winding_w_sesudah_start: RequestBody,
        @Part("belt_radiator_fan_sebelum_start") belt_radiator_fan_sebelum_start: RequestBody,
        @Part("belt_radiator_fan_sesudah_start") belt_radiator_fan_sesudah_start: RequestBody,

        @Part("catatan") catatan: RequestBody,

    ): Call<PostDataResponse>
    //==========================END Schedule EDGBLOK2 =======================
    //==========================Schedule DAMKAR =======================
    @GET("damkar")
    fun get_damkar(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<DamkarResponse>

    //Hapus SeaWater
    @FormUrlEncoded
    @POST("hapus_damkar")
    fun hapus_damkar(
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //tambah schedule
    @FormUrlEncoded
    @POST("schedule_damkar")
    fun schedule_damkar(
        @Field("hari") hari: String,
        @Field("tw") tw: String,
        @Field("tahun") tahun: String
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_damkar")
    fun return_damkar(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("acc_damkar")
    fun acc_damkar(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @Multipart
    @POST("damkar_pdf/{id}")
    fun damkarku_pdf(
        @Path("id") id :Int?,
        @Part image: MultipartBody.Part?,
        @Part("jabatan") jabatan: RequestBody,
        @Part("nama") nama: RequestBody,
    ): Call<PostDataResponse>

    @GET("hasil_damkar")
    fun gethasil_damkar(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<DamkarResponse>

    @GET("damkar_pelaksana")
    fun damkar_pelaksana(): Call<DamkarResponse>

    @Headers("Content-Type: application/json")
    @POST("update_damkar")
    fun update_damkarku(@Body post: UpdateDamkar): Call<PostDataResponse>
    //==========================END Schedule DAMKAR =======================
    //==========================Schedule AMBULANCE =======================
    @GET("ambulance")
    fun get_ambulance(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<AmbulanceResponse>

    //Hapus SeaWater
    @FormUrlEncoded
    @POST("hapus_ambulance")
    fun hapus_ambulance(
        @Field("id") id: Int
    ): Call<PostDataResponse>
    //tambah schedule
    @FormUrlEncoded
    @POST("schedule_ambulance")
    fun schedule_ambulance(
        @Field("hari") hari: String,
        @Field("tw") tw: String,
        @Field("tahun") tahun: String
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("return_ambulance")
    fun return_ambulance(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @FormUrlEncoded
    @POST("acc_ambulance")
    fun acc_ambulance(
        @Field("id") id: Int
    ): Call<PostDataResponse>

    @Multipart
    @POST("ambulance_pdf/{id}")
    fun ambulanceku_pdf(
        @Path("id") id :Int?,
        @Part image: MultipartBody.Part?,
        @Part("jabatan") jabatan: RequestBody,
        @Part("nama") nama: RequestBody,
    ): Call<PostDataResponse>

    @GET("hasil_ambulance")
    fun gethasil_ambulance(
        @Query("tw") tw: String,
        @Query("tahun") tahun: String
    ): Call<AmbulanceResponse>

    @GET("ambulance_pelaksana")
    fun ambulance_pelaksana(): Call<AmbulanceResponse>

    @Headers("Content-Type: application/json")
    @POST("update_ambulance")
    fun update_ambulanceku(@Body post: UpdateAmbulance): Call<PostDataResponse>
    //==========================END Schedule DAMKAR =======================
//    ttdk3 seawater
    @Multipart
    @POST("update_seawater")
    fun ttdk3_seawater(
        @Part k3_ttd: MultipartBody.Part?,
        @Part("k3_nama") k3_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_seawater")
    fun ttdoperator_seawater(
        @Part operator_ttd: MultipartBody.Part?,
        @Part("operator_nama") operator_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_seawater")
    fun ttdspv_seawater(
        @Part supervisor_ttd: MultipartBody.Part?,
        @Part("supervisor_nama") supervisor_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>
//    ttdk3 ffblok1
    @Multipart
    @POST("update_ffblok")
    fun ttdk3_ffblok(
        @Part k3_ttd: MultipartBody.Part?,
        @Part("k3_nama") k3_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_ffblok")
    fun ttdoperator_ffblok(
        @Part operator_ttd: MultipartBody.Part?,
        @Part("operator_nama") operator_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_ffblok")
    fun ttdspv_ffblok(
        @Part supervisor_ttd: MultipartBody.Part?,
        @Part("supervisor_nama") supervisor_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_ffblok2")
    fun ttdk3_ffblok2(
        @Part k3_ttd: MultipartBody.Part?,
        @Part("k3_nama") k3_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_ffblok2")
    fun ttdoperator_ffblok2(
        @Part operator_ttd: MultipartBody.Part?,
        @Part("operator_nama") operator_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_ffblok2")
    fun ttdspv_ffblok2(
        @Part supervisor_ttd: MultipartBody.Part?,
        @Part("supervisor_nama") supervisor_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_edgblok1")
    fun ttdk3_edgblok1(
        @Part k3_ttd: MultipartBody.Part?,
        @Part("k3_nama") k3_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_edgblok1")
    fun ttdoperator_edgblok1(
        @Part operator_ttd: MultipartBody.Part?,
        @Part("operator_nama") operator_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_edgblok1")
    fun ttdspv_edgblok1(
        @Part supervisor_ttd: MultipartBody.Part?,
        @Part("supervisor_nama") supervisor_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_edgblok2")
    fun ttdk3_edgblok2(
        @Part k3_ttd: MultipartBody.Part?,
        @Part("k3_nama") k3_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_edgblok2")
    fun ttdoperator_edgblok2(
        @Part operator_ttd: MultipartBody.Part?,
        @Part("operator_nama") operator_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_edgblok2")
    fun ttdspv_edgblok2(
        @Part supervisor_ttd: MultipartBody.Part?,
        @Part("supervisor_nama") supervisor_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_edgblok3")
    fun ttdk3_edgblok3(
        @Part k3_ttd: MultipartBody.Part?,
        @Part("k3_nama") k3_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_edgblok3")
    fun ttdoperator_edgblok3(
        @Part operator_ttd: MultipartBody.Part?,
        @Part("operator_nama") operator_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>

    @Multipart
    @POST("update_edgblok3")
    fun ttdspv_edgblok3(
        @Part supervisor_ttd: MultipartBody.Part?,
        @Part("supervisor_nama") supervisor_nama: RequestBody,
        @Part("id") id: RequestBody,
    ): Call<PostDataResponse>



}

