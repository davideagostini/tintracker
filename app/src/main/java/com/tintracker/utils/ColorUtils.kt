package com.tintracker.utils

import com.tintracker.R
import com.tintracker.data.model.Project

fun convertIntColorToCircleColor(value: String) : Int {
    return when (value) {
        "orange" -> R.drawable.circle_orange
        "yellow" -> R.drawable.circle_yellow
        "lightGreen" -> R.drawable.circle_light_green
        "green" -> R.drawable.circle_green
        "lightBlue" -> R.drawable.circle_light_blue
        "blue" -> R.drawable.circle_blue
        "grey" -> R.drawable.circle_grey
        "red" ->  R.drawable.circle_red
        "pink" -> R.drawable.circle_pink
        "violet" -> R.drawable.circle_violet
        else -> R.drawable.circle_orange
    }
}

fun convertStringColorToResourceColor(value: String) : Int {
    return when (value) {
        "orange" -> R.color.pOrange
        "yellow" -> R.color.pYellow
        "lightGreen" -> R.color.pLightGreen
        "green" -> R.color.pGreen
        "lightBlue" -> R.color.pLightBlue
        "blue" -> R.color.pBlue
        "grey" -> R.color.pGrey
        "red" ->  R.color.pRed
        "pink" -> R.color.pPink
        "violet" -> R.color.pViolet
        else -> R.color.pOrange
    }
}

fun getListPositionByColor(value: String): Int {
    var index = 0
    for ((i, v) in Project.colors.withIndex()) {
        if (v == value) {
            index = i
            break
        }
    }
    return index
}
