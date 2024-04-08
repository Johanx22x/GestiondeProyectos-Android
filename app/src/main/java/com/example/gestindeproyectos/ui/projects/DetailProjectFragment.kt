package com.example.gestindeproyectos.ui.projects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gestindeproyectos.databinding.FragmentDetailProjectBinding
import com.example.gestindeproyectos.db.DB
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailProjectFragment : Fragment() {

    private var _binding: FragmentDetailProjectBinding? = null

    companion object {
        private const val TAG = "DetailProjectFragment"

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
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val projectId = arguments?.getString("projectId")
        _binding = FragmentDetailProjectBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.titleProject
        val meetingView: TextView = binding.meetingsV
        val taskView: TextView = binding.taskv
        val resView: TextView = binding.resV
        //textView.text = projectId
        DB.instance.fetchProject(projectId.toString()).thenAccept { project ->
            if (project != null) {
                DB.instance.fetchMeetings(projectId.toString()).thenAccept { meetings ->
                    meetings.map {
                        val totalMeetings = it.getSubject() + " " + it.getVia() + " " + it.getLinkOrPlace() + " " + it.getMembers()
                        meetingView.text = totalMeetings
                    }
                }
                DB.instance.fetchTasks(projectId.toString()).thenAccept { tasks ->
                    tasks.map {
                        val totalTasks = it.getDescription() + " " + it.getState() + " " + it.getStoryPoints() + " " + it.getResponsible()
                        taskView.text = totalTasks
                    }
                }
                DB.instance.fetchResources(projectId.toString()).thenAccept { res ->
                    res.map {
                        val totalRes = it.getName() + " " + it.getDescription() + " " + it.getAmount()
                        resView.text = totalRes
                    }
                }
                val totalInfo = "Name: " + project.getName() + "\nDescription:  " + project.getDescription()
                textView.text = totalInfo
            } else {
                textView.text = "Proyecto no encontrado"
            }
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}