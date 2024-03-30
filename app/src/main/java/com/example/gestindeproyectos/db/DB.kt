package com.example.gestindeproyectos.db

import android.util.Log
import com.example.gestindeproyectos.model.Collaborator
import com.example.gestindeproyectos.model.CollaboratorState
import com.example.gestindeproyectos.model.CollaboratorType
import com.example.gestindeproyectos.model.Meeting
import com.example.gestindeproyectos.model.Project
import com.example.gestindeproyectos.model.State
import com.google.android.gms.tasks.Task
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
                        document.data!!["collaborators"] as? List<String> ?: emptyList(), // collaborators
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

                    // Check if the field project exists
                    val projectId = if (document.data.containsKey("project")) {
                        (document.data["project"] as DocumentReference).id
                    } else {
                        ""
                    }

                    val collaborator = Collaborator(
                        document.id,
                        document.data["identification"] as String,
                        document.data["name"] as String,
                        document.data["lastname"] as String,
                        document.data["email"] as String,
                        document.data["phone"] as String,
                        document.data["department"] as String,
                        CollaboratorState.fromValue((document.data["state"] as Long).toInt()),
                        projectId,
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

                    // Check if the field project exists
                    val projectId = if (document.data?.containsKey("project") == true) {
                        (document.data!!["project"] as DocumentReference).id
                    } else {
                        ""
                    }

                    val collaborator = Collaborator(
                        document.id,
                        document.data?.get("id") as String,
                        document.data!!["name"] as String,
                        document.data!!["lastname"] as String,
                        document.data!!["email"] as String,
                        document.data!!["phone"] as String,
                        document.data!!["department"] as String,
                        CollaboratorState.fromValue((document.data!!["state"] as Long).toInt()),
                        projectId,
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


    fun updateCollaborator(id: String, name: String, lastname: String, phone: String, department: String) {
        Log.d(TAG, "Updating collaborator $id, phone: $phone, department: $department")
        db.collection("Collaborator")
            .document(id)
            .update(
                mapOf(
                    "name" to name,
                    "lastname" to lastname,
                    "phone" to phone,
                    "department" to department
                )
            )
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating document", e) }
    }

    fun addCollaborator(
        id: String,
        email: String,
        name: String,
        lastname: String,
        phone: String,
        identification: String,
        department: String,
        state: CollaboratorState,
        type: CollaboratorType
    ) {
        Log.d(TAG, "Adding collaborator $id, email: $email, name: $name, lastname: $lastname, phone: $phone, department: $department, state: $state, type: $type")
        db.collection("Collaborator")
            .document(id)
            .set(
                mapOf(
                    "email" to email,
                    "name" to name,
                    "lastname" to lastname,
                    "phone" to phone,
                    "id" to identification,
                    "department" to department,
                    "state" to state.value,
                    "type" to type.value
                )
            )
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error writing document", e) }
    }

    // Func to fetch all the meetings from a specific project
    // param: Project ID
    fun fetchMeetings(id: String): CompletableFuture<List<Meeting>> {
        val future = CompletableFuture<List<Meeting>>()
        db.collection("Project/$id/meetings")
            .get()
            .addOnSuccessListener { documents ->
                val meetingsList = mutableListOf<Meeting>()
                for (document in documents) {
                    val meeting = document.toObject(Meeting::class.java)
                    meetingsList.add(meeting)
                }
                future.complete(meetingsList)
            }
            .addOnFailureListener { exception ->
                future.completeExceptionally(exception)
            }
        return future
    }

    // Función para actualizar los detalles de un documento en Firebase
    fun updateDetails(documentId: String, newName: String, newDescription: String): Task<Void> {

        // Crea un mapa con los nuevos datos
        val newData = hashMapOf(
            "name" to newName,
            "description" to newDescription
        )

        // Actualiza el documento con los nuevos datos
        return db.collection("Project").document(documentId)
            .update(newData as Map<String, Any>)
    }
}