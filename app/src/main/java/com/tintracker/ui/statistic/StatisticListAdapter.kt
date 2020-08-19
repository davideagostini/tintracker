package com.tintracker.ui.statistic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tintracker.R
import com.tintracker.data.model.ItemStatistic
import com.tintracker.data.model.Statistic
import com.tintracker.data.model.StatisticType
import kotlinx.android.synthetic.main.item_statistics.view.*
import kotlinx.android.synthetic.main.item_title_statistic.view.*

class StatisticListAdapter(
    private val statisticList: MutableList<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM = 1
    private val TITLE = 2

    override fun getItemCount(): Int {
        return statisticList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (statisticList[position] is ItemStatistic) ITEM
        else TITLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        lateinit var viewHolder: RecyclerView.ViewHolder
        val layoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            1 -> {
                val view = layoutInflater.inflate(R.layout.item_statistics, parent, false)
                viewHolder = StatisticHolder(view)
            }
            2 -> {
                val view = layoutInflater.inflate(R.layout.item_title_statistic, parent, false)
                viewHolder = DayHolder(view)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                val statisticHolder = holder as StatisticHolder
                statisticHolder.bind(position)
            }
            2 -> {
                val dayHolder = holder as DayHolder
                dayHolder.bind(position)
            }
        }
    }

    fun addStatistic(statistics: List<Any>) {
        this.statisticList.clear()
        this.statisticList.addAll(statistics)
        notifyDataSetChanged()
    }

    fun updateDayStatisticItem(statistic: Statistic, position: Int) {
        (this.statisticList[position] as ItemStatistic).day = statistic
        notifyItemChanged(position)
    }

    fun updateWeekStatisticItem(statistic: Statistic, position: Int) {
        (this.statisticList[position] as ItemStatistic).week = statistic
        notifyItemChanged(position)
    }

    inner class StatisticHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) = with(view) {
            val s = statisticList[position] as ItemStatistic
            imageViewDay.setImageResource(getIcon(s.day?.type))
            tvTrackDay.text = s.day?.text
            imageViewWeek.setImageResource(getIcon(s.week?.type))
            tvTrackWeek.text = s.week?.text
        }

        private fun getIcon(type: StatisticType? = null): Int {
            return when(type) {
                StatisticType.TASK -> R.drawable.ic_tasks
                StatisticType.TRACK_HOUR -> R.drawable.ic_clock
                StatisticType.EARN -> R.drawable.ic_money
                else -> R.drawable.ic_square
            }
        }
    }

    inner class DayHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) = with(view) {
            val t = statisticList[position] as String
            labelDay.text = t
        }
    }
}