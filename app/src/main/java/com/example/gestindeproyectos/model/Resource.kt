package com.example.gestindeproyectos.model

class Resource(
    private val id: String,
    private var name: String,
    private var description: String,
    private var amount: Int
) {
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

    fun getAmount(): Int {
        return amount
    }

    fun setAmount(amount: Int) {
        this.amount = amount
    }
}