package com.example.gestindeproyectos.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.databinding.FragmentProfileBinding
import com.example.gestindeproyectos.db.DB
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    // Firebase Auth
    private lateinit var auth: FirebaseAuth

    // Binding
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // View models
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val editProfileButton: Button = binding.editProfile
        editProfileButton.setOnClickListener {
            editProfile()
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        DB.instance.fetchCollaborator(currentUser!!.email!!).thenAccept { collaborator ->
            activity?.runOnUiThread {
                if (collaborator == null) {
                    Log.d(TAG, "Collaborator not found")

                    // Get the navigation controller
                    val navController = findNavController()

                    // Replace the current fragment with the registration fragment
                    // TODO: Create the registration fragment
                    navController.navigate(R.id.nav_forum)
                }
            }
        }

        // Set the fields for profile view
        val userProfilePictureViewMode: ImageView = binding.userProfilePicture
        profileViewModel.userProfilePicture.observe(viewLifecycleOwner) {
            userProfilePictureViewMode.setImageDrawable(it)
        }
        val userNameTextView: TextView = binding.userName
        profileViewModel.userName.observe(viewLifecycleOwner) {
            userNameTextView.text = it
        }
        val userEmailTextView: TextView = binding.userEmail
        profileViewModel.userEmail.observe(viewLifecycleOwner) {
            userEmailTextView.text = it
        }
        val userId: TextView = binding.userId
        profileViewModel.userId.observe(viewLifecycleOwner) {
            userId.text = it
        }
        val userPhone: TextView = binding.userPhone
        profileViewModel.userPhone.observe(viewLifecycleOwner) {
            userPhone.text = it
        }
        val userDepartmentTextView: TextView = binding.userDepartment
        profileViewModel.userDepartment.observe(viewLifecycleOwner) {
            userDepartmentTextView.text = it
        }
        val userState: TextView = binding.userState
        profileViewModel.userState.observe(viewLifecycleOwner) {
            userState.text = it
        }
        val userProject: TextView = binding.userProject
        profileViewModel.userProject.observe(viewLifecycleOwner) {
            userProject.text = it
        }

        return binding.root
    }

    private fun editProfile() {
        // Get the Navigation Controller
        val navController = findNavController()

        // Navigate to the Edit Profile Fragment
        navController.navigate(R.id.nav_edit_profile)
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.fetchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}