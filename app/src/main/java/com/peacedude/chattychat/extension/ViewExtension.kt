package com.peacedude.chattychat.extension

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.GONE
}

fun View.backgroundColor(@ColorRes color:Int){
    this.setBackgroundColor(ContextCompat.getColor(this.context, color))
}

fun View.deactivate(){
    this.isClickable = false
    this.isEnabled = false
}

fun View.activate(){
    this.isClickable = true
    this.isEnabled = true
}

object SharedPref{
//    lateinit var sharedPreferences:EncryptedSharedPreferences
    fun sharedPref(context:Context, masterKey: MasterKey): EncryptedSharedPreferences {
    return EncryptedSharedPreferences.create(
         context,
         "ChattyFile",
         masterKey,
         EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
         EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
     ) as EncryptedSharedPreferences
    }
}