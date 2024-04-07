package com.example.gestindeproyectos.ui.projects

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.databinding.FragmentDetailProjectBinding
import com.example.gestindeproyectos.databinding.FragmentEditDetailsBinding
import com.example.gestindeproyectos.db.DB

class EditDetailsFragment : Fragment() {

    private var _binding: FragmentEditDetailsBinding? = null

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
        _binding = FragmentEditDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textViewName: TextView = binding.editName
        val textViewDesc: TextView = binding.editDesc
        val textViewBudget: TextView = binding.editBudget

        val updaterButton : Button = binding.buttonUpdateDetails
        updaterButton.setOnClickListener {
            // DB.instance.updateDetails(
            //     projectId.toString(),
            //     textViewName.toString(),
            //     textViewDesc.toString()
            // )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}