package com.example.gestindeproyectos.model

class Collaborator (
    private val id: String,
    private var identification: String,
    private var name: String,
    private var lastName: String,
    private var email: String,
    private var phone: String,
    private var department: String,
    private var state: CollaboratorState,
    private var project: Project?,
    private var type: CollaboratorType
) {
    fun getFullName(): String {
        return "$name $lastName"
    }

    fun getId(): String {
        return id
    }

    fun setIdentification(identification: String) {
        this.identification = identification
    }

    fun getIdentification(): String {
        return identification
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getName(): String {
        return name
    }

    fun setLastName(lastName: String) {
        this.lastName = lastName
    }

    fun getLastName(): String {
        return lastName
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getEmail(): String {
        return email
    }

    fun setPhone(phone: String) {
        this.phone = phone
    }

    fun getPhone(): String {
        return phone
    }

    fun setDepartment(department: String) {
        this.department = department
    }

    fun getDepartment(): String {
        return department
    }

    fun setState(state: CollaboratorState) {
        this.state = state
    }

    fun getState(): CollaboratorState {
        return state
    }

    fun setProject(project: Project) {
        this.project = project
    }

    fun getProject(): Project? {
        return project
    }

    fun setType(type: CollaboratorType) {
        this.type = type
    }

    fun getType(): CollaboratorType {
        return type
    }
}