package com.peacedude.chattychat.ui

import android.R.attr.password
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.peacedude.chattychat.R
import com.peacedude.chattychat.extension.*
import kotlinx.android.synthetic.main.fragment_register.*
import java.lang.Exception


/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    val createAccountBtn by lazy {
        create_an_account.findViewById(R.id.btn) as Button
    }
    val progressBar by lazy {
        create_an_account.findViewById(R.id.progress_bar) as ProgressBar
    }


    val masterKey by lazy {
        MasterKey.Builder(requireContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS).
        setKeyScheme(MasterKey.KeyScheme.AES256_GCM).
        build()
    }
    val sharedPreferences by lazy {
        SharedPref.sharedPref(requireContext(), masterKey)
    }


    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase:DatabaseReference
    val TAG = "RegisterFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createAccountBtn.text = getString(R.string.create_account)
        mAuth = FirebaseAuth.getInstance()




        createAccountBtn.setOnClickListener {
            val displayName = displayName_edittext.text.toString()
            val email = email_edittext.text.toString()
            val password = password_edittext.text.toString()
            progressBar.show()
            createAccountBtn.deactivate()
            createAccountBtn.backgroundColor(R.color.colorWhite)
            createAccount(email, password, displayName)
        }
    }


    private fun createAccount(email: String, password: String, displayName:String) {
        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(),
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            progressBar.hide()
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = mAuth.currentUser
                            var userId = user?.uid
                            mDatabase =  FirebaseDatabase.getInstance().getReference().child("Users").child(userId.toString())
                            val userCredentials = HashMap<String, String>()
                            userCredentials["name"] = displayName
                            userCredentials["status"] = getString(R.string.hi_there)
                            userCredentials["image"] = "default"
                            userCredentials["thumb_nail"] = "default"
                            mDatabase.setValue(userCredentials).addOnCompleteListener {t ->
                                when{
                                    t.isSuccessful ->{
                                        Toast.makeText(
                                            requireContext(), "Successful",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        val editor = sharedPreferences.edit()

                                        editor.putString("name", displayName)
                                        editor.putString("status", getString(R.string.hi_there))
                                        editor.apply()
                                        startActivity(Intent(requireContext(), MainActivity::class.java))
                                        requireActivity().finish()
                                    }
                                }
                            }

                        } else {
                            progressBar.hide()
                            createAccountBtn.activate()
                            createAccountBtn.backgroundColor(R.color.colorPrimary)
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireContext(), "${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })

        }
        catch (e:Exception){
            progressBar.hide()
            createAccountBtn.activate()
            createAccountBtn.backgroundColor(R.color.colorPrimary)

            Toast.makeText(
                requireContext(), e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }


}