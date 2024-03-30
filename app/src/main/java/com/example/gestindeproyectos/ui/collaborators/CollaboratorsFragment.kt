package com.example.gestindeproyectos.ui.collaborators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestindeproyectos.adapter.CollaboratorAdapter
import com.example.gestindeproyectos.databinding.FragmentCollaboratorsBinding
import com.example.gestindeproyectos.db.DB

class CollaboratorsFragment : Fragment() {
    private var _binding: FragmentCollaboratorsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollaboratorsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.collaboratorsList
        recyclerView.layoutManager = LinearLayoutManager(context)

        DB.instance.fetchCollaborators().thenAccept { collaborators ->
            activity?.runOnUiThread {
                binding.collaboratorsList.adapter = CollaboratorAdapter(collaborators)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}