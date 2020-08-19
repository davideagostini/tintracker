package com.tintracker.data.dao

import androidx.room.*
import com.tintracker.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM ${Category.TABLE_NAME}")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Insert
    fun insertAll(list: List<Category>)
}