package com.example.gestindeproyectos.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.databinding.FragmentRegisterProfileBinding
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.model.CollaboratorState
import com.example.gestindeproyectos.model.CollaboratorType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class RegisterProfileFragment : Fragment() {
    private var _binding: FragmentRegisterProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var registerProfileViewModel: RegisterProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerProfileViewModel =
            ViewModelProvider(this)[RegisterProfileViewModel::class.java]

        _binding = FragmentRegisterProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        registerProfileViewModel.email.observe(viewLifecycleOwner) {
            binding.userEmail.text = it
        }

        val saveButton = binding.saveProfile
        saveButton.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid!!
            val email = binding.userEmail.text.toString()
            val profilePicture = binding.userProfilePicture.drawable
            val name = binding.userNameEditText.text.toString()
            val lastname = binding.userLastnameEditText.text.toString()
            val phone = binding.userPhoneEditText.text.toString()
            val id = binding.userIdEditText.text.toString()
            val department = binding.userDepartmentEditText.text.toString()
            val state = CollaboratorState.INACTIVE
            val type = CollaboratorType.NONE

            DB.instance.addCollaborator(
                userId,
                email,
                name,
                lastname,
                phone,
                id,
                department,
                state,
                type
            )

            val bitmap = (profilePicture as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val ref = FirebaseStorage.getInstance().getReference("/profile_pictures/$userId")

            ref.putBytes(data)
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
                }
                .addOnFailureListener {
                    Log.e(TAG, "Failed to upload image to storage: ${it.message}")
                }

            // Navigate to the home screen
            val navController = findNavController()
            navController.navigate(R.id.nav_home)
        }

        val addProfilePictureButton = binding.floatingActionButton
        addProfilePictureButton.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST
            )
        }

        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            Picasso.get().load(imageUri).resize(1000, 1000).centerCrop().into(binding.userProfilePicture)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "RegisterProfileFragment"
        private const val PICK_IMAGE_REQUEST = 1
    }
}