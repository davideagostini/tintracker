package com.tintracker.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tintracker.data.model.Project.Companion.TABLE_NAME
import kotlinx.android.parcel.Parcelize

enum class StatusProject {
    ONGOING,
    CLOSED
}

@Parcelize
@Entity(tableName = TABLE_NAME)
data class Project (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "client_name")
    var clientName: String? = null,
    @ColumnInfo(name = "project_name")
    var projectName: String? = null,
    var color: String? = null,
    @ColumnInfo(name = "hourly_rate")
    var hourlyRate: Float?  = 0f,
    var status: StatusProject? = StatusProject.ONGOING
): Parcelable {
    companion object {
        const val TABLE_NAME = "tintracker_projects"
        val colors = listOf(
            "orange",
            "yellow",
            "lightGreen",
            "green",
            "lightBlue",
            "blue",
            "grey",
            "red",
            "pink",
            "violet"
        )


        fun populateProject(): List<Project> {
            return listOf(
                Project(
                    clientName = "Google",
                    projectName = "Gmail client",
                    color = colors[3],
                    hourlyRate = 70f,
                    status = StatusProject.ONGOING
                )
            )
        }
    }
}