package com.peacedude.chattychat.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.security.crypto.MasterKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.peacedude.chattychat.R
import com.peacedude.chattychat.extension.SharedPref
import com.peacedude.chattychat.extension.hide
import com.peacedude.chattychat.extension.show
import kotlinx.android.synthetic.main.fragment_account_setting.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [AccountSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountSettingFragment : Fragment() {
    val TAG = "AccountSetting"

    private lateinit var changePictureButton: Button
    lateinit var changeStatusButton: Button
    private val masterKey by lazy {
        MasterKey.Builder(requireContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS).
        setKeyScheme(MasterKey.KeyScheme.AES256_GCM).
        build()
    }
    private val sharedPreferences by lazy {
        SharedPref.sharedPref(requireContext(), masterKey)
    }
    lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changePictureButton = edit_pix_btn.findViewById(R.id.btn)
        changeStatusButton = edit_profile_btn.findViewById(R.id.btn)
        changePictureButton.text = getString(R.string.change_picture)
        changeStatusButton.text = getString(R.string.edit_status)
        val userId = mAuth.currentUser?.uid
        mDatabase =  FirebaseDatabase.getInstance().reference.child("Users").child(userId.toString())

        val name = sharedPreferences.getString("name", "No name")
        val status = sharedPreferences.getString("status", getString(R.string.hi_there))
        profile_display_name.text = name
        profile_status.hint = status
        profile_status.setText(status.toString())
        (setting_toolbar as androidx.appcompat.widget.Toolbar).menu.clear()
        val navController = Navigation.findNavController((setting_toolbar as androidx.appcompat.widget.Toolbar))
        NavigationUI.setupWithNavController((setting_toolbar as androidx.appcompat.widget.Toolbar), navController)

        changeStatusButton.setOnClickListener {
            profile_status.setText("")
            profile_status.isClickable = true
            profile_status.isEnabled = true
            profile_status.isFocusable = true
            profile_status.requestFocus()
        }

        profile_status.setOnKeyListener { view, keycode, keyEvent ->

            val newStatus = profile_status.text.toString()
            if(keyEvent.action == KeyEvent.ACTION_DOWN && keycode == KEYCODE_ENTER){
                setting_progress_bar.show()
                profile_status.hide()
                mDatabase.child("status").setValue(newStatus).addOnCompleteListener {task ->
                    when {
                        task.isSuccessful -> {
                            findNavController().navigate(R.id.accountSettingFragment)
                            val sharedPrefEditor = sharedPreferences.edit()
                            sharedPrefEditor.putString("status", newStatus)
                            sharedPrefEditor.apply()
                            setting_progress_bar.hide()
                            profile_status.show()
                            profile_status.isEnabled = false
                            Toast.makeText(requireContext(), "new status set successfully to $newStatus", Toast.LENGTH_LONG).show()
                        }
                    }

                }



            }
            true

        }


    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
//        requireActivity().recreate()

    }

}