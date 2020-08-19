package com.tintracker.ui.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tintracker.R
import com.tintracker.data.model.ItemStatistic
import com.tintracker.data.model.Project
import com.tintracker.data.model.Statistic
import com.tintracker.data.model.StatisticType
import com.tintracker.databinding.FragmentStatisticsBinding
import com.tintracker.utils.calculateDiffTwoDate
import com.tintracker.utils.endTimeOfDate
import com.tintracker.utils.zeroTimeOfDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val statisticViewModel: StatisticViewModel by viewModel()
    private var adapter = StatisticListAdapter(mutableListOf())
    private val projectMap = mutableMapOf<Int, Project>()
    private var date = Date()
    private val week = mutableListOf<Date>()
    private var itemStatistic = mutableListOf<Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val view = binding.root

        itemStatistic = mutableListOf(
            getString(R.string.number_tasks),
            ItemStatistic(
                day = Statistic(
                    type = StatisticType.TASK,
                    text = "0"
                ),
                week = Statistic(
                    type = StatisticType.TASK,
                    text = "0"
                )
            ),

            getString(R.string.tracked),
            ItemStatistic(
                day = Statistic(
                    type = StatisticType.TRACK_HOUR,
                    text = "0h 0m"
                ),
                week = Statistic(
                    type = StatisticType.TRACK_HOUR,
                    text = "0h 0m"
                )
            ),
            getString(R.string.billable),
            ItemStatistic(
                day = Statistic(
                    type = StatisticType.EARN,
                    text = "0 $"
                ),
                week = Statistic(
                    type = StatisticType.EARN,
                    text = "0 $"
                )
            )
        )
        binding.listStatistics.hasFixedSize()
        binding.listStatistics.adapter = adapter
        adapter.addStatistic(itemStatistic)
        fillDay()
        getProjects()

        return view
    }

    private fun getProjects() {
        statisticViewModel.getProjects()
        statisticViewModel.projectsLiveData.observe(requireActivity(), Observer { state ->
            if (state.isNotEmpty()) {
                for (r in state) {
                    projectMap[r.id] = r
                }

                getStatisticsDay(Date(), Date())
                getStatisticsWeek(week[0], week[6])

            }
        })
    }

    private fun getStatisticsDay(from: Date, to: Date) {
        statisticViewModel.statisticsDay(zeroTimeOfDate(from).time, endTimeOfDate(to).time)
        statisticViewModel.statisticsDay.observe(requireActivity(), Observer { state ->
            if (state.isNotEmpty()) {
                var hours = 0L
                var minutes = 0L
                var hourlyRate = 0f
                for (t in state) {
                    val diffDate = calculateDiffTwoDate(t.from!!, t.to!!)
                    minutes += diffDate.diffMinutes!!
                    hours += diffDate.diffHours!!
                    val p = projectMap[t.projectId]
                    if (hours != 0L) {
                        hourlyRate += p?.hourlyRate!! * diffDate.diffHours!!
                    }
                    if (diffDate.diffMinutes != 0L) {
                        hourlyRate += (p?.hourlyRate!! / 60) * diffDate.diffMinutes!!
                    }
                }
                hours += (minutes / 60).toInt()
                minutes = ((minutes % 60).toInt()).toLong()

                val roundHourlyRateTwoDecimal =
                    String.format("%.2f", hourlyRate)

                val sTask = Statistic(
                    type = StatisticType.TASK,
                    text = state.size.toString()
                )

                val sHour = Statistic(
                    type = StatisticType.TRACK_HOUR,
                    text = "${hours}h ${minutes}m"
                )

                val sEarn = Statistic(
                    type = StatisticType.EARN,
                    text = "$roundHourlyRateTwoDecimal $"
                )

                adapter.updateDayStatisticItem(sTask, 1)
                adapter.updateDayStatisticItem(sHour, 3)
                adapter.updateDayStatisticItem(sEarn, 5)
            }
        })
    }

    private fun getStatisticsWeek(from: Date, to: Date) {
        statisticViewModel.statisticsWeek(zeroTimeOfDate(from).time, endTimeOfDate(to).time)
        statisticViewModel.statisticsWeek.observe(requireActivity(), Observer { state ->
            if (state.isNotEmpty()) {
                var hours = 0L
                var minutes = 0L
                var hourlyRate = 0f
                for (t in state) {
                    val diffDate = calculateDiffTwoDate(t.from!!, t.to!!)
                    minutes += diffDate.diffMinutes!!
                    hours += diffDate.diffHours!!
                    val p = projectMap[t.projectId]
                    if (hours != 0L) {
                        hourlyRate += p?.hourlyRate!! * diffDate.diffHours!!
                    }
                    if (diffDate.diffMinutes != 0L) {
                        hourlyRate += (p?.hourlyRate!! / 60) * diffDate.diffMinutes!!
                    }
                }
                hours += (minutes / 60).toInt()
                minutes = ((minutes % 60).toInt()).toLong()
                val roundHourlyRateTwoDecimal =
                    String.format("%.2f", hourlyRate)
                val sTask = Statistic(
                    type = StatisticType.TASK,
                    text = state.size.toString()
                )

                val sHour = Statistic(
                    type = StatisticType.TRACK_HOUR,
                    text = "${hours}h ${minutes}m"
                )

                val sEarn = Statistic(
                    type = StatisticType.EARN,
                    text = "$roundHourlyRateTwoDecimal $"
                )

                adapter.updateWeekStatisticItem(sTask, 1)
                adapter.updateWeekStatisticItem(sHour, 3)
                adapter.updateWeekStatisticItem(sEarn, 5)
            }
        })
    }

    private fun fillDay() {
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
