package com.tintracker.ui.task

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tintracker.R
import com.tintracker.data.model.Project
import com.tintracker.databinding.FragmentTasksBinding
import com.tintracker.ui.project.ProjectViewModel
import com.tintracker.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private var adapter = TaskListAdapter(mutableListOf())
    private val projectViewModel: ProjectViewModel by viewModel()
    private val taskViewModel: TaskViewModel by viewModel()
    private val projectMap = mutableMapOf<Int, Project>()
    private var week = mutableListOf<Date>()
    private var date = Date()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val view = binding.root

        // Set Menu
        setHasOptionsMenu(true)

        binding.listTask.hasFixedSize()
        binding.listTask.adapter = adapter

        getProjects()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.date_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.date_picker){
            showStartDateTimeDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showStartDateTimeDialog() {
        val calendar = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            date = calendar.time
            fillDay(date)
            taskViewModel.getTaskByDate(zeroTimeOfDate(week[0]).time, endTimeOfDate(week[6]).time)
        }

        DatePickerDialog(
            requireActivity(),
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get((Calendar.DAY_OF_MONTH))
        ).show()
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

    private fun fillDay(date: Date) {
        week = mutableListOf<Date>()
        var calendar = Calendar.getInstance()
        calendar.time = date
        var cursor = 7
        while (cursor > 0) {
            calendar = Calendar.getInstance()
            calendar.time = date
            cursor -= 1
            calendar.add(Calendar.DAY_OF_YEAR, -cursor)
            val newDate = calendar.time
            week.add(newDate)
        }
    }

}
