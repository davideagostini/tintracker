package com.tintracker.data.model

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = Task.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("project_id"),
            onDelete = CASCADE
        )
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var description: String? = null,
    @Embedded(prefix = "category_")
    var category: Category? = null,
    @ColumnInfo(index = true, name = "project_id") var projectId: Int? = null,
    @ColumnInfo(index = true, name = "from_time") var from: Long? = null,
    @ColumnInfo(index = true, name = "to_time") var to: Long? = null

): Parcelable {
    companion object {
        const val TABLE_NAME = "taimtracker_tasks"
    }
}