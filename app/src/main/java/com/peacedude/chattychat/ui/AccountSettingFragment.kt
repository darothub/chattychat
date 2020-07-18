package com.peacedude.chattychat.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.security.crypto.MasterKey
import com.google.firebase.auth.FirebaseAuth
import com.peacedude.chattychat.R
import com.peacedude.chattychat.extension.SharedPref
import kotlinx.android.synthetic.main.fragment_account_setting.*
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 * Use the [AccountSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountSettingFragment : Fragment() {
    val TAG = "AccountSetting"

    lateinit var changePictureButton: Button
    lateinit var changeProfileButton: Button
    val masterKey by lazy {
        MasterKey.Builder(requireContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS).
        setKeyScheme(MasterKey.KeyScheme.AES256_GCM).
        build()
    }
    val sharedPreferences by lazy {
        SharedPref.sharedPref(requireContext(), masterKey)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        changeProfileButton = edit_profile_btn.findViewById(R.id.btn)
        changePictureButton.text = getString(R.string.change_picture)
        changeProfileButton.text = getString(R.string.edit_profile)

        val name = sharedPreferences.getString("name", "No name")
        val status = sharedPreferences.getString("status", getString(R.string.hi_there))
        profile_display_name.text = name
        profile_status.text = status
        (setting_toolbar as androidx.appcompat.widget.Toolbar).menu.clear()
        val navController = Navigation.findNavController((setting_toolbar as androidx.appcompat.widget.Toolbar))
        NavigationUI.setupWithNavController((setting_toolbar as androidx.appcompat.widget.Toolbar), navController)


    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
        requireActivity().recreate()
    }

}