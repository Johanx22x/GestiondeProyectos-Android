package com.example.gestindeproyectos.model

enum class CollaboratorState(val value: Int) {
    ACTIVE(0),
    INACTIVE(1);

    companion object {
        fun fromValue(value: Int): CollaboratorState {
            return when (value) {
                0 -> ACTIVE
                1 -> INACTIVE
                else -> throw IllegalArgumentException("Invalid value")
            }
        }
    }
}