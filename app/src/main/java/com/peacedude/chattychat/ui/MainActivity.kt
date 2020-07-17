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
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.peacedude.chattychat.R
import com.peacedude.chattychat.adapters.MainViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(main_toolbar as Toolbar)
//        supportActionBar?.setTitle(R.string.app_name)
        (main_toolbar as Toolbar).setTitle(R.string.app_name)
//        val colorDrawable = ColorDrawable(Color.parseColor("#B54747"))
//        supportActionBar?.setBackgroundDrawable(colorDrawable)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        var currentUser = mAuth.currentUser

        if (currentUser == null) {
            sendToStartActivity()
        }
        val adapter = MainViewPagerAdapter(supportFragmentManager)
        main_viewPager.adapter = adapter
        main_tabLayout.setupWithViewPager(main_viewPager)
        main_tabLayout.setSelectedTabIndicatorColor(
            ContextCompat.getColor(
                this,
                R.color.colorWhite
            )
        )
        (main_toolbar as Toolbar).setOnMenuItemClickListener {item ->
            when (item.itemId) {
                R.id.logout -> {
                    mAuth.signOut()
                    sendToStartActivity()
                    true
                }
                else -> false
            }
        }

        setTextOnButton("Chats")
        main_viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        setTextOnButton("Chats")
                    }
                    1-> {
                        setTextOnButton("Friends")
                    }
                    2->{
                        setTextOnButton("Requests")
                    }
                }
            }

        })

        nextBtn.setOnClickListener {
            if(main_viewPager.currentItem +1 < adapter.count){
                main_viewPager.currentItem = main_viewPager.currentItem+1
            }
            else{
                main_viewPager.currentItem = 0
            }
        }



    }

    private fun setTextOnButton(string: String) {
        nextBtn.setText(string)
    }

    private fun sendToStartActivity() {
        startActivity(Intent(this, StartActivity::class.java))
        finish()
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

