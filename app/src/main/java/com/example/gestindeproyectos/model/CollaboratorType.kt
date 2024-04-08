package com.example.gestindeproyectos.model

enum class CollaboratorType(val value: Int) {
    NONE(0),
    MANAGER(1),
    RESPONSIBLE(2);

    companion object {
        fun fromValue(value: Int): CollaboratorType {
            return when (value) {
                0 -> NONE
                1 -> MANAGER
                2 -> RESPONSIBLE
                else -> throw IllegalArgumentException("Invalid value")
            }
        }

        fun fromOrdinal(ordinal: String): CollaboratorType {
            return when (ordinal.uppercase()) {
                "NONE" -> NONE
                "MANAGER" -> MANAGER
                "RESPONSIBLE" -> RESPONSIBLE
                else -> throw IllegalArgumentException("Invalid value")
            }
        }
    }
}