package com.example.gestindeproyectos.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.databinding.FragmentEditProfileBinding
import com.example.gestindeproyectos.db.DB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var userProfilePicture: ImageView
    private var updateImageButtonClicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        editProfileViewModel =
            ViewModelProvider(this)[EditProfileViewModel::class.java]

        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        userProfilePicture = binding.userProfilePicture
        val buttonUpload = binding.uploadProfilePicture

        // Set an OnClickListener on the button
        buttonUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
            updateImageButtonClicked = true
        }

        val saveButton = binding.saveProfile
        saveButton.setOnClickListener {
            val userPhone = binding.userPhoneEdit.text.toString()
            val userDepartment = binding.userDepartmentEdit.text.toString()

            // Save the profile modifications
            DB.instance.updateCollaborator(
                FirebaseAuth.getInstance().currentUser?.uid!!,
                userPhone,
                userDepartment,
            )

            // Update the profile picture
            if (updateImageButtonClicked) {
                val bitmap = (userProfilePicture.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                val uid = FirebaseAuth.getInstance().currentUser?.uid
                val ref = FirebaseStorage.getInstance().getReference("/profile_pictures/$uid")

                ref.putBytes(data)
                    .addOnSuccessListener {
                        Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "Failed to upload image to storage: ${it.message}")
                    }
            }

            // Navigate back to the profile fragment
            val navController = findNavController()
            navController.navigate(R.id.nav_profile)
        }

        // set the fields for edit profile view model
        val userProfilePictureViewMode: ImageView = binding.userProfilePicture
        editProfileViewModel.userProfilePicture.observe(viewLifecycleOwner) {
            userProfilePictureViewMode.setImageDrawable(it)
        }
        val userNameTextView: TextView = binding.userName
        editProfileViewModel.userName.observe(viewLifecycleOwner) {
            userNameTextView.text = it
        }
        val userEmailTextView: TextView = binding.userEmail
        editProfileViewModel.userEmail.observe(viewLifecycleOwner) {
            userEmailTextView.text = it
        }
        val userId: TextView = binding.userId
        editProfileViewModel.userId.observe(viewLifecycleOwner) {
            userId.text = it
        }
        val userPhone: TextView = binding.userPhoneEdit
        editProfileViewModel.userPhone.observe(viewLifecycleOwner) {
            userPhone.text = it
        }
        val userDepartmentTextView: TextView = binding.userDepartmentEdit
        editProfileViewModel.userDepartment.observe(viewLifecycleOwner) {
            userDepartmentTextView.text = it
        }
        val userState: TextView = binding.userState
        editProfileViewModel.userState.observe(viewLifecycleOwner) {
            userState.text = it
        }
        val userProject: TextView = binding.userProject
        editProfileViewModel.userProject.observe(viewLifecycleOwner) {
            userProject.text = it
        }

        return binding.root
    }

    // onActivityResult method
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            Picasso.get().load(imageUri).resize(500, 500).centerCrop().into(userProfilePicture)
        } else {
            // Handle the error
            Log.e(TAG, "Error while picking the image")
        }
    }

    override fun onResume() {
        super.onResume()
        editProfileViewModel.fetchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 1
        private const val TAG = "EditProfileFragment"
    }
}