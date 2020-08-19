package com.tintracker.ui.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tintracker.data.model.Project
import com.tintracker.data.model.Task
import com.tintracker.data.repository.ProjectRepository
import com.tintracker.data.repository.TaskRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.dsl.module

val statisticModule = module {
    factory { StatisticViewModel(get(), get()) }
}

class StatisticViewModel(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _statisticsWeek = MutableLiveData<List<Task>>()
    private val _statisticsDay = MutableLiveData<List<Task>>()

    val statisticsWeek: LiveData<List<Task>>
        get() = _statisticsWeek

    val statisticsDay: LiveData<List<Task>>
        get() = _statisticsDay

    fun statisticsWeek(start: Long, end: Long) {
        viewModelScope.launch {
            taskRepository.statisticsWeek(start, end).collect {
                _statisticsWeek.value = it
            }
        }
    }

    fun statisticsDay(start: Long, end: Long) {
        viewModelScope.launch {
            taskRepository.statisticsDay(start, end).collect {
                _statisticsDay.value = it
            }
        }
    }

    private val _projectsLiveData = MutableLiveData<List<Project>>()

    val projectsLiveData: LiveData<List<Project>>
        get() = _projectsLiveData

    fun getProjects() {
        viewModelScope.launch {
            projectRepository.getAllProjects().collect {
                _projectsLiveData.value = it
            }
        }
    }
}