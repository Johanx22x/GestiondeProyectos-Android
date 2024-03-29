package com.example.gestindeproyectos.ui.collaborators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gestindeproyectos.databinding.FragmentCollaboratorsBinding

class CollaboratorsFragment : Fragment() {
    private var _binding: FragmentCollaboratorsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var collaboratorsViewModel: CollaboratorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        collaboratorsViewModel =
            ViewModelProvider(this)[CollaboratorsViewModel::class.java]

        _binding = FragmentCollaboratorsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}