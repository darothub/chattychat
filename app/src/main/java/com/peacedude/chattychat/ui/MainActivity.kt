package com.peacedude.chattychat.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.peacedude.chattychat.R
import com.peacedude.chattychat.adapters.MainViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var mAuth: FirebaseAuth
    lateinit var sharedPreferences: EncryptedSharedPreferences
    val masterKey by lazy {
        MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS).
        setKeyScheme(MasterKey.KeyScheme.AES256_GCM).
        build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(main_toolbar as Toolbar)
//        supportActionBar?.setTitle(R.string.app_name)

//        val colorDrawable = ColorDrawable(Color.parseColor("#B54747"))
//        supportActionBar?.setBackgroundDrawable(colorDrawable)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        super.onCreateOptionsMenu(menu)
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        super.onOptionsItemSelected(item)
//        when (item.itemId) {
//            R.id.logout -> {
//                mAuth.signOut()
//                sendToStartActivity()
//            }
//        }
//        return true
//    }
}

