package com.tintracker.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tintracker.data.model.Category
import com.tintracker.data.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.dsl.module

val categoryModule = module {
    factory { CategoryViewModel(get()) }
}

class CategoryViewModel(
    private val categoryRepository: CategoryRepository
):ViewModel() {

    private val _categoriesLiveData = MutableLiveData<List<Category>>()

    val categoriesLiveData: LiveData<List<Category>>
        get() = _categoriesLiveData

    fun getCategories() {
        viewModelScope.launch {
            categoryRepository.gellAllCategories().collect {
                _categoriesLiveData.value = it
            }
        }
    }

    fun saveCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.saveCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.deleteCategory(category)
        }
    }

}