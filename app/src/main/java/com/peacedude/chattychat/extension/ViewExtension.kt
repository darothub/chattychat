package com.peacedude.chattychat.extension

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.peacedude.chattychat.R

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.GONE
}

fun View.backgroundColor(@ColorRes color:Int){
    this.setBackgroundColor(ContextCompat.getColor(this.context, color))
}