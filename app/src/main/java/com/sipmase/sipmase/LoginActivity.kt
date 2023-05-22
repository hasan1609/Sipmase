package com.sipmase.sipmase

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.sipmase.sipmase.admin.HomeActivity
import com.sipmase.sipmase.model.LoginResponse
import com.sipmase.sipmase.pelaksana.MainActivity
import com.sipmase.sipmase.session.SessionManager
import com.sipmase.sipmase.webservice.ApiClient
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), AnkoLogger {

    var api = ApiClient.instance()

    var token : String? = null
    lateinit var progressDialog: ProgressDialog
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressDialog = ProgressDialog(this)
        sessionManager = SessionManager(this)

        btnlogin.setOnClickListener {
            val username = edtusername.text.toString().trim()
            val password = edtpassword.text.toString().trim()
            if (username.isNotEmpty() && password.isNotEmpty()){
                gettoken(username,password,it)
            }else{
                snackbar("jangan kosongi kolom",it)
            }
        }
    }

    fun snackbar(text : String, view : View){
        Snackbar.make(view,text,3000).show()
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

    private fun gettoken(username : String, password : String,view : View) {
        loading(true)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                toast("gagal dapat token")
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result
            if (token != null) {
                api.login(username,password,token!!).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        try {
                            if (response.isSuccessful){
                                if (response.body()!!.status ==1) {
                                    sessionManager.setToken(response.body()!!.data!!.tokenId!!)
                                    sessionManager.setNama(response.body()!!.data!!.name!!)
                                    sessionManager.setUsername(response.body()!!.data!!.username!!)
                                    sessionManager.setLoginadmin(true)
                                    loading(false)
                                    toast("login berhasil")
                                    startActivity<HomeActivity>()
                                    finish()
                                }else if (response.body()!!.status ==2){
                                    sessionManager.setToken(response.body()!!.data!!.tokenId!!)
                                    sessionManager.setNama(response.body()!!.data!!.name!!)
                                    sessionManager.setUsername(response.body()!!.data!!.username!!)
                                    sessionManager.setLogin(true)
                                    loading(false)
                                    toast("login berhasil")
                                    startActivity<MainActivity>()

                                }
                                else {
                                    loading(false)
                                    snackbar("Email atau password salah",view)

                                }

                            }else{
                                snackbar("Kesalahan aplikasi",view)
                            }


                        }catch (e : Exception){
                            loading(false)
                            info { "dinda ${e.message }${response.code()} " }
                        }

                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        loading(false)
                        info { "erro ${t.message } " }
                        snackbar("Kesalahan Jaringan",view)

                    }

                })

            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (sessionManager.getLoginadmin() == true){
            startActivity<HomeActivity>()
            finish()
        }else if (sessionManager.getLogin() == true){
            startActivity<MainActivity>()
            finish()
        }
    }
}