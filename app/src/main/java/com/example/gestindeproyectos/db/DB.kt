package com.example.gestindeproyectos.db

import android.util.Log
import com.example.gestindeproyectos.model.Collaborator
import com.example.gestindeproyectos.model.CollaboratorState
import com.example.gestindeproyectos.model.CollaboratorType
import com.example.gestindeproyectos.model.Project
import com.example.gestindeproyectos.model.State
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

class DB {
    private val db = Firebase.firestore

    companion object {
        val instance = DB()
        const val TAG = "DB"
    }

    fun fetchProjects(): CompletableFuture<List<Project>> {
        val future = CompletableFuture<List<Project>>()
        db.collection("Project")
            .get()
            .addOnSuccessListener { result ->
                val projects = mutableListOf<Project>()
                for (document in result) {
                    if (!document.exists()) {
                        continue
                    }
                    try {
                        val project = Project(
                            document.id,
                            document.data["name"] as String,
                            document.data["description"] as String,
                            document.data["initialDate"] as Timestamp,
                            emptyList(), // resources
                            document.data["budget"] as Long,
                            emptyList(), // collaborators
                            emptyList(), // tasks
                            State.fromValue((document.data["state"] as Long).toInt()),
                            null,
                            emptyList(), // changeHistory
                            emptyList(), // meetings
                            null // forum
                        )
                        projects.add(project)
                    } catch (e: Exception) {
                        Log.e("DB", "Error parsing project", e)
                    }
                }
                future.complete(projects)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
                future.completeExceptionally(exception)
            }

        return future
    }

    fun fetchProject(id: String): CompletableFuture<Project?> {
        val future = CompletableFuture<Project?>()
        db.collection("Project")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if (!document.exists()) {
                        future.complete(null)
                        return@addOnSuccessListener
                    }
                    val project = Project(
                        document.id,
                        document.data!!["name"] as String,
                        document.data!!["description"] as String,
                        document.data!!["initialDate"] as Timestamp,
                        emptyList(), // resources
                        document.data!!["budget"] as Long,
                        emptyList(), // collaborators
                        emptyList(), // tasks
                        State.fromValue((document.data!!["state"] as Long).toInt()),
                        null,
                        emptyList(), // changeHistory
                        emptyList(), // meetings
                        null // forum
                    )
                    future.complete(project)
                } else {
                    future.complete(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
                future.completeExceptionally(exception)
            }

        return future
    }

    fun fetchCollaborators(): CompletableFuture<List<Collaborator>> {
        val future = CompletableFuture<List<Collaborator>>()
        db.collection("Collaborator")
            .get()
            .addOnSuccessListener { result ->
                val collaborators = mutableListOf<Collaborator>()
                for (document in result) {
                    val projectReference = document.data["project"] as DocumentReference
                    val collaborator = Collaborator(
                        document.id,
                        document.data["identification"] as String,
                        document.data["name"] as String,
                        document.data["lastname"] as String,
                        document.data["email"] as String,
                        document.data["phone"] as String,
                        document.data["department"] as String,
                        CollaboratorState.fromValue((document.data["state"] as Long).toInt()),
                        projectReference.id,
                        CollaboratorType.fromValue((document.data["type"] as Long).toInt())
                    )
                    collaborators.add(collaborator)
                }
                future.complete(collaborators)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
                future.completeExceptionally(exception)
            }

        return future
    }

    fun fetchCollaborator(email: String): CompletableFuture<Collaborator?> {
        val future = CompletableFuture<Collaborator?>()
        db.collection("Collaborator")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    future.complete(null)
                } else {
                    val document = result.documents[0]
                    val projectReference = document.data!!["project"] as DocumentReference
                    val collaborator = Collaborator(
                        document.id,
                        document.data?.get("id") as String,
                        document.data!!["name"] as String,
                        document.data!!["lastname"] as String,
                        document.data!!["email"] as String,
                        document.data!!["phone"] as String,
                        document.data!!["department"] as String,
                        CollaboratorState.fromValue((document.data!!["state"] as Long).toInt()),
                        projectReference.id,
                        CollaboratorType.fromValue((document.data!!["type"] as Long).toInt())
                    )
                    future.complete(collaborator)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
                future.completeExceptionally(exception)
            }

        return future
    }

    fun updateCollaborator(id: String, phone: String, department: String) {
        Log.d(TAG, "Updating collaborator $id, phone: $phone, department: $department")
        db.collection("Collaborator")
            .document(id)
            .update(
                mapOf(
                    "phone" to phone,
                    "department" to department
                )
            )
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating document", e) }
    }
}