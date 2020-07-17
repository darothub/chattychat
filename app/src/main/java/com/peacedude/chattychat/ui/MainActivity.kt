package com.peacedude.chattychat.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.peacedude.chattychat.R
import com.peacedude.chattychat.adapters.MainViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar as Toolbar)
        supportActionBar?.setTitle(R.string.app_name)
//        val colorDrawable = ColorDrawable(Color.parseColor("#B54747"))
//        supportActionBar?.setBackgroundDrawable(colorDrawable)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        var currentUser = mAuth.currentUser

        if(currentUser == null){
            sendToStartActivity()
        }
        val adapter = MainViewPagerAdapter(supportFragmentManager)
        main_viewPager.adapter = adapter
        main_tabLayout.setupWithViewPager(main_viewPager)
        (main_toolbar as Toolbar).setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.logout -> {
                    mAuth.signOut()
                    sendToStartActivity()
                    true
                }
                else -> false
            }
        }
    }

    private fun sendToStartActivity() {
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId){
            R.id.logout -> {
                mAuth.signOut()
                sendToStartActivity()
            }
        }
        return true
    }
}

