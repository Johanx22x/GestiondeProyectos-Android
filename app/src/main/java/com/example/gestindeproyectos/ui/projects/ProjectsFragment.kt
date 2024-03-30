package com.example.gestindeproyectos.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestindeproyectos.adapter.ProjectAdapter
import com.example.gestindeproyectos.databinding.FragmentProjectsBinding
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.model.CollaboratorType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProjectsFragment: Fragment() {

    private var _binding: FragmentProjectsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView: RecyclerView = binding.projectList
        recyclerView.layoutManager = LinearLayoutManager(context)

        val currentUser = FirebaseAuth.getInstance().currentUser as FirebaseUser

        DB.instance.fetchCollaboratorWithEmail(currentUser.email!!).thenAccept { collaborator ->
            activity?.runOnUiThread {
                if (collaborator == null) {
                    return@runOnUiThread
                }
                if (collaborator.getType() == CollaboratorType.MANAGER) {
                    DB.instance.fetchProjects().thenAccept { projectList ->
                        activity?.runOnUiThread {
                            binding.projectList.adapter = ProjectAdapter(projectList, findNavController())
                }
            }
                } else {
                    DB.instance.fetchProject(collaborator.getProject()).thenAccept { project ->
                        activity?.runOnUiThread {
                            binding.projectList.adapter = ProjectAdapter(listOf(project!!), findNavController())
                        }
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}