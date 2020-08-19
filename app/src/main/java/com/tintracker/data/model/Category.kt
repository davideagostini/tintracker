package com.tintracker.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = Category.TABLE_NAME)
class Category(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String
):Parcelable {
    companion object {
        const val TABLE_NAME = "tintracker_categories"

        fun populateCategory(): List<Category> {
            return listOf(
                Category(name = "Meeting"),
                Category(name = "Support")
            )
        }
    }
}