package com.example.gestindeproyectos.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.model.Project
import com.example.gestindeproyectos.ui.projects.DetailProjectFragment
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class ProjectAdapter(private val projectList: List<Project>, private val navController: NavController) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectImage: ImageView = itemView.findViewById(R.id.project_image)
        val projectName: TextView = itemView.findViewById(R.id.project_name)
        val projectDescription: TextView = itemView.findViewById(R.id.project_description)
        val projectDetailsButton: Button = itemView.findViewById(R.id.button_DetailsProject)
        val projectEditDetailsButton: Button = itemView.findViewById(R.id.edit_details_project)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.project_card, parent, false)
        return ProjectViewHolder(itemView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentItem = projectList[position]
        val storageReference = FirebaseStorage.getInstance().reference.child("projects/${currentItem.getId()}.jpg")
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(holder.projectImage)
        }.addOnFailureListener {
            holder.projectImage.setImageResource(R.drawable.side_nav_bar)
        }
        holder.projectName.text = currentItem.getName()
        holder.projectDescription.text = currentItem.getDescription()
        holder.projectDetailsButton.setOnClickListener() {
            val projectId = currentItem.getName()
            val bundle = Bundle().apply {
                putString("projectId", projectId)
            }
           navController.navigate(R.id.detail_project, bundle)
        }

        holder.projectEditDetailsButton.setOnClickListener() {
            val projectId = currentItem.getName()
            val bundle = Bundle().apply {
                putString("projectId", projectId)
            }
            navController.navigate(R.id.detail_project, bundle)
        }
    }


    override fun getItemCount() = projectList.size
}
