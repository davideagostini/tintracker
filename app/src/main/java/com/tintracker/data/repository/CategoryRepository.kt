package com.tintracker.data.repository

import com.tintracker.data.dao.CategoryDao
import com.tintracker.data.model.Category
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module

val categoryRepositoryModule = module {
    factory { CategoryRepository(get()) }
}

class CategoryRepository(private val categoryDao: CategoryDao) {

    fun gellAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }

    suspend fun saveCategory(category: Category) {
        categoryDao.saveCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
}