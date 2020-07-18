package com.peacedude.chattychat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.peacedude.chattychat.R
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    lateinit var registerBtn:Button
    lateinit var signinBtn:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerBtn = register_btn.findViewById(R.id.btn)
        signinBtn = signin_btn.findViewById<Button>(R.id.btn)
        registerBtn.text = getString(R.string.register)
        signinBtn.text = getString(R.string.login)

        registerBtn.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        signinBtn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }


}