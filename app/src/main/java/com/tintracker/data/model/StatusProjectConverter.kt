package com.tintracker.data.model

import androidx.room.TypeConverter

class StatusProjectConverter {

    @TypeConverter
    fun toType(type: String): StatusProject {
        return if (type == StatusProject.ONGOING.name) {
            StatusProject.ONGOING
        } else if (type == StatusProject.CLOSED.name) {
            StatusProject.CLOSED
        }
        else {
            throw IllegalArgumentException("Could not recognize type")
        }
    }

    @TypeConverter
    fun toString(find: StatusProject): String? {
        return find.toString()
    }
}