package com.brainstormideas.tokencash.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.brainstormideas.tokencash.Login
import java.util.*

class SessionManager {

    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var _context: Context? = null
    var PRIVATE_MODE = 0
    private val PREF_NAME = "Pref"
    private val IS_LOGIN = "IsLoggedIn"
    val KEY_NAME = "name"
    val KEY_EMAIL = "email"
    val KEY_TOKEN = "token"

    fun SessionManager(context: Context?) {
        _context = context
        pref = _context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref?.edit()
    }


    fun createLoginSession(name: String?, email: String?) {

        editor!!.putBoolean(IS_LOGIN, true)
        editor!!.putString(KEY_NAME, name)
        editor!!.putString(KEY_EMAIL, email)
        editor!!.commit()
    }

    fun checkLogin() {
        if (!isLoggedIn()) {
            val i = Intent(_context, Login::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            _context!!.startActivity(i)
        }
    }

    fun getUserDetails(): HashMap<String, String?>? {

        val user = HashMap<String, String?>()
        user[KEY_NAME] = pref!!.getString(KEY_NAME, null)
        user[KEY_EMAIL] = pref!!.getString(KEY_EMAIL, null)
        user[KEY_TOKEN] = pref!!.getString(KEY_TOKEN, null)
        return user
    }

    fun logoutUser() {

        editor!!.clear()
        editor!!.commit()
        val i = Intent(_context, Login::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        _context!!.startActivity(i)
    }

    fun isLoggedIn(): Boolean {
        return pref!!.getBoolean(IS_LOGIN, false)
    }
}