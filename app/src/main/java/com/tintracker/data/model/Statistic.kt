package com.tintracker.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class StatisticType {
    TASK,
    TRACK_HOUR,
    EARN,
}

@Parcelize
data class Statistic(
    var type: StatisticType,
    var text: String? = "0"
): Parcelable