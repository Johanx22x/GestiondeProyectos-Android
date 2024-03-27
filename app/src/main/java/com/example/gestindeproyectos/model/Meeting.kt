package com.example.gestindeproyectos.model

import com.google.firebase.Timestamp

class Meeting (
    private val id: String,
    private var timestamp: Timestamp,
    private var subject: String,
    private var via: String,
    private var linkOrPlace: String,
    private var members: List<Collaborator>
) {
    fun getId(): String {
        return id
    }

    fun getTimestamp(): Timestamp {
        return timestamp
    }

    fun setTimestamp(timestamp: Timestamp) {
        this.timestamp = timestamp
    }

    fun getSubject(): String {
        return subject
    }

    fun setSubject(subject: String) {
        this.subject = subject
    }

    fun getVia(): String {
        return via
    }

    fun setVia(via: String) {
        this.via = via
    }

    fun getLinkOrPlace(): String {
        return linkOrPlace
    }

    fun setLinkOrPlace(linkOrPlace: String) {
        this.linkOrPlace = linkOrPlace
    }

    fun getMembers(): List<Collaborator> {
        return members
    }

    fun setMembers(members: List<Collaborator>) {
        this.members = members
    }
}