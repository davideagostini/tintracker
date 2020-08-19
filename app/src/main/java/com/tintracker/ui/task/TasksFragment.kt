package com.tintracker.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tintracker.data.model.Project
import com.tintracker.databinding.FragmentTasksBinding
import com.tintracker.ui.project.ProjectViewModel
import com.tintracker.utils.hide
import com.tintracker.utils.show
import com.tintracker.utils.toPx
import com.tintracker.utils.zeroTimeOfDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private var adapter = TaskListAdapter(mutableListOf())
    private val projectViewModel: ProjectViewModel by viewModel()
    private val taskViewModel: TaskViewModel by viewModel()
    private val projectMap = mutableMapOf<Int, Project>()
    private var date = Date()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.listTask.hasFixedSize()
        binding.listTask.adapter = adapter

        getProjects()

        return view
    }

    private fun getProjects() {
        projectViewModel.getProjects()
        projectViewModel.projectsLiveData.observe(requireActivity(), Observer { state ->
            if (state.isNotEmpty()) {
                for (r in state) {
                    projectMap[r.id] = r
                }
                taskViewModel.getAllTasks()
                getTaskList()
            } else {
                binding.listTask.hide()
                binding.noDataImageView.show()
                binding.noDataTextView.show()
            }
        })
    }

    private fun getTaskList() {
        taskViewModel.tasksLiveData.observe(requireActivity(), Observer { state ->
            if (state.isNotEmpty()) {

                var tempDate = date
                val list = mutableListOf<Any>()
                state.forEachIndexed { index, item ->
                    if (zeroTimeOfDate(Date(item.from!!)) == zeroTimeOfDate(tempDate)) {

                        if (index == 0) list.add(tempDate)
                        list.add(item)

                    } else {
                        tempDate = zeroTimeOfDate(Date(item.from!!))
                        list.add(tempDate)
                        list.add((item))
                    }
                }

                adapter.setTask(list, projectMap)
                binding.listTask.setPadding(0, 0, 0, 180.toPx)
                binding.listTask.scrollToPosition(0)

                binding.listTask.show()
                binding.noDataImageView.hide()
                binding.noDataTextView.hide()
            } else {
                binding.listTask.hide()
                binding.noDataImageView.show()
                binding.noDataTextView.show()
            }
        })
    }

}
