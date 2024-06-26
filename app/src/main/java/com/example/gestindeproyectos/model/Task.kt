package com.example.gestindeproyectos.model

import com.google.firebase.firestore.DocumentReference

class Task(
    private val id: String,
    private var description: String,
    private var state: State,
    private var storyPoints: Int,
    private var responsible: DocumentReference
) {
    fun getId(): String {
        return id
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getState(): State {
        return state
    }

    fun setState(state: State) {
        this.state = state
    }

    fun getStoryPoints(): Int {
        return storyPoints
    }

    fun setStoryPoints(storyPoints: Int) {
        this.storyPoints = storyPoints
    }

    fun getResponsible(): DocumentReference {
        return responsible
    }

    fun setResponsible(responsible: DocumentReference) {
        this.responsible = responsible
    }
}