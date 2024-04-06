package com.example.gestindeproyectos.ui.collaborators

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.example.gestindeproyectos.databinding.FragmentCollaboratorBinding
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.model.CollaboratorState
import com.example.gestindeproyectos.model.Project

class CollaboratorFragment: Fragment() {
    private var _binding: FragmentCollaboratorBinding? = null

    private val binding get() = _binding!!

    private lateinit var collaboratorViewModel: CollaboratorViewModel

    companion object {
        fun newInstance(collaboratorId: String): CollaboratorFragment {
            val fragment = CollaboratorFragment()

            // Pass the project ID as an argument
            val args = Bundle()
            args.putString("collaboratorId", collaboratorId)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        collaboratorViewModel = CollaboratorViewModel(requireActivity().application, arguments?.getString("collaboratorId")!!)

        _binding = FragmentCollaboratorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set the fields for collaborator view
        val collaboratorProfilePictureView: ImageView = binding.userProfilePicture
        collaboratorViewModel.collaboratorProfilePicture.observe(viewLifecycleOwner) {
            collaboratorProfilePictureView.setImageDrawable(it)
        }
        val collaboratorNameTextView: TextView = binding.userName
        collaboratorViewModel.collaboratorName.observe(viewLifecycleOwner) {
            collaboratorNameTextView.text = it
        }
        val collaboratorEmailTextView: TextView = binding.userEmail
        collaboratorViewModel.collaboratorEmail.observe(viewLifecycleOwner) {
            collaboratorEmailTextView.text = it
        }
        val collaboratorProjectSpinner: Spinner = binding.projectSelect
        collaboratorViewModel.projectList.observe(viewLifecycleOwner) {
            collaboratorProjectSpinner.adapter = it
        }

        val saveButton: Button = binding.saveButton
        saveButton.setOnClickListener() {
            DB.instance.fetchProjects().thenAccept { projects ->
                val collaboratorId = collaboratorViewModel.collaborator.value?.getId()
                if (collaboratorId == null) {
                    Toast.makeText(context, "Can't update this collaborator", Toast.LENGTH_SHORT).show()
                    return@thenAccept
                }
                val project: Project = projects.filter { it.getName() == collaboratorProjectSpinner.selectedItem }[0]
                val projectId = project.getId()
                val state = CollaboratorState.ACTIVE
                DB.instance.updateCollaboratorWorking(collaboratorId, projectId, state)
            }
            Toast.makeText(context, "Collaborator updated!", Toast.LENGTH_SHORT).show()
        }

        val unassignButton: Button = binding.unassignButton
        unassignButton.setOnClickListener() {
            val collaboratorId = collaboratorViewModel.collaborator.value?.getId()
            if (collaboratorId == null) {
                Toast.makeText(context, "Can't update this collaborator", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val project = ""
            val state = CollaboratorState.INACTIVE
            DB.instance.updateCollaboratorWorking(collaboratorId, project, state)
            Toast.makeText(context, "Collaborator updated!", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        collaboratorViewModel.fetchData(arguments?.getString("collaboratorId")!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}