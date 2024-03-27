package com.example.gestindeproyectos.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gestindeproyectos.databinding.FragmentProjectsBinding

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
        val projectsViewModel =
                ViewModelProvider(this).get(ProjectsViewModel::class.java)

        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textProjects
        projectsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}