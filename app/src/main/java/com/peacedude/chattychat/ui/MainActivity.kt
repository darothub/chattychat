package com.peacedude.chattychat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.peacedude.chattychat.R

class MainActivity : AppCompatActivity() {

    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        var currentUser = mAuth.currentUser

        if(currentUser == null){
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}

fun main(){
    var res = breakPalindrome("aaabbaaa")
    var a = "aaa"
    var b = a.reversed()
    println(res)
}


fun breakPalindrome(palindromeStr: String): String {
    // Write your code here
    var result = ""
    var impossible = "IMPOSSIBLE"
    var lowercase = palindromeStr.toLowerCase()
    var stringArray = lowercase.split("").toMutableList()
    stringArray.removeAt(0)
    println(stringArray)
    if(lowercase.length < 2){
        lowercase = impossible
        return lowercase
    }
    for(i in 0..stringArray.size){
        if(stringArray[i] != "a"){
            stringArray[i] = "a"
            break
        }
    }
    var joined = stringArray.joinToString("")
    var reversedString = joined.reversed()
    if(reversedString == joined){
        result = impossible
    }
    else{
        result = joined
    }
    return result

}