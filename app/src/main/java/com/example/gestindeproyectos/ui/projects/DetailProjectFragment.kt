package com.example.gestindeproyectos.ui.projects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gestindeproyectos.databinding.FragmentDetailProjectBinding
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.ui.forum.ForumViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailProjectFragment : Fragment() {

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
        //textView.text = projectId
        DB.instance.fetchProject(projectId.toString()).thenAccept { project ->
            if (project != null) {
                val projectName = DB.instance.fetchMeetings(projectId.toString())
                //val projectName = project.getMeetings()
                //val projectName = project.getInitialDate()
                //val projectName = project.getInitialDate()
                val meetingsCollectionRef = db.collection("Project").document().collection("Meetings")


                textView.text = projectName.toString()
                    //descriptionTextView.text = project.description

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