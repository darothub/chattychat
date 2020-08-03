package com.peacedude.chattychat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.peacedude.chattychat.R


class StartActivity : AppCompatActivity() {
    lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);

        if(intent != null){
            when(intent.extras?.get("finish")){
                true ->finish()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return (Navigation.findNavController(this, R.id.fragment).navigateUp()
                || super.onSupportNavigateUp())
    }


}