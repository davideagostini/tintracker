package com.tintracker.ui.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.tintracker.R
import com.tintracker.data.model.Project
import com.tintracker.data.model.StatusProject
import com.tintracker.utils.convertStringColorToResourceColor
import com.tintracker.utils.hide
import com.tintracker.utils.show
import kotlinx.android.synthetic.main.project_item.view.*

enum class ItemProjectClick {
    START_TIMER,
    DETAIL
}

class ProjectListAdapter(
    private var listener: (Project, ItemProjectClick) -> Unit
) : androidx.recyclerview.widget.ListAdapter<Project, ProjectListAdapter.ProjectHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.project_item, parent, false)
        return ProjectHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProjectHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(project: Project) = with(view) {
            tvClient.text = project.clientName
            tvProject.text = project.projectName
            val roundHourlyRateDecimal = String.format("%.2f", project.hourlyRate)
            tvHourlyRate.text = "${context.getString(R.string.hourly_rate_home)}: ${roundHourlyRateDecimal} $"
            val intColor = convertStringColorToResourceColor(project.color!!)
            layoutStart.setBackgroundColor(ContextCompat.getColor(context, intColor))

            if (project.status!!.name == StatusProject.ONGOING.name) {
                layoutStart.setOnClickListener {
                    listener(project, ItemProjectClick.START_TIMER)
                }
                ivPlay.show()
                tvTextStart.text = context.getString(R.string.start)
                tvTextStart.setTextColor(ContextCompat.getColor(context, R.color.project_locked_color))
                ivPlay.setColorFilter(ContextCompat.getColor(context, R.color.project_locked_color), android.graphics.PorterDuff.Mode.MULTIPLY)
                ivLock.hide()
            } else {
                ivPlay.hide()
                tvTextStart.text = context.getString(R.string.closed)
                tvTextStart.setTextColor(ContextCompat.getColor(context, R.color.white))
                ivLock.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
                ivLock.show()
            }


            layoutDetailProject.setOnClickListener {
                listener(project, ItemProjectClick.DETAIL)
            }
        }
    }

    private object DiffCallback: DiffUtil.ItemCallback<Project>() {
        override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.clientName == newItem.clientName
                    && oldItem.projectName == newItem.projectName
                    && oldItem.color == newItem.color
                    && oldItem.hourlyRate == newItem.hourlyRate
                    && oldItem.status == newItem.status
        }
    }
}