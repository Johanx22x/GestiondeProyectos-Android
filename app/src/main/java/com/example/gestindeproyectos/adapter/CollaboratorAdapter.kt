package com.example.gestindeproyectos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.model.Collaborator
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class CollaboratorAdapter(private val collaborators: List<Collaborator>) : RecyclerView.Adapter<CollaboratorAdapter.CollaboratorViewHolder>() {

    class CollaboratorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val collaboratorProfilePicture: ImageView = itemView.findViewById(R.id.collaborator_profile_picture)
        val collaboratorName: TextView = itemView.findViewById(R.id.collaborator_name)
        val collaboratorEmail: TextView = itemView.findViewById(R.id.collaborator_email)
        val collaboratorPhone: TextView = itemView.findViewById(R.id.collaborator_phone)
        val collaboratorType: TextView = itemView.findViewById(R.id.collaborator_type)
        val collaboratorProject: TextView = itemView.findViewById(R.id.collaborator_project)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollaboratorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.collaborator_card, parent, false)
        return CollaboratorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CollaboratorViewHolder, position: Int) {
        val currentItem = collaborators[position]
        getProfilePicture(currentItem, holder)
        holder.collaboratorName.text = currentItem.getFullName()
        holder.collaboratorEmail.text = currentItem.getEmail()
        holder.collaboratorPhone.text = currentItem.getPhone()
        // TODO: Handle collaborator type in a better way
        holder.collaboratorType.text = currentItem.getType().toString()
        if (currentItem.getProject() == "") {
            holder.collaboratorProject.text = currentItem.getProject()
            return
        }
        DB.instance.fetchProject(currentItem.getProject()).thenAccept { project ->
            if (project == null) {
                holder.collaboratorProject.text = "No project"
                return@thenAccept
            }
            holder.collaboratorProject.text = project.getName()
        }
    }

    private fun getProfilePicture(collaborator: Collaborator, holder: CollaboratorViewHolder) {
        val storageReference = FirebaseStorage.getInstance().reference.child("profile_pictures/${collaborator.getId()}")
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).resize(200, 200).centerCrop().into(holder.collaboratorProfilePicture)
        }.addOnFailureListener {
            holder.collaboratorProfilePicture.setImageResource(R.drawable.menu_profile)
        }
    }

    override fun getItemCount() = collaborators.size
}