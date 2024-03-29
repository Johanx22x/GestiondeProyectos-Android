package com.example.gestindeproyectos.ui.projects

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.databinding.FragmentDetailProjectBinding

class EditDetailsFragment : Fragment() {

    private var _binding: FragmentDetailProjectBinding? = null

    companion object {
        fun newInstance(projectId: String): DetailProjectFragment {
            val fragment = DetailProjectFragment()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val projectId = arguments?.getString("projectId")
        _binding = FragmentDetailProjectBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.titleProject
        textView.text = projectId


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}