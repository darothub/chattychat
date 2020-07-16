package com.peacedude.chattychat.ui

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.peacedude.chattychat.R
import com.peacedude.chattychat.extension.backgroundColor
import com.peacedude.chattychat.extension.hide
import com.peacedude.chattychat.extension.show
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
    val TAG = "LoginFragment"

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
                            val user = mAuth.currentUser
                            Toast.makeText(
                                requireContext(), "Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.hide()
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        // ...
                    })
        }
        catch (e:Exception){
            progressBar.hide()
            loginBtn.backgroundColor(R.color.colorPrimary)

            Toast.makeText(
                requireContext(), e.message,
                Toast.LENGTH_SHORT
            ).show()
        }


    }


}