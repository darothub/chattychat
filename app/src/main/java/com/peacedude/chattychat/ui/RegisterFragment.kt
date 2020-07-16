package com.peacedude.chattychat.ui

import android.R.attr.password
import android.content.Intent
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.peacedude.chattychat.R
import com.peacedude.chattychat.extension.backgroundColor
import com.peacedude.chattychat.extension.hide
import com.peacedude.chattychat.extension.show
import kotlinx.android.synthetic.main.fragment_register.*
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

    lateinit var mAuth: FirebaseAuth
    val TAG = "REGISTER Fragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createAccountBtn.text = "Create account"
        mAuth = FirebaseAuth.getInstance()


        createAccountBtn.setOnClickListener {
            val email = email_edittext.text.toString()
            val password = password_edittext.text.toString()
            progressBar.show()
            createAccountBtn.backgroundColor(R.color.colorWhite)
            createAccount(email, password)
        }
    }

    private fun createAccount(email: String, password: String) {
        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(),
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            progressBar.hide()
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = mAuth.currentUser
                            Toast.makeText(
                                requireContext(), "Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        } else {
                            progressBar.hide()
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })

        }
        catch (e:Exception){
            progressBar.hide()
            createAccountBtn.backgroundColor(R.color.colorPrimary)

            Toast.makeText(
                requireContext(), e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }


}