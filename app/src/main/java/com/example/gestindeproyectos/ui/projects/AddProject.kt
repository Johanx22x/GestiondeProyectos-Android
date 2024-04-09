package com.example.gestindeproyectos.ui.projects

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gestindeproyectos.databinding.FragmentAddProjectBinding
import com.example.gestindeproyectos.db.DB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.time.Instant

class AddProject : Fragment() {
    private var _binding: FragmentAddProjectBinding? = null
    private val auth = FirebaseAuth.getInstance()
    companion object {
        private const val TAG = "AddProject"

        fun newInstance(projectId: String): AddProject {
            val fragment = AddProject()

            // Pass the project ID as an argument
            val args = Bundle()
            args.putString("projectId", projectId)
            fragment.arguments = args

            return fragment
        }
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val projectId = arguments?.getString("projectId")
        _binding = FragmentAddProjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //if the user clicks the button, add the task to the database
        binding.button2.setOnClickListener() {

            val name = binding.editTextText.text.toString()
            val desc = binding.editTextText2.text.toString()


            //user actual
            val emailRef = auth.currentUser!!.email.toString()
            Log.d(AddProject.TAG, "${auth.currentUser!!.email.toString()}")
            db.collection("Collaborator").whereEqualTo("email", emailRef).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val responsibleFinal = db.collection("Collaborator").document(document.id)
                    Log.d(AddProject.TAG, "${document.id} => ${document.data}")
                    Log.d(AddProject.TAG, "${document.id}")
                    val timeS = com.google.firebase.Timestamp.now()
                    DB.instance.addProject(name, desc, timeS, listOf(responsibleFinal))
                }
            }

            // change the fragment to the previous one
            val fragment = DetailProjectFragment.newInstance(projectId.toString())
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(androidx.appcompat.R.id.home, fragment)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}