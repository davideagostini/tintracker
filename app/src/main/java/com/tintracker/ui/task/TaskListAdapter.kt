package com.tintracker.ui.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tintracker.R
import com.tintracker.data.model.Project
import com.tintracker.data.model.Task
import com.tintracker.utils.calculateDiffTwoDate
import com.tintracker.utils.convertDateDayMonthYear
import com.tintracker.utils.convertIntColorToCircleColor
import kotlinx.android.synthetic.main.day_item.view.*
import kotlinx.android.synthetic.main.task_item.view.*
import java.util.*

class TaskListAdapter(
    private val taskList: MutableList<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_TASK = 1
    private val TITLE = 2
    private var projectMap = mutableMapOf<Int, Project>()

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (taskList[position] is Task) ITEM_TASK
        else TITLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        lateinit var viewHolder: RecyclerView.ViewHolder
        val layoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            1 -> {
                val view = layoutInflater.inflate(R.layout.task_item, parent, false)
                viewHolder = TaskHolder(view)
            }
            2 -> {
                val view = layoutInflater.inflate(R.layout.day_item, parent, false)
                viewHolder = DayHolder(view)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                val taskHolder = holder as TaskHolder
                taskHolder.bind(position)
            }
            2 -> {
                val dayHolder = holder as DayHolder
                dayHolder.bind(position)
            }
        }
    }

    fun setTask(taskList: List<Any>, projectMap: MutableMap<Int, Project>) {
        this.projectMap = projectMap
        this.taskList.clear()
        this.taskList.addAll(taskList)
        notifyDataSetChanged()
    }

    inner class TaskHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) = with(view) {

            val t = taskList[position] as Task
            val project = projectMap[t.projectId]

            ivProjectColor.background = ContextCompat.getDrawable(context, convertIntColorToCircleColor(project?.color!!))
            tvProject.text = project.projectName!!.substring(0, 1).toUpperCase()
            tvTask.text = t.category?.name!!
            val diffDate = calculateDiffTwoDate(t.from!!, t.to!!)
            var hourlyRate = 0f
            if (diffDate.diffDays != 0L) {
                hourlyRate += project.hourlyRate!! * diffDate.diffDays!! * 24
            }
            if (diffDate.diffHours != 0L) {
                hourlyRate += project.hourlyRate!! * diffDate.diffHours!!
            }
            if (diffDate.diffMinutes != 0L) {
                hourlyRate += (project.hourlyRate!! / 60) * diffDate.diffMinutes!!
            }
            val roundHourlyRateTwoDecimal = String.format("%.2f", hourlyRate)
            tvDateRate.text = "${diffDate.diffHours}h ${diffDate.diffMinutes}m - ${roundHourlyRateTwoDecimal} $"

        }
    }

    inner class DayHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) = with(view) {
            val t = taskList[position] as Date
            labelDay.text = convertDateDayMonthYear(t)
        }
    }
}