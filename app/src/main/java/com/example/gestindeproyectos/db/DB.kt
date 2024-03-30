package com.example.gestindeproyectos.db

import android.util.Log
import com.example.gestindeproyectos.model.Collaborator
import com.example.gestindeproyectos.model.CollaboratorState
import com.example.gestindeproyectos.model.CollaboratorType
import com.example.gestindeproyectos.model.Forum
import com.example.gestindeproyectos.model.ForumItem
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

    fun fetchCollaborator(id: String): CompletableFuture<Collaborator?> {
        val future = CompletableFuture<Collaborator?>()
        db.collection("Collaborator")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if (!document.exists()) {
                        future.complete(null)
                        return@addOnSuccessListener
                    }

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

    fun fetchCollaboratorWithEmail(email: String): CompletableFuture<Collaborator?> {
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

    fun fetchGlobalForum(): CompletableFuture<Forum> {
        val future = CompletableFuture<Forum>()
        db.collection("Forum")
            .whereEqualTo("isLocal", false)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    future.complete(null)
                } else {
                    val document = result.documents[0]
                    val forum = Forum(
                        document.id,
                        document.data!!["name"] as String,
                        document.data!!["isLocal"] as Boolean,
                        emptyList() // items
                    )
                    future.complete(forum)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
                future.completeExceptionally(exception)
            }

        return future
    }

    fun fetchForumItems(forumId: String): CompletableFuture<List<ForumItem>> {
        val future = CompletableFuture<List<ForumItem>>()
        db.collection("Forum")
            .document(forumId)
            .collection("Items")
            .orderBy("datetime", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->

                val forumItems = mutableListOf<ForumItem>()
                for (document in result) {
                    if (document.data["author"] == null) {
                        continue
                    }
                    try {
                        val forumItem = ForumItem(
                            document.id,
                            (document.data["author"] as DocumentReference).id,
                            document.data["content"] as String,
                            document.data["datetime"] as Timestamp,
                            emptyList()
                        )
                        Log.d(TAG, "Forum item: $forumItem")
                        forumItems.add(forumItem)
                    } catch (e: Exception) {
                        Log.e("DB", "Error parsing forum item", e)
                    }
                }
                future.complete(forumItems)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
                future.completeExceptionally(exception)
            }

        return future
    }

    fun fetchForumItemReplies(forumId: String, forumItemId: String): CompletableFuture<List<ForumItem>> {
        val future = CompletableFuture<List<ForumItem>>()
        db.collection("Forum")
            .document(forumId)
            .collection("Items")
            .document(forumItemId)
            .collection("Replies")
            .get()
            .addOnSuccessListener { result ->
                val forumItems = mutableListOf<ForumItem>()
                for (document in result) {
                    try {
                        val forumItem = ForumItem(
                            document.id,
                            (document.data["author"] as DocumentReference).id,
                            document.data["content"] as String,
                            document.data["date"] as Timestamp,
                            emptyList()
                        )
                        forumItems.add(forumItem)
                    } catch (e: Exception) {
                        Log.e("DB", "Error parsing forum item", e)
                    }
                }
                future.complete(forumItems)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
                future.completeExceptionally(exception)
            }

        return future
    }
}