package com.example.gestindeproyectos.model

import com.google.firebase.Timestamp

class Project (
    private val id: String,
    private var name: String,
    private var description: String,
    private var initialDate: Timestamp,
    private var resources: List<Resource>,
    private var budget: Long,
    private var collaborators: List<String>,
    private var tasks: List<Task>,
    private var state: State,
    private var responsible: Collaborator?,
    private var changeHistory: List<Record>,
    private var meetings: List<Meeting>,
    private var forum: Forum?
) {
    override fun toString(): String {
        return "Project(id='$id', name='$name', description='$description', initialDate=$initialDate, resources=$resources, budget=$budget, collaborators=$collaborators, tasks=$tasks, state=$state, responsible=$responsible, changeHistory=$changeHistory, meetings=$meetings, forum=$forum)"
    }

    fun getId(): String {
        return id
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getInitialDate(): Timestamp {
        return initialDate
    }

    fun setInitialDate(initialDate: Timestamp) {
        this.initialDate = initialDate
    }

    fun getResources(): List<Resource> {
        return resources
    }

    fun setResources(resources: List<Resource>) {
        this.resources = resources
    }

    fun getBudget(): Long {
        return budget
    }

    fun setBudget(budget: Long) {
        this.budget = budget
    }

    fun getCollaborators(): List<String> {
        return collaborators
    }

    fun setCollaborators(collaborators: List<String>) {
        this.collaborators = collaborators
    }

    fun getTasks(): List<Task> {
        return tasks
    }

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
    }

    fun getState(): State {
        return state
    }

    fun setState(state: State) {
        this.state = state
    }

    fun getResponsible(): Collaborator? {
        return responsible
    }

    fun setResponsible(responsible: Collaborator) {
        this.responsible = responsible
    }

    fun getChangeHistory(): List<Record> {
        return changeHistory
    }

    fun setChangeHistory(changeHistory: List<Record>) {
        this.changeHistory = changeHistory
    }

    fun getMeetings(): List<Meeting> {
        return meetings
    }

    fun setMeetings(meetings: List<Meeting>) {
        this.meetings = meetings
    }

    fun getForum(): Forum? {
        return forum
    }

    fun setForum(forum: Forum) {
        this.forum = forum
    }
}