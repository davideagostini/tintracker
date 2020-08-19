package com.tintracker.utils

import android.content.res.Resources
import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()