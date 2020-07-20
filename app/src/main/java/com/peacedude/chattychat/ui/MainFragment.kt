package com.peacedude.chattychat.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.peacedude.chattychat.R
import com.peacedude.chattychat.adapters.MainViewPagerAdapter
import com.peacedude.chattychat.extension.SharedPref
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private val TAG = "MainFragment"
    lateinit var mAuth: FirebaseAuth
    val masterKey by lazy {
        MasterKey.Builder(requireContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS).
        setKeyScheme(MasterKey.KeyScheme.AES256_GCM).
        build()
    }
    val sharedPreferences by lazy {
        SharedPref.sharedPref(requireContext(), masterKey)
    }
    lateinit var adapter : MainViewPagerAdapter
    lateinit var valueListener:ValueEventListener
    private lateinit var mDatabase: DatabaseReference
    private val currentUser by lazy {
        mAuth.currentUser
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()


        if (currentUser == null) {
            sendToStartActivity()
        }



        setupViewPager()
        toolBarMenuListener()

        setTextOnButton("Chats")
        viewPagerPageListener()

        nextBtn.setOnClickListener {
            if(main_viewPager.currentItem +1 < adapter.count){
                main_viewPager.currentItem = main_viewPager.currentItem+1
            }
            else{
                main_viewPager.currentItem = 0
            }
        }
    }

    private fun retrievingDataFromFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference()
        val userId = currentUser?.uid

        val editor = sharedPreferences.edit()

        valueListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val tableNameRef = snapshot.child("Users")
                val displayName =
                    tableNameRef.child(userId.toString()).child("name").value
                val status = tableNameRef.child(userId.toString()).child("status").value
                Log.i(TAG, displayName.toString())
                (main_toolbar as Toolbar).title = displayName.toString().capitalize()
                editor.putString("name", displayName.toString())
                editor.putString("status", status.toString())
                editor.apply()
            }
        }

        mDatabase.addValueEventListener(valueListener)
    }

    private fun viewPagerPageListener() {
        main_viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        setTextOnButton("Chats")
                    }
                    1 -> {
                        setTextOnButton("Friends")
                    }
                    2 -> {
                        setTextOnButton("Requests")
                    }
                }
            }

        })
    }

    private fun toolBarMenuListener() {
        (main_toolbar as Toolbar).setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    mAuth.signOut()
                    sendToStartActivity()
                    true
                }
                R.id.account_setting -> {
                    findNavController().navigate(R.id.accountSettingFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViewPager() {
        adapter = MainViewPagerAdapter(requireActivity().supportFragmentManager)
        main_viewPager.adapter = adapter
        main_tabLayout.setupWithViewPager(main_viewPager)
        main_tabLayout.setSelectedTabIndicatorColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorWhite
            )
        )
    }

    private fun setTextOnButton(string: String) {
        nextBtn.setText(string)
    }

    private fun sendToStartActivity() {
        startActivity(Intent(requireContext(), StartActivity::class.java))
        requireActivity().finish()
    }

    override fun onStart() {
        super.onStart()

        Log.i(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        retrievingDataFromFirebase()


        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        mDatabase.removeEventListener(valueListener)
        Log.i(TAG, "onPause")
    }

}