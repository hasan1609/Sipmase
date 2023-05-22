package com.sipmase.sipmase.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context?) {
    val privateMode = 0
    val privateName ="login"
    var Pref : SharedPreferences?=context?.getSharedPreferences(privateName,privateMode)
    var editor : SharedPreferences.Editor?=Pref?.edit()

    private val islogin = "login"
    fun setLogin(check: Boolean){
        editor?.putBoolean(islogin,check)
        editor?.commit()
    }

    fun getLogin():Boolean?
    {
        return Pref?.getBoolean(islogin,false)
    }

    private val isloginadmin = "loginadmin"
    fun setLoginadmin(check: Boolean){
        editor?.putBoolean(isloginadmin,check)
        editor?.commit()
    }

    fun getLoginadmin():Boolean?
    {
        return Pref?.getBoolean(isloginadmin,false)
    }

    private val isToken = "isToken"
    fun setToken(check: String){
        editor?.putString(isToken,check)
        editor?.commit()
    }

    fun getToken():String?
    {
        return Pref?.getString(isToken,"")
    }

    private val isnama = "isnama"
    fun setNama(check: String){
        editor?.putString(isnama,check)
        editor?.commit()
    }

    fun getNama():String?
    {
        return Pref?.getString(isnama,"")
    }

    private val isusername = "isusername"
    fun setUsername(check: String){
        editor?.putString(isusername,check)
        editor?.commit()
    }

    fun getUsername():String?
    {
        return Pref?.getString(isusername,"")
    }
}