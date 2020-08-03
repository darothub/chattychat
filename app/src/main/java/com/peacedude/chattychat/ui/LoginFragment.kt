package com.peacedude.chattychat.ui

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.peacedude.chattychat.R
import com.peacedude.chattychat.extension.SharedPref
import com.peacedude.chattychat.extension.backgroundColor
import com.peacedude.chattychat.extension.hide
import com.peacedude.chattychat.extension.show
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.reusables.*
import java.lang.Exception


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth
    lateinit var loginBtn:Button
    private val progressBar by lazy {
        login_btn.findViewById(R.id.progress_bar) as ProgressBar
    }
    private val TAG = "LoginFragment"
    val masterKey by lazy {
        MasterKey.Builder(requireContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS).
        setKeyScheme(MasterKey.KeyScheme.AES256_GCM).
        build()
    }
    val sharedPreferences by lazy {
        SharedPref.sharedPref(requireContext(), masterKey)
    }
    private lateinit var mDatabase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        loginBtn = login_btn.findViewById(R.id.btn)
        loginBtn.text = getString(R.string.login)

        loginBtn.setOnClickListener {
            val email = login_email_edittext.text.toString()
            val password = login_password_edittext.text.toString()
            progressBar.show()
            loginBtn.backgroundColor(R.color.colorWhite)
            login(email, password)
        }
    }


    private fun login(email: String, password: String) {
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(),
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            progressBar.hide()
                            loginBtn.backgroundColor(R.color.colorPrimary)
                            loginBtn.isEnabled = false
                            val user = mAuth.currentUser

                            requireActivity().gdToast("Successful", Gravity.BOTTOM)

                            mDatabase =  FirebaseDatabase.getInstance().reference.child("Users")

                            startActivity(Intent(this.activity, MainActivity::class.java))
                            requireActivity().finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.hide()
                            loginBtn.backgroundColor(R.color.colorPrimary)
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            requireActivity().gdErrorToast("Authentication failed.", Gravity.BOTTOM)

                        }
                    })
        }
        catch (e:Exception){
            progressBar.hide()
            loginBtn.backgroundColor(R.color.colorPrimary)

            requireActivity().gdErrorToast(e.message.toString(), Gravity.BOTTOM)

        }


    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
//        this.onDestroy()
        Log.i(TAG, "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")

    }



}