package com.example.gestindeproyectos.ui.projects

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.databinding.FragmentAddTaskBinding
import com.example.gestindeproyectos.databinding.FragmentDetailProjectBinding
import com.example.gestindeproyectos.db.DB
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddTask : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null

    companion object {
        private const val TAG = "AddTask"

        fun newInstance(projectId: String): AddTask {
            val fragment = AddTask()

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
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //val textView: TextView = binding.titleProject

        //usar funcion addTask de DB
        //DB.instance.addTask(projectId.toString(), task)

        //add options to drop down menu
        val spinner: Spinner = binding.spinner1
        val spinner2: Spinner = binding.spinner2
        //create array for spinner
        val items = arrayOf("To Do", "In Progress", "Done")
        //add items to spinner
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        //fetch collaborators to create a array and add them to the spinner
        DB.instance.fetchCollaborators().thenAccept { collaborators ->
            val collaboratorList = mutableListOf<String>()
            collaborators.map {
                collaboratorList.add(it.getName())
            }
            spinner2.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, collaboratorList)
        }

        //if the user clicks the button, add the task to the database
        binding.button.setOnClickListener() {

            val desc = binding.editDescTask.text.toString()
            // get index of selected item in spinner

            var status = spinner.selectedItem.toString()
            var statusNum = 0
            if (status == "In Progress") {
                statusNum = 1
            } else if (status == "Done") {
                statusNum = 2
            }

            val sp = ((binding.editstorypoints.text.toString()).toInt()).toLong()
            val responsible = spinner2.selectedItem.toString()
            //responsible needs to be a documentreference of firebase
            val responsibleFinal = db.collection("Collaborator").document(spinner2.selectedItem.toString())

            // TODO make fun to path to responsible (reference)
            DB.instance.addTask(projectId.toString(), desc, statusNum, sp, responsibleFinal)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}