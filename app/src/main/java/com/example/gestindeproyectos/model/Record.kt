package com.example.gestindeproyectos.model

import com.google.firebase.Timestamp

class Record (
    private val id: String,
    private val timestamp: Timestamp,
    private val description: String,
    private val author: Collaborator
) {
    fun getId(): String {
        return id
    }

    fun getTimestamp(): Timestamp {
        return timestamp
    }

    fun getDescription(): String {
        return description
    }

    fun getAuthor(): Collaborator {
        return author
    }
}