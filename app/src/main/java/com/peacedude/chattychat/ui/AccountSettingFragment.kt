package com.peacedude.chattychat.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.security.crypto.MasterKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.peacedude.chattychat.R
import com.peacedude.chattychat.extension.SharedPref
import com.peacedude.chattychat.extension.hide
import com.peacedude.chattychat.extension.show
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_account_setting.*


/**
 * A simple [Fragment] subclass.
 * Use the [AccountSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountSettingFragment : Fragment() {
    val TAG = "AccountSetting"
    val GALLERY_PHOTO_CODE = 1

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
    private lateinit var profileImageRef:StorageReference
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
        profileImageRef = FirebaseStorage.getInstance().reference


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
                            requireActivity().gdToast("new status set successfully to $newStatus", Gravity.BOTTOM)
                        }
                    }
                }
            }
            true
        }

        changePictureButton.setOnClickListener {
//            val galleryIntent = Intent()
//            galleryIntent.type = "image/*"
//            galleryIntent.action = Intent.ACTION_GET_CONTENT
//
//            startActivityForResult(Intent.createChooser(galleryIntent, "Choose a picture"), GALLERY_PHOTO_CODE)
            CropImage.activity()
                .start(requireContext(), this)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == GALLERY_PHOTO_CODE){
//
//            val imageUri = data?.data
//            requireActivity().gdToast("Workings $imageUri", Gravity.BOTTOM)
//            CropImage.activity(imageUri)
//                .setAspectRatio(1, 1)
//                .start(requireActivity())
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                val filePath = profileImageRef.child("profile_images").child("profile_images1.jpg")
                requireActivity().gdToast("Workings $resultUri", Gravity.BOTTOM)
                filePath.putFile(resultUri).addOnCompleteListener {
                    if(it.isSuccessful){
                        requireActivity().gdToast("Working $resultUri", Gravity.BOTTOM)
                    }
                    else{
                        requireActivity().gdErrorToast("${result.error}", Gravity.BOTTOM)
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                requireActivity().gdErrorToast("${result.error}", Gravity.BOTTOM)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
//        requireActivity().recreate()

    }

}